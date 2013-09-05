package com.yunat.ccms.schedule.core.quartz;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.service.CheckpointService;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.service.WorkflowService;
import com.yunat.ccms.schedule.support.NextActionEnum;
import com.yunat.ccms.schedule.support.ParamHolder;
import com.yunat.ccms.schedule.support.ScheduleCons;
import com.yunat.ccms.workflow.domain.Node;

/**
 * 相对于4.0以前版本，TimerJob的功能被弱化， 只做触发活动或节点即刻执行使用,
 * PS：这里有个隐含的条件，就是通过TimerJob触发的Task都是正式执行的Task，因为预执行都是立刻执行而不会设置到某个时间再触发
 * 
 * @author xiaojing.qu
 * 
 */
public class TimerJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(TimerJob.class);

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private JobLogService joblogService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private CheckpointService statusService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Date scheduleTime = context.getScheduledFireTime();
		Date fireTime = context.getFireTime();
		Trigger trigger = context.getTrigger();
		String jobName = trigger.getJobKey().getName();
		String triggerName = trigger.getKey().getName();
		boolean triggerMayFireAgain = trigger.mayFireAgain();
		logger.info("job:[{}]triggerd by:[{}]fired,plantime:{},actualTime:{},triggerMayFireAgain:{}", new Object[] {
				jobName, triggerName, scheduleTime, fireTime, triggerMayFireAgain });
		try {
			List<? extends Trigger> triggers = context.getScheduler().getTriggersOfJob(trigger.getJobKey());
			for (Trigger t : triggers) {
				if (!t.getKey().equals(trigger.getKey()) && t.mayFireAgain()) {
					triggerMayFireAgain = true;
					logger.info("other trigger:[{}] will fire again!", t.getKey().getName());
					break;
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		ParamHolder extra = new ParamHolder();
		extra.put(ScheduleCons.KEY_TRIGGER_FIRE_BY_QUARTZ, true);
		extra.put(ScheduleCons.KEY_TRIGGER_SCHEDULE_TIME, scheduleTime);
		extra.put(ScheduleCons.KEY_TRIGGER_FIRE_AGAIG, triggerMayFireAgain);
		if (triggerName.startsWith(ScheduleCons.PREFIX_CAMPAIGN)) {
			Long campId = JobNamingUtil.getCampId(jobName);
			NextActionEnum nextAction = statusService.checkCampState(campId, false);
			if (nextAction == NextActionEnum.RETURN) {
				deleteStopedCampJobs(context, campId);
			}
			LogJob logjob = new LogJob(campId, false, scheduleTime, !triggerMayFireAgain);
			logjob = joblogService.saveJob(logjob);
			FlowTask flowTask = taskRepository.createFlowTask(campId, logjob.getJobId(), logjob.isTest(), extra);
			disruptor.fireStartEvent(flowTask);
		} else {
			Long jobId = JobNamingUtil.getJobId(jobName);
			Long nodeId = JobNamingUtil.getNodeId(jobName);
			LogSubjob subjob = joblogService.getSubjob(jobId, nodeId);
			NextActionEnum nextAction1 = statusService.checkCampState(subjob.getCampId(), false);
			NextActionEnum nextAction2 = statusService.checkJobState(jobId, false);
			if (!(nextAction1 == NextActionEnum.CONTINUE && nextAction2 == NextActionEnum.CONTINUE)) {
				deleteStopedNodeJobs(context, jobId, nodeId);
			}
			Node node = workflowService.getNodeById(nodeId);
			NodeTask nodeTask = taskRepository.createNodeTask(subjob.getCampId(), jobId, node.getId(), node.getType(),
					false, extra);
			disruptor.fireStartEvent(nodeTask);
		}

	}

	private void deleteStopedCampJobs(JobExecutionContext context, Long campId) {
		try {
			logger.info("delete Job:Camp[{}]", new Object[] { campId });
			context.getScheduler().deleteJob(new JobKey(JobNamingUtil.getCampJobName(campId)));

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	private void deleteStopedNodeJobs(JobExecutionContext context, Long jobId, Long nodeId) {
		try {
			logger.info("delete Job:Job:[{}],Node[{}]", new Object[] { jobId, nodeId });
			context.getScheduler().deleteJob(new JobKey(JobNamingUtil.getNodeJobName(jobId, nodeId)));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}