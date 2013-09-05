package com.yunat.ccms.schedule.impl.trigger;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

public class TestQuartzTriggers {

	@Test
	public void testSimpleTrigger1() {
		String name = "t";
		DateTime now = new DateTime();
		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setStartTime(now.toDate());
		trigger.setName(name);
		trigger.setJobName(name);
		trigger.setRepeatCount(0);
		List<Date> dates = TriggerUtils.computeFireTimesBetween(trigger, null, now.toDate(), new Date(Long.MAX_VALUE));
		Assert.assertEquals(1, dates.size());
		Assert.assertEquals(now.toDate(), dates.get(0));

	}

	@Test
	public void testSimpleTrigger2() {
		String name = "t";
		DateTime now = new DateTime();
		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setStartTime(now.toDate());
		trigger.setName(name);
		trigger.setJobName(name);
		trigger.setRepeatCount(1);
		trigger.setRepeatInterval(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS));
		List<Date> dates = TriggerUtils.computeFireTimes(trigger, null, 2);
		Assert.assertEquals(2, dates.size());
		Assert.assertEquals(now.toDate(), dates.get(0));
		Assert.assertEquals(now.plusHours(1).toDate(), dates.get(1));
	}

	@Test
	public void testCronTrigger1() throws Exception {
		String name = "t";
		String cron = " 0 0 10,14,16 * * ? ";
		DateTime now = new DateTime();
		Date endTime = new Date(Long.MAX_VALUE);
		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression(cron);
		trigger.setStartTime(now.toDate());
		trigger.setEndTime(endTime);
	}
}
