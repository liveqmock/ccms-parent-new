package com.yunat.ccms.tradecenter.controller;

import java.nio.charset.Charset;
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
import org.springframework.test.web.server.ResultActions;
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.result.StatusResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;

/**
 * 关怀任务配置测试
 *
 * @author teng.zeng
 * date 2013-6-14 下午03:54:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml",
		"classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class CareConfigControllerTest {

	@Autowired
	private CareConfigController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testCareConfigTest(){
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/care/careConfig").param("dp_id", "123").param("care_type","4")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			ResultActions action = this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
			action.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(action.andReturn().getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCareConfigPostTest(){
		CareConfigDomain careConfigDomain = new CareConfigDomain();
		careConfigDomain.setCareType(4);
		careConfigDomain.setDateType(0);
		careConfigDomain.setDateNumber(30);
		careConfigDomain.setCareStartTime(new Date());
		careConfigDomain.setCareEndTime(new Date());
		careConfigDomain.setCareStatus(1);
		careConfigDomain.setNotifyOption(1);
		careConfigDomain.setFilterCondition("1,2");
		careConfigDomain.setMemberGrade("1,2");
		careConfigDomain.setOrderMaxAcount(2.0);
		careConfigDomain.setOrderMinAcount(1.0);
		careConfigDomain.setSmsContent("test");
		careConfigDomain.setGatewayId(1);
		careConfigDomain.setIsOpen(1);
		careConfigDomain.setDpId("1234568");
//		careConfigDomain.setPkid(4L);
		System.out.println(JackSonMapper.toCJsonString(careConfigDomain));

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/care/careConfig");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body(JackSonMapper.toCJsonString(careConfigDomain).getBytes(Charset.forName("UTF-8")));
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
