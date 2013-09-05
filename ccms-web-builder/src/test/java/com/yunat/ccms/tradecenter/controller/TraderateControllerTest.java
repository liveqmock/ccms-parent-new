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

import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.controller.vo.TraderateAutoSetRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class TraderateControllerTest {

	@Autowired
	private TraderateController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testQuery() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/traderate/query")
				.body("{\"content\":\"内容\",\"isExplainFlag\":\"true\",\"currPage\":\"1\",\"pageSize\":\"1\"}"
						.getBytes()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");
		try {
			ResultActions action = this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
			action.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(action.andReturn().getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testregardTest() {
		CaringRequest caringRequest = new CaringRequest();
    	caringRequest.setCaringType("27");
    	caringRequest.setContent("我是短信内容hahaha");
    	caringRequest.setCustomerno("用户昵称");
    	caringRequest.setDpId("654321");
    	caringRequest.setGatewayId(1L);
    	caringRequest.setOids(new String[]{"999999"});
    	caringRequest.setTids(new String[]{"123456"});

		System.out.println(JackSonMapper.toCJsonString(caringRequest));

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/traderate/regard");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body(JackSonMapper.toCJsonString(caringRequest).getBytes(Charset.forName("UTF-8")));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testbatchRegardTest() {
		CaringRequest caringRequest = new CaringRequest();
    	caringRequest.setCaringperson("我是客服");
    	caringRequest.setCaringType("28");
    	caringRequest.setContent("我是短信内容");
    	caringRequest.setCustomerno("用户昵称");
    	caringRequest.setDpId("654321");
    	caringRequest.setGatewayId(1L);

		caringRequest.setOids(new String[]{"999999"});
		caringRequest.setTids(new String[]{"123456"});

		System.out.println(JackSonMapper.toCJsonString(caringRequest));

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/batch/regard");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body(JackSonMapper.toCJsonString(caringRequest).getBytes(Charset.forName("UTF-8")));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void autoSetTest(){
		TraderateAutoSetRequest traderateAutoSetRequest = new TraderateAutoSetRequest();
		traderateAutoSetRequest.setDpId("100571094");
		traderateAutoSetRequest.setType("order_success");
		traderateAutoSetRequest.setContent("非常棒的买家，收到宝贝有任何问题请立即与小店售后联系！");
		traderateAutoSetRequest.setStatus(0);

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/traderate/autoset");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body(JackSonMapper.toCJsonString(traderateAutoSetRequest).getBytes(Charset.forName("UTF-8")));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	@Test
	public void getAutoSetTest(){
		String dp_id = "100571094";
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ordermonitor/orders/orderMonitor30");
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



}
