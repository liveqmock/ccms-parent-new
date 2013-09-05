package com.yunat.ccms.schedule.impl.trigger;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluate;
import com.yunat.ccms.node.biz.time.NodeTime;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.trigger.TriggerFactory;

public class TestTriggerFactory {

	@Test
	public void testInstantTrigger() {
		String jobName = "CAMPAIGN_1";
		NodeTime nodeTime = null;
		TaskTrigger trigger = TriggerFactory.createCampTrigger(jobName, nodeTime);
		Assert.assertNotNull(trigger);
	}

	@Test
	public void testNodeEvaluateTrigger() {
		String jobName = "123_456";
		NodeEvaluate nodeEvaluate = new NodeEvaluate();
		nodeEvaluate.setEvaluateCycle(7);
		TaskTrigger trigger = TriggerFactory.createEvaluateNodeTrigger(jobName, nodeEvaluate);
		Assert.assertNotNull(trigger);
		Date date = trigger.getNextFireTime();
		System.out.println(date);
	}
}
