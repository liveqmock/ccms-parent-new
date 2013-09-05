package com.yunat.ccms.schedule.core.trigger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多个不同的trigger组成,基本上能覆盖所有的触发类型
 * 
 * @author xiaojing.qu
 * 
 */
public class ComboTrigger extends DurableTrigger {

	private static final Logger logger = LoggerFactory.getLogger(ComboTrigger.class);

	/***  */
	private static final long serialVersionUID = -3521752090546511371L;

	private final String jobName;
	private final AtomicInteger order;
	private final List<Trigger> triggers;

	public ComboTrigger(String jobName) {
		this.jobName = jobName;
		order = new AtomicInteger(1);
		triggers = new ArrayList<Trigger>();
	}

	public void addSimpleTrigger(Date scheduledTime) {
		logger.info("Add trigger to Job:{},StartTime:{},RepeatCount:{}", new Object[] { jobName, scheduledTime, 0 });
		String triggerName = jobName + "_" + order.getAndIncrement();
		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setStartTime(scheduledTime);
		trigger.setName(triggerName);
		trigger.setJobName(jobName);
		trigger.setRepeatCount(0);
		triggers.add(trigger);
	}

	public void addSimpleTrigger(Date scheduledTime, int repeatCount, TimeUnit timeUnit, int timeUintCount) {
		logger.info("Add trigger to Job:{},StartTime:{},RepeatCount:{}", new Object[] { jobName, scheduledTime,
				repeatCount });
		String triggerName = jobName + "_" + order.getAndIncrement();
		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setStartTime(scheduledTime);
		trigger.setName(triggerName);
		trigger.setJobName(jobName);
		trigger.setRepeatCount(repeatCount);
		trigger.setRepeatInterval(TimeUnit.MILLISECONDS.convert(timeUintCount, timeUnit));
		triggers.add(trigger);
	}

	public void addCronTrigger(String cronExpression, Date startTime, Date endTime) throws ParseException {
		logger.info("Add trigger to Job:{},cron:{},startTime:{},endTime:{}",
				new Object[] { jobName, startTime, endTime });
		int index = triggers.size();
		String triggerName = jobName + "_" + (index + 1);// 应该是同一个线程访问，就不加同步了
		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setName(triggerName);
		trigger.setJobName(jobName);
		trigger.setCronExpression(cronExpression);
		trigger.setStartTime(startTime);
		trigger.setEndTime(endTime);
		triggers.add(trigger);
	}

	@Override
	public String getId() {
		return jobName;
	}

	@Override
	public Date getNextFireTime() {
		Date d = null;
		for (Trigger t : triggers) {
			Date d1 = TriggerUtils.computeFireTimes((OperableTrigger) t, null, 1).get(0);
			if (d == null || d1.before(d)) {
				d = d1;
			}
		}
		return d;
	}

	@Override
	public List<? extends Trigger> getTriggers() {
		return triggers;
	}

}
