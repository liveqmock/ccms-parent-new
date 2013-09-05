package com.yunat.ccms.metadata.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.node.biz.query.QueryNodeConfigService;
import com.yunat.ccms.node.biz.query.QueryNodeExecuteService;

public class QueryNodeExecuteServiceTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	QueryNodeConfigService queryNodeConfigService;

	@Autowired
	MetaQueryConfigService metaQueryConfigService;

	@Autowired
	QueryNodeExecuteService queryNodeExecuteService;

	@Test
	public void testExecuteCustomer() {

		String output = queryNodeExecuteService.executePropertiesQuery(1002L, 100L, 2L, "uni_customer", null);
		System.out.println(output);
	}

	@Test
	public void testExecuteConsume() {

		String output = queryNodeExecuteService.executeConsumeQuery(0L, 100L, 10000L, "uni_customer", null);
		System.out.println(output);
	}
}
