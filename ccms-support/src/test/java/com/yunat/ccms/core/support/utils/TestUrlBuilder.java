package com.yunat.ccms.core.support.utils;

import junit.framework.Assert;

import org.junit.Test;

public class TestUrlBuilder {

	@Test
	public void test1() {
		UrlBuilder builder = new UrlBuilder("http://testserver/").append("/path/").addParam("key", "value");
		Assert.assertEquals("http://testserver/path/?key=value", builder.build());
	}

	@Test
	public void test2() {
		UrlBuilder builder = new UrlBuilder("http://testserver/").append("/path?").addParam("key", "value");
		Assert.assertEquals("http://testserver/path?key=value", builder.build());
	}

}
