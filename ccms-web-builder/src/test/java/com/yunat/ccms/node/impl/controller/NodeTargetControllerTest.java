package com.yunat.ccms.node.impl.controller;

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
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.result.StatusResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.yunat.ccms.node.biz.target.NodeTargetController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", 
		"classpath:config/spring/applicationContext.xml" }, 
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class NodeTargetControllerTest {

	@Autowired
	private NodeTargetController controller;
	
	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testOpen() {
		Long idPath = 1L;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/node/target/{id}", idPath);
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.param("name", "目标组");
		requestBuilder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSave() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/node/target");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body("{ \"nodeId\": 1 , \"name\" : \"目标组节点\", \"controlGroupType\" : 1, \"controlGroupValue\" : 10 }".getBytes(Charset.forName("UTF-8")));
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
