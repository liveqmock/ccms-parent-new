package com.yunat.ccms.auth.controller;

import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

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

import com.yunat.ccms.auth.user.controller.UserManageController;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml",
		"classpath:config/spring/applicationContext.xml"
		}, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)

public class UserManageControllerTest {

	@Autowired
	UserManageController userManageController;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
	    mockMvc = MockMvcBuilders.standaloneSetup(userManageController).build();
	}
	
	@Test
	public void testSaveUser() throws Exception{
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user-manager/user").characterEncoding("utf-8");
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
}
