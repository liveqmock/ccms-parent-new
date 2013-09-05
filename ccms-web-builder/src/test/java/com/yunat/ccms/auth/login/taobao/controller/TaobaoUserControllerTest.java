package com.yunat.ccms.auth.login.taobao.controller;

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
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.result.StatusResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", 
		"classpath:config/spring/applicationContext.xml" }, 
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)

public class TaobaoUserControllerTest {
	@Autowired
	private TaobaoUserController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void updateTaobaoUserStatus() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/taobao/1");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.param("disabled", "0");
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void paginationTaobaoUserList() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/taobao/list");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.param("page", "1");
		requestBuilder.param("rp", "15");
		requestBuilder.param("query", "");
		requestBuilder.param("sortname", "");
		requestBuilder.param("sortorder", "asc");
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}