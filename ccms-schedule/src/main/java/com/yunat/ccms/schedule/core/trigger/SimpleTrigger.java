package com.yunat.ccms.schedule.core.trigger;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import com.google.common.collect.Lists;

/**
 * 指定时间点触发(可以指定)
 * 
 * @author xiaojing.qu
 * 
 */
public class SimpleTrigger extends DurableTrigger {

	private static final long serialVersionUID = -7999159794713451194L;
	private final SimpleTriggerImpl trigger;

	/**
	 * 指定在某个时间执行一次
	 * 
	 * @param jobName
	 *            trigger名
	 * @param planTime
	 *            计划执行时间
	 */
	public SimpleTrigger(String jobName, Date planTime) {
		trigger = new SimpleTriggerImpl();
		trigger.setName(jobName);
		trigger.setJobName(jobName);
		trigger.setStartTime(planTime);
		trigger.setRepeatCount(0);// 不需要重复
	}

	/**
	 * 指定在某个时间执行，执行多次（在第一次执行后，指定时间间隔后执行）
	 * 
	 * @param name
	 *            trigger名
	 * @param planTime
	 *            计划首次执行时间
	 * @param repeatCount
	 *            首次执行后重复的次数
	 * @param timeUnit
	 *            时间间隔的单位
	 * @param timeUintValue
	 *            时间间隔的数值
	 */
	public SimpleTrigger(String name, Date planTime, int repeatCount, TimeUnit timeUnit, int timeUintValue) {
		trigger = new SimpleTriggerImpl();
		trigger.setName(name);
		trigger.setJobName(name);
		trigger.setStartTime(planTime);
		trigger.setRepeatCount(repeatCount);
		trigger.setRepeatInterval(TimeUnit.MILLISECONDS.convert(timeUintValue, timeUnit));
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
