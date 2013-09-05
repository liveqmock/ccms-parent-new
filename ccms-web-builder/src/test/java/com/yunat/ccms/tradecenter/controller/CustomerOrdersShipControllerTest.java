package com.yunat.ccms.tradecenter.controller;

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

/**
 * 催付任务配置测试
 *
 * @author teng.zeng date 2013-6-3 上午11:40:44
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml",
		"classpath:config/spring/applicationContext.xml"
		},
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class CustomerOrdersShipControllerTest {

	@Autowired
	private CustomerOrdersController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testUrPayOrdersLogList() {
		DefaultRequestBuilder req = MockMvcRequestBuilders.post("/customerCenter/orders/urPayOrdersLogList")
				.param("currPage", "1").param("pageSize", "10").param("dpId", "a")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			ResultActions actions = this.mockMvc.perform(req).andExpect(new StatusResultMatchers().isOk());
			System.out.println(actions.andReturn().getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCustomerOrdersShip() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customerCenter/orders/customerOrdersShip");
		requestBuilder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");
		try {
			ResultActions action = this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
			System.out.println(action.andReturn().getResponse().getContentAsString());
			System.out.println(action.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
