package com.yunat.ccms.schedule.core.quartz;

import java.text.ParseException;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yunat.ccms.schedule.core.trigger.DurableTrigger;

@Service
public class QuartzService {

	private static Logger logger = LoggerFactory.getLogger(QuartzService.class);

	@Autowired
	@Qualifier("timer")
	private org.quartz.Scheduler timer;

	/**
	 * 将 DurableTrigger保存到Quartz JobStore中
	 * 
	 * @param timer
	 * @param taskTrigger
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public void scheduleTimer(DurableTrigger taskTrigger) throws SchedulerException {
		JobDetailImpl quartzJob = new JobDetailImpl();
		quartzJob.setName(taskTrigger.getId());
		quartzJob.setJobClass(TimerJob.class);
		quartzJob.setRequestsRecovery(true);
		quartzJob.setDurability(true);
		timer.addJob(quartzJob, true);
		logger.info("Quartz {}:add Job:{} ", timer.getSchedulerName(), taskTrigger.getId());
		for (Trigger quartzTrigger : taskTrigger.getTriggers()) {
			timer.scheduleJob(quartzTrigger);
			logger.info("Quartz {}:add Trigger:{} to Job:{}",
					new Object[] { timer.getSchedulerName(), taskTrigger.getId(), quartzTrigger.getKey().getName() });
		}
	}

	/**
	 * 将 DurableTrigger从Quartz JobStore移除
	 * 
	 * @param timer
	 * @param taskTrigger
	 * @throws SchedulerException
	 * 
	 */
	public void cancelTimer(DurableTrigger taskTrigger) throws SchedulerException {
		JobKey jobKey = new JobKey(taskTrigger.getId());
		boolean foundAndDeleted = timer.deleteJob(jobKey);
		logger.info("Quartz {}:delete Job:{} and Related Triggers", new Object[] { timer.getSchedulerName(),
				taskTrigger.getId(), foundAndDeleted });
	}

	public void cancleTimer(String quartzJobId) throws SchedulerException {
		JobKey jobKey = new JobKey(quartzJobId);
		boolean foundAndDeleted = timer.deleteJob(jobKey);
		logger.info("Quartz {}:delete Job:{} and Related Triggers,{}", new Object[] { timer.getSchedulerName(),
				quartzJobId, foundAndDeleted });
	}

}
