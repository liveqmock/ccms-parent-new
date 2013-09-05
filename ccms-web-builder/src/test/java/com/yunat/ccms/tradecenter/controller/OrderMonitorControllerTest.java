package com.yunat.ccms.tradecenter.controller;

import java.text.DecimalFormat;

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
public class OrderMonitorControllerTest {
	
	@Autowired
	private OrderMonitorController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	
	@Test
	public void orderMonitorMonthTest(){
		String dp_id = "100571094";
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ordermonitor/orders/orderMonitorMonth");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.param("dpId", dp_id);
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);

		try {
			ResultActions action = this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
			action.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(action.andReturn().getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void urpayAndCareMonitoringTest(){
		String dp_id = "100571094";
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ordermonitor/orders/urpayAndCareMonitoring");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.param("dpId", dp_id);
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);

		try {
			ResultActions action = this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
			action.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(action.andReturn().getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String agrs[]){
		Integer divisor = 23;
		Integer dividend = 24;
		Double flowRate = 0.0;
		if (dividend > 0) {
			flowRate =  ((double)divisor / dividend);
		}
		flowRate = flowRate * 100;	//计算百分比
		
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(1);
		decimalFormat.setMinimumFractionDigits(0);
		String end = decimalFormat.format(flowRate).toString();
		
		System.out.println(end);
		System.out.println(flowRate);
		System.out.println((divisor/dividend));
	}

}
