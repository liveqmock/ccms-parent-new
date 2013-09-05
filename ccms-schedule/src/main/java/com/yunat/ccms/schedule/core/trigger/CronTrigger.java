package com.yunat.ccms.schedule.core.trigger;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Cron表达式触发
 * 
 * @author xiaojing.qu
 * 
 */
public class CronTrigger extends DurableTrigger {

	private static final Logger logger = LoggerFactory.getLogger(CronTrigger.class);

	private static final long serialVersionUID = -9191417087755703649L;

	private final CronTriggerImpl trigger;

	/**
	 * @param jobName
	 * @param cronExpression
	 * @param startTime
	 * @param endTime
	 * @throws ParseException
	 */
	public CronTrigger(String jobName, String cronExpression, Date startTime, Date endTime) throws ParseException {
		logger.info("add Trigger for Job:{},Cron:{},Start:{},End:{}", new Object[] { jobName, cronExpression,
				startTime, endTime });
		trigger = new CronTriggerImpl();
		trigger.setName(jobName);
		trigger.setJobName(jobName);
		trigger.setCronExpression(cronExpression);
		trigger.setStartTime(startTime);
		trigger.setEndTime(endTime);
	}

	@Override
	public String getId() {
		return trigger.getName();
	}

	@Override
	public Date getNextFireTime() {
		return TriggerUtils.computeFireTimes(trigger, null, 1).get(0);
	}

	@Override
	public List<? extends Trigger> getTriggers() {
		return Lists.newArrayList(trigger);
	}

}
