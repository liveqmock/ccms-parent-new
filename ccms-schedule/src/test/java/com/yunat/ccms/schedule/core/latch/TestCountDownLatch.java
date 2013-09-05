package com.yunat.ccms.schedule.core.latch;

import junit.framework.Assert;

import org.junit.Test;

public class TestCountDownLatch {

	@Test
	public void testGetCountDownLatchId() {
		Assert.assertEquals("FLOW_123456", FlowCountDownLatch.toLatchId(123456));
		Assert.assertEquals("FLOW_123456_GATEWAY_789", GatewayCountDownLatch.toLatchId(123456, 789));
		Assert.assertEquals("FLOW_123456_NODE_789", NodeCountDownLatch.toLatchId(123456, 789));
	}
}
