package com.yunat.ccms.tradecenter.controller;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;

/**
 * 催付任务配置测试
 *
 * @author teng.zeng
 * date 2013-6-3 上午11:40:44
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml",
		"classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class UrpayConfigControllerTest{

	@Autowired
	private UrpayConfigController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testUrpayConfigTest(){
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/urpay/urpayConfig").param("dp_id", "123").param("urpay_type","1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUrpayConfigPostTest(){
		UrpayConfigDomain urpayConfigDomain = new UrpayConfigDomain();
    	urpayConfigDomain.setCreated(new Date());
    	urpayConfigDomain.setUpdated(new Date());
    	urpayConfigDomain.setUrpayType(2);
    	urpayConfigDomain.setTaskType(1);
    	urpayConfigDomain.setStartDate(new Date());
    	urpayConfigDomain.setEndDate(new Date());
    	urpayConfigDomain.setDateType(0);
    	try {
			urpayConfigDomain.setUrpayStartTime(new SimpleDateFormat("HH:mm:ss").parse("11:00:00"));
			urpayConfigDomain.setUrpayEndTime(new SimpleDateFormat("HH:mm:ss").parse("12:00:00"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		urpayConfigDomain.setFixUrpayTime("1,2");
		urpayConfigDomain.setOffset(15);
		urpayConfigDomain.setNotifyOption(-1);
		urpayConfigDomain.setOrderMaxAcount(-1.0);
		urpayConfigDomain.setOrderMinAcount(-1.0);
		urpayConfigDomain.setIsOpen(0);
		urpayConfigDomain.setIsSwitch(1);
		urpayConfigDomain.setUserName("test");
		urpayConfigDomain.setPlatName("taobao");
		urpayConfigDomain.setOpUser("admin");
		urpayConfigDomain.setDpId("111");
		urpayConfigDomain.setFilterCondition("1,2");
		urpayConfigDomain.setMemberGrade("1");
		urpayConfigDomain.setIncludeCheap(null);
		urpayConfigDomain.setSmsContent("测试内容");
		urpayConfigDomain.setGatewayId(1);
    	urpayConfigDomain.setDateNumber(1);
//    	urpayConfigDomain.setPkid(3L);
    	System.out.println(JackSonMapper.toCJsonString(urpayConfigDomain));

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/urpay/urpayConfig");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body(JackSonMapper.toCJsonString(urpayConfigDomain).getBytes(Charset.forName("UTF-8")));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
