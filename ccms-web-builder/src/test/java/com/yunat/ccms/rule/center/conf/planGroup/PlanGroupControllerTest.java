package com.yunat.ccms.rule.center.conf.planGroup;

import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.yunat.ccms.Util;
import com.yunat.ccms.rule.center.conf.planGroup.PlanGroupController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/spring/applicationContext.xml",
		"classpath:config/security/applicationContext-security.xml" },
loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class PlanGroupControllerTest {
	@Autowired
	private PlanGroupController controller;

	private MockMvc mock;

	@Before
	public void setUp() throws Exception {
		mock = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlanGroupOfShopString() {
		Util.request(mock, MockMvcRequestBuilders.get("/planGroup/100571094"));
	}

	@Test
	public void testPlanGroupOfShop() {
		Util.request(mock, MockMvcRequestBuilders.get("/planGroup"));
	}

	@Test
	public void testSaveSign() {
		final String content = "{ \"sign\": \"【我是备注签名！】" + Util.now() + "\" }";
		final DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/planGroup/100571094");
		requestBuilder.body(content.getBytes(Charset.forName("UTF-8")));
		Util.request(mock, requestBuilder);
	}
}
