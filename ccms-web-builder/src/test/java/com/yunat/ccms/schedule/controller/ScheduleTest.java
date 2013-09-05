package com.yunat.ccms.schedule.controller;

import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;

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

import com.yunat.ccms.schedule.controler.ScheduleControler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", 
		"classpath:config/spring/applicationContext.xml" },
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class ScheduleTest {

	@Autowired
	private ScheduleControler controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testTestingExecute() {
		Long campaingId = 123l;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/campaign/{campId}/test",
				campaingId);
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			ResultActions actions = mockMvc.perform(requestBuilder);
			actions.andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStopTesting() {
		Long campaingId = 123l;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/campaign/{campId}/stopTest",
				campaingId);
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			ResultActions actions = mockMvc.perform(requestBuilder);
			actions.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExecute() {

		Long campaingId = 123l;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/campaign/{campId}/execute",
				campaingId);
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			ResultActions actions = mockMvc.perform(requestBuilder);
			actions.andExpect(new StatusResultMatchers().isOk());
			actions.andDo(print());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStopExecute() {

		Long campaingId = 123l;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/campaign/{campId}/stop",
				campaingId);
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			ResultActions actions = mockMvc.perform(requestBuilder);
			actions.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
