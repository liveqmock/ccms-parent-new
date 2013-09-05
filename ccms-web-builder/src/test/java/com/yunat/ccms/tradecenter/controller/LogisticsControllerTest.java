package com.yunat.ccms.tradecenter.controller;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.ResultActions;
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.result.StatusResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class LogisticsControllerTest {

	@Autowired
	private LogisticsController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testShowLogisticsInfo() {

		// 读取
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/customerCenter/logistics/showLogisticsInfo").characterEncoding("UTF-8");
		requestBuilder.param("tid", "368088803585230", new String[] {});
		requestBuilder.accept(new MediaType("application", "json", Charset.forName("utf-8")));
		String dbJson = "{}";
		try {

			ResultActions ra = this.mockMvc.perform(requestBuilder);
			dbJson = ra.andReturn().getResponse().getContentAsString();
			System.out.println(dbJson);
			ra.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
