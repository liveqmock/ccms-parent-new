package com.yunat.ccms.schedule.core.task;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.service.CountDownLatchService;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.service.WorkflowService;
import com.yunat.ccms.schedule.support.ParamHolder;
import com.yunat.ccms.schedule.support.ScheduleCons;
import com.yunat.ccms.workflow.domain.Node;

public class FlowTask extends Task {

	private static final Logger logger = LoggerFactory.getLogger(FlowTask.class);

	private final long jobId;

	public FlowTask(long campId, long jobId, boolean isTest, TaskTrigger trigger, ParamHolder extra) {
		super(campId, isTest, trigger, extra);
		this.jobId = jobId;
	}

	@Autowired
	private CountDownLatchService countDownLatchService;

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public String getTaskId() {
		return TaskNamingUtil.getFlowTaskId(campId, isTest, jobId);
	}

	@Override
	public void run() {
		try {
			action();
		} catch (Exception e) {// TO prevent Thread leak
			e.printStackTrace();
		}
	}

	public long getJobId() {
		return jobId;
	}

	public void action() {
		logger.info("流程执行开始：" + this.toString());
		LogJob job = jobLogService.getJob(jobId);
		job.setStatus(job.getState().handleStart().getCode());
		job.setStarttime(job.getPlantime());
		jobLogService.updateJob(job);
		countDownLatchService.rebuildLatchForJob(job.getCampId(), job.getJobId());
		Node startNode = workflowService.getStartNodeByCampId(job.getCampId());
		ParamHolder params = new ParamHolder();
		Task startNodeTask = taskRepository.createNodeTask(job.getCampId(), jobId, startNode.getId(),
				startNode.getType(), job.isTest(), params);
		disruptor.fireStartEvent(startNodeTask);
	}

	@Override
	public String toString() {
		return MessageFormat.format("FlowTask[camp_id={0},job_id={1},{2}]", String.valueOf(campId),
				String.valueOf(jobId), ScheduleCons.execType(isTest));
	}
}
