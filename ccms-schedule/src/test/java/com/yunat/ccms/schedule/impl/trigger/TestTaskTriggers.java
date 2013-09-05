package com.yunat.ccms.schedule.impl.trigger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.yunat.ccms.schedule.core.trigger.ComboTrigger;
import com.yunat.ccms.schedule.core.trigger.InstantTrigger;
import com.yunat.ccms.schedule.core.trigger.SimpleTrigger;

public class TestTaskTriggers {

	@Test
	public void testInstantTrigger() {
		InstantTrigger trigger = new InstantTrigger();
		Assert.assertNotNull(trigger.getNextFireTime());
	}

	@Test
	public void testSimpleTrigger1() {
		Date now = new Date();
		SimpleTrigger trigger = new SimpleTrigger("test", now);
		Assert.assertEquals(now, trigger.getNextFireTime());
	}

	@Test
	public void testSimpleTrigger2() {
		DateTime now = new DateTime();
		SimpleTrigger trigger = new SimpleTrigger("test", now.toDate(), 1, TimeUnit.HOURS, 1);
		Assert.assertEquals(now.toDate(), trigger.getNextFireTime());
	}

	@Test
	public void testCronTrigger() {

	}

	@Test
	public void testComboTrigger() {
		ComboTrigger comboTrigger = new ComboTrigger("test");
		DateTime now = new DateTime();
		comboTrigger.addSimpleTrigger(now.toDate());
		comboTrigger.addSimpleTrigger(now.minusHours(1).toDate());
		Assert.assertEquals(now.minusHours(1).toDate(), comboTrigger.getNextFireTime());
	}
}
