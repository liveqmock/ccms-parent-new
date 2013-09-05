package com.yunat.ccms.rule.center.conf.condition;

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
@ContextConfiguration(locations = {//
"classpath:config/spring/spring-servlet.xml",//
		"classpath:config/spring/applicationContext.xml",//
		"classpath:config/security/applicationContext-security.xml" },//
loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class ProductSearchControllerTest {
	
	@Autowired
	private ProductSearchController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void testSearchProducts() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rulecenter/shop/100571094/products");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.param("q", "雪花");
		requestBuilder.param("pageNo", "1");
		requestBuilder.param("pageSize", "15");
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
	public void testShowProducts() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rulecenter/shop/100571094/products/20808156866");
		requestBuilder.characterEncoding("UTF-8");
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
