package com.yunat.ccms.biz.controller;

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

import com.yunat.ccms.biz.controller.TemplateController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", 
		"classpath:config/spring/applicationContext.xml" }, 
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class TemplateControllerTest {

	@Autowired
	private TemplateController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testFindTemplate() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/template/option")
				.param("platCode", "taobao").param("version", "123")
				.contentType(MediaType.TEXT_PLAIN).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}