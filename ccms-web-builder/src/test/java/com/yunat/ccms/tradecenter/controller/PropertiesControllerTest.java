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
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.result.StatusResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.tradecenter.controller.vo.PropertiesRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class PropertiesControllerTest {
	
	@Autowired
	private TraderateController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void getTest() {
		PropertiesRequest propertiesRequest = new PropertiesRequest();
		propertiesRequest.setDpId("100571094");
		propertiesRequest.setName("nearly30DaysOrderFlowMonitoring");
		
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/properties/get");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body(JackSonMapper.toCJsonString(propertiesRequest).getBytes(Charset.forName("UTF-8")));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
