package com.yunat.ccms.schedule.service;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;
import com.yunat.ccms.core.support.statemachine.state.JobState;
import com.yunat.ccms.core.support.statemachine.state.SubjobState;
import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.TaskScheduler;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.repository.CampaignDao;
import com.yunat.ccms.schedule.repository.LogJobDao;
import com.yunat.ccms.schedule.repository.LogSubjobDao;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.domain.WorkFlowGraph;

@Component
@Scope("singleton")
public class RecoverService {

	private static final Logger logger = LoggerFactory.getLogger(RecoverService.class);

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private CountDownLatchService countLatchService;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private CampaignDao campDao;

	@Autowired
	private LogJobDao jobDao;

	@Autowired
	private LogSubjobDao subjobDao;

	@Autowired
	private JdbcTemplate jdbcHelper;

	@Autowired
	private org.quartz.Scheduler timer;

	/**
	 * 重启后恢复流程(只恢复正式执行的活动)
	 * 
	 * @throws SchedulerException
	 */
	public void recoverScheduelContext() {
		doCleanBeforeRecoverContext();
		List<Campaign> runningCamps = campDao.getCampaignsByStatus(CampaignState.EXECUTING.getCode());
		logger.info("监测到状态为执行中[{}]的活动共[{}]个", CampaignState.EXECUTING.getCode(), runningCamps.size());
		for (Campaign camp : runningCamps) {
			recoverCampaign(camp);
		}
		try {
			logger.info("启动基于Quartz的定时器:{}", timer.getSchedulerName());
			timer.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * 重启后恢复活动
	 */
	public void recoverCampaign(Campaign camp) {
		CampTask campTask = taskRepository.createCampTask(camp.getCampId(), false, null);
		List<LogJob> runningJobs = jobDao
				.getJobsInStatesByCampId(camp.getCampId(), JobState.getRunningJobCode(), false);
		logger.info("活动[{}, {}]中状态为执行中的job共[{}]个",
				new Object[] { camp.getCampId(), camp.getCampName(), runningJobs.size() });
		for (LogJob job : runningJobs) {
			recoverJob(job);
		}
	}

	/**
	 * 重启后恢复活动内Job
	 */
	public void recoverJob(LogJob job) {
		countLatchService.rebuildLatchForJob(job.getCampId(), job.getJobId());
		FlowTask flowTask = taskRepository.createFlowTask(job.getCampId(), job.getJobId(), job.isTest(), null);
		disruptor.fireStartEvent(flowTask);
	}

	/**
	 * 运行中恢复执行出错的节点 </br>
	 * 条件是： </br>
	 * 1：该节点是出错分支的第一个出错节点 </br>
	 * 2：该节点后面的节点已经跑完（包括跳过）
	 * 
	 * 
	 * @param jobId
	 * @param nodeId
	 * @return canRecover
	 * @throws Exception
	 */
	public boolean recoverNode(Long jobId, Long nodeId) throws Exception {
		logger.info("尝试恢复节点:jobId={},nodeId={}", new Object[] { jobId, nodeId });
		boolean canRecover = true;
		LogJob job = jobDao.get(jobId);
		logger.info("{}", job);
		LogSubjob subjob = subjobDao.getSubjobByJobidAndNodeId(jobId, nodeId);
		logger.info("{}", subjob);
		Campaign campaign = campDao.get(job.getCampId());
		logger.info("{}", campaign);
		if (campaign == null || job == null || subjob == null) {
			return false;
		}
		Long campId = job.getCampId();
		Node node = subjob.getNode();
		CampaignState campState = CampaignState.fromCode(campaign.getCampStatus().getStatusId());
		JobState jobState = job.getState();
		SubjobState subjobState = subjob.getState();
		boolean isCampRuning = campState.isRunning();
		boolean isJobRunning = jobState.isRunning();
		boolean isTestJob = job.isTest();
		if (isTestJob) {
			logger.error("预执行job不支持恢复");
			canRecover = false;
		}
		if (!(subjobState.isError() && !subjobState.isSkip())) {
			logger.error("subjob:{}不支持恢复,当前状态：{}", subjob.getJobId(), subjob.getStatus());
			canRecover = false;
		}
		if (!canRecover) {
			return canRecover;
		}
		WorkFlowGraph<Node, Connect> workFlowGraph = workflowService.getWorkFlowGraph(campId);
		WorkFlowGraph<Node, Connect> subFlowGraph = workFlowGraph.getSubGraph(node);
		List<Long> recoverNodeIds = Lists.newArrayList();
		for (Node n : subFlowGraph.getAllNodes()) {
			recoverNodeIds.add(n.getId());
		}
		logger.info("Job:{}需要恢复如下节点:{}", jobId, recoverNodeIds);
		if (!isCampRuning) {
			Campaign updateCampaign = new Campaign();
			updateCampaign.setCampId(campId);
			updateCampaign.setCampStatus(new CampaignStatus(campState.handleRecover().getCode()));
			campDao.update(updateCampaign);
		}
		job = jobDao.get(jobId);
		job.setStatus(job.getState().handleRecover().getCode());
		jobDao.update(job);
		subjobDao.deleteSubjobByJobAndNodeIds(jobId, recoverNodeIds);
		if (!isJobRunning) {
			FlowTask flowTask = taskRepository.createFlowTask(campId, jobId, job.isTest(), null);
			disruptor.fireStartEvent(flowTask);
			return canRecover;
		}
		countLatchService.resetLatchForRecoverNodes(jobId, recoverNodeIds);
		Node thisNode = workflowService.getNodeById(nodeId);
		Task task = taskRepository.createNodeTask(campId, jobId, nodeId, thisNode.getType(), job.isTest(), null);
		taskScheduler.submit(task);
		logger.info("subjob:{}提交恢复成功", subjob.getJobId());
		return canRecover;

	}

	/**
	 * 在重启后，恢复流程前
	 */
	private void doCleanBeforeRecoverContext() {
		String sql1 = "update tb_campaign set camp_status='A1' where camp_status='B1' ";
		jdbcHelper.execute(sql1);
		String sql2 = "update tb_campaign set camp_status='A2' where camp_status='B2' ";
		jdbcHelper.execute(sql2);

		String sql3 = "delete from twf_log_job where  camp_id not in (select camp_id from tb_campaign ) ";
		jdbcHelper.execute(sql3);

		String sql4 = "delete from twf_log_subjob where job_id not in (select job_id from twf_log_job) ";
		jdbcHelper.execute(sql4);

		String sql5 = "delete from twf_log_subjob where status=11 and is_test=true  ";
		// jdbcHelper.execute(sql5);

		String sql6 = "update twf_log_job set status=23 where status in (10,11) and UNIX_TIMESTAMP()-UNIX_TIMESTAMP(plantime)>60*60*24*3 and not exits (select 1 from twf_log_subjob s where job_id=s.job_id)";
		// jdbcHelper.execute(sql6);
	}

}
