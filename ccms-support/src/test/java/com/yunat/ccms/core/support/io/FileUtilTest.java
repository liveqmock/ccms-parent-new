package com.yunat.ccms.core.support.io;

import junit.framework.Assert;

import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testConcat() {
		Assert.assertEquals(FileUtils.concat("C:/upload", "drools", "plan_6_123456.drl"),
				"C:\\upload\\drools\\plan_6_123456.drl");

		// will fail on win
		// Assert.assertEquals(FileUtils.concat("/data/upload", "drools",
		// "plan_6_123456.drl"),"/data/upload/drools/plan_6_123456.drl");

	}
}
