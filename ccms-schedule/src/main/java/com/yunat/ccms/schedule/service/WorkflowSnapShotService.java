package com.yunat.ccms.schedule.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;
import com.yunat.ccms.core.support.statemachine.state.JobState;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.schedule.controler.vo.CampaignSnapShot;
import com.yunat.ccms.schedule.controler.vo.JobSnapShot;
import com.yunat.ccms.schedule.controler.vo.NodeSnapShot;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.repository.CampaignDao;
import com.yunat.ccms.schedule.repository.LogJobDao;
import com.yunat.ccms.schedule.repository.LogSubjobDao;

@Component
public class WorkflowSnapShotService {

	private static Logger logger = LoggerFactory.getLogger(WorkflowSnapShotService.class);

	@Autowired
	private CampaignDao campDao;

	@Autowired
	private LogJobDao jobDao;

	@Autowired
	private LogSubjobDao subjobDao;

	public ControlerResult getCampaignSnapShot(Long campaignId) {
		Campaign campaign = campDao.get(campaignId);
		if (campaign == null) {
			logger.info("活动{}不存在", campaignId);
			return ControlerResult.newError("活动" + campaignId + "不存在");
		}
		CampaignState state = CampaignState.fromCode(campaign.getCampStatus().getStatusId());
		boolean isTest = state.showTestJob();
		List<LogJob> jobs = jobDao.getJobsByCampIdAndIsTest(campaignId, isTest);
		List<JobSnapShot> jobStatusList = Lists.transform(jobs, new Function<LogJob, JobSnapShot>() {
			@Override
			public JobSnapShot apply(LogJob job) {
				JobSnapShot jobSnapShot = new JobSnapShot();
				jobSnapShot.setJobId(job.getJobId());
				jobSnapShot.setIsTest(job.isTest());
				String startTime = DateUtils.getStringDate(job.getStarttime());
				if (startTime == null) {
					startTime = DateUtils.getStringDate(job.getPlantime());
				}
				jobSnapShot.setStartTime(startTime);
				jobSnapShot.setJobStatus(job.getStatus());
				return jobSnapShot;
			}
		});
		CampaignSnapShot campaignVo = new CampaignSnapShot();
		campaignVo.setCampaignId(campaignId);
		campaignVo.setCampaignStatus(state.getCode());
		campaignVo.setJobStatusList(jobStatusList);
		return ControlerResult.newSuccess(campaignVo);
	}

	public ControlerResult getJobSnapShot(Long jobId) {
		LogJob job = jobDao.get(jobId);
		if (job == null) {
			logger.info("job:{}不存在", jobId);
			return ControlerResult.newError("job:" + jobId + "不存在");
		}
		JobState state = JobState.fromCode(job.getStatus());
		List<LogSubjob> subjobs = subjobDao.getSubjobByJobid(jobId);
		List<NodeSnapShot> nodeStatusList = Lists.transform(subjobs, new Function<LogSubjob, NodeSnapShot>() {
			@Override
			public NodeSnapShot apply(LogSubjob input) {
				return new NodeSnapShot(input);
			}
		});

		JobSnapShot jobVo = new JobSnapShot();
		jobVo.setJobId(jobId);
		jobVo.setJobStatus(state.getCode());
		jobVo.setNodeStatusList(nodeStatusList);
		return ControlerResult.newSuccess(jobVo);
	}
}
