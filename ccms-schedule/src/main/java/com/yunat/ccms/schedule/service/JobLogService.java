package com.yunat.ccms.schedule.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.core.support.cons.TableConstant;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;
import com.yunat.ccms.core.support.statemachine.state.JobState;
import com.yunat.ccms.core.support.statemachine.state.SubjobState;
import com.yunat.ccms.node.support.NodeSQLExecutor;
import com.yunat.ccms.node.support.service.NodeJobService;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.repository.CampaignDao;
import com.yunat.ccms.schedule.repository.LogJobDao;
import com.yunat.ccms.schedule.repository.LogSubjobDao;
import com.yunat.ccms.workflow.domain.Node;

/**
 * 
 * @author xiaojing.qu
 * 
 */
@Component
@Scope("singleton")
public class JobLogService implements NodeJobService {

	private static Logger logger = LoggerFactory.getLogger(JobLogService.class);

	@Autowired
	private LogJobDao jobDao;

	@Autowired
	private LogSubjobDao subjobDao;

	@Autowired
	private CampaignDao campDao;

	@Autowired
	private NodeSQLExecutor sqlExecutor;

	public LogJob getJob(Long jobId) {
		return jobDao.get(jobId);
	}

	public void updateJob(LogJob job) {
		jobDao.update(job);
	}

	public LogJob saveJob(LogJob job) {
		return jobDao.save(job);
	}

	public LogSubjob getSubjob(Long subjobId) {
		return subjobDao.get(subjobId);
	}

	public LogSubjob getSubjob(Long jobId, Long nodeId) {
		return subjobDao.getSubjobByJobidAndNodeId(jobId, nodeId);
	}

	public LogSubjob getOrCreatSubjob(Long jobId, Long nodeId, Date plantime) {
		LogSubjob subjob = subjobDao.getSubjobByJobidAndNodeId(jobId, nodeId);
		if (subjob == null) {
			LogJob job = jobDao.get(jobId);
			Node node = new Node(nodeId);
			subjob = new LogSubjob();
			subjob.setCampId(job.getCampId());
			subjob.setJobId(jobId);
			subjob.setNode(node);
			subjob.setIsTest(job.isTest());
			subjob.setStatus(JobState.INITIAL.getCode());
			subjob.setPlantime(plantime);
			subjob = subjobDao.save(subjob);
		}
		return subjob;
	}

	public void updateSubjob(LogSubjob subjob) {
		subjobDao.update(subjob);
	}

	public LogSubjob saveSubjob(LogSubjob subjob) {
		return subjobDao.save(subjob);
	}

	public boolean checkPreNodeSuccess(Long jobId, List<Long> nodeIds) {
		boolean preNodeSuccess = true;
		List<LogSubjob> subjobList = subjobDao.getSubjobByJobidAndNodeIds(jobId, nodeIds);
		if (subjobList != null) {
			for (LogSubjob subjob : subjobList) {
				if (SubjobState.fromCode(subjob.getStatus()).isError()) {
					preNodeSuccess = false;
					break;
				}
			}
		}
		return preNodeSuccess;
	}

	/**
	 * 判断当前job是否是所属活动的最后一个在执行的job，如果返回true，则需要根据job状态更新活动状态
	 * 
	 * @param thisJob
	 * @return
	 */
	public boolean isThisLastJob(LogJob thisJob) {
		Campaign campaign = campDao.get(thisJob.getCampId());
		CampaignStatus campStatus = campaign.getCampStatus();
		CampaignState campState = CampaignState.fromCode(campStatus.getStatusId());
		boolean jobIsTest = thisJob.isTest();
		boolean campInTesting = campState.isTesting();
		if (jobIsTest != campInTesting) {
			logger.error("invalid state: Camp:{} & Job:{}", thisJob.getState(), campState);
			return false;
		}
		if (jobIsTest) {// 预执行
			return true;
		} else {// 正式执行
			List<LogJob> currentJobs = jobDao.getJobsByCampIdAndIsTest(thisJob.getCampId(), false);
			boolean isAllJobEnds = true;// 一票否决
			boolean isLastJobEnds = false;// 因为只有一个lastjob,如果当前没有lastjob，则认为lastjob没有结束
			for (LogJob logJob : currentJobs) {
				JobState jobState = logJob.getState();
				if (logJob.isLastJob()) {
					if (jobState.isFinished()) {
						isLastJobEnds = true;
					}
				}
				if (!jobState.isFinished()) {
					isAllJobEnds = false;
					break;// 有一个job没有执行完
				}
			}
			if (isAllJobEnds && isLastJobEnds) {
				return true;
			}
		}
		return false;
	}

	public void updateCampaignStatus(Long campId, boolean isTest) {
		Campaign campaign = campDao.get(campId);
		CampaignStatus campStatus = campaign.getCampStatus();
		CampaignState campState = CampaignState.fromCode(campStatus.getStatusId());
		if (isTest) {
			// 预执行在状态上不关心是否正确执行
			campaign.setCampStatus(new CampaignStatus(campState.handleFinishSuccess().getCode()));
		} else {
			List<LogJob> allJobs = jobDao.getJobsByCampIdAndIsTest(campId, false);
			boolean isAllJobSuccess = true;// 一票否决
			for (LogJob logJob : allJobs) {
				if (logJob.getState().isError()) {
					isAllJobSuccess = false;
					break;
				}
			}
			if (isAllJobSuccess) {
				campaign.setCampStatus(new CampaignStatus(campState.handleFinishSuccess().getCode()));
			} else {
				campaign.setCampStatus(new CampaignStatus(campState.handleFinishError().getCode()));
			}

		}
		Campaign updateCampaign = new Campaign();
		updateCampaign.setCampId(campaign.getCampId());
		updateCampaign.setCampStatus(campaign.getCampStatus());
		campDao.update(updateCampaign);
	}

	@Override
	public Long getSubjobId(Long jobId, Long nodeId) {
		LogSubjob subjob = subjobDao.getSubjobByJobidAndNodeId(jobId, nodeId);
		return subjob == null ? null : subjob.getSubjobId();
	}

	@Override
	public Long getCampIdByJobId(Long jobId) {
		LogJob job = jobDao.get(jobId);
		return job == null ? null : job.getCampId();
	}

	@Override
	public Long getCampIdBySubjobId(Long subjobId) {
		LogSubjob subjob = subjobDao.get(subjobId);
		return subjob == null ? null : subjob.getCampId();
	}

	/**
	 * 活动测试/正式执行前清理job
	 * 
	 * @param campaignId
	 * @param isTest
	 */
	public void cleanJobBeforeExecute(Long campaignId, boolean isTest) {
		if (isTest) {
			List<Long> subjobList = sqlExecutor.queryForList("select subjob_id  from " + TableConstant.TWF_LOG_SUBJOB
					+ " where camp_id=? and is_test=true", Long.class, campaignId);
			String allSubjobIds = StringUtils.join(subjobList, ",");
			if (allSubjobIds == null) {
				sqlExecutor.execute("delete from " + TableConstant.TWF_LOG_GROUP + " where subjob_id in ("
						+ allSubjobIds + ")");
				sqlExecutor.execute("delete from " + TableConstant.TWF_LOG_GROUP_USER + " where subjob_id in ("
						+ allSubjobIds + ")");
			}
			sqlExecutor.execute("delete from " + TableConstant.TWF_LOG_JOB + " where camp_id=?  and is_test=true",
					campaignId);
			sqlExecutor.execute("delete from " + TableConstant.TWF_LOG_SUBJOB + " where camp_id=?  and is_test=true",
					campaignId);
		}
	}

	/**
	 * 活动中止测试/正式执行重置job状态
	 * 
	 * @param campaignId
	 * @param isTest
	 */
	public void resetJobAfterStop(Long campaignId, boolean isTest) {
		List<LogJob> jobList = jobDao.getJobsByCampIdAndIsTest(campaignId, isTest);
		if (jobList != null) {
			for (LogJob job : jobList) {
				JobState jobState = job.getState();
				JobState newJobState = jobState.handleStop();
				if (jobState != newJobState) {
					logger.info("停止活动{},{},更新Job:{}->{}", new Object[] { campaignId, isTest, jobState, newJobState });
					job.setStatus(newJobState.getCode());
					job.setEndtime(new Date());
					jobDao.update(job);
				}
			}
		}

	}

}
