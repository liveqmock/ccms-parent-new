package com.yunat.ccms.node.impl.controller;



import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.yunat.ccms.node.biz.time.TimeNodeController;


/**
* ©Copyright：yunat                        
* Project：CCMS                                         
* Module ID：基础节点控制层测试   
* Comments：时间节点测试                                          
* JDK version used：<JDK1.6>                             
* Author：yinwei                
* Create Date： 2013-03-21
* Version：1.0
* Modified By：                                          
* Modified Date：                                    
* Why & What is modified：     
* Version：            
*/ 
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml",
		"classpath:config/spring/applicationContext.xml"
		}, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class TimeNodeControllerTest{
   
	@Autowired
	TimeNodeController timeNodeController;
	
	private MockMvc mockMvc;
	  
	  
	@Before
	public void setUp() throws Exception {
	    mockMvc = MockMvcBuilders.standaloneSetup(timeNodeController).build();
	}
	  
	  
	@Test
	public void testOpen() throws Exception {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/timeNodeController/timeNode?name=time&id=1001").characterEncoding("utf-8");
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	  
	  
	  
	  
	@Test  
    public void testSave() throws Exception { 
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/timeNodeController/timeNode").characterEncoding("utf-8")
				.contentType(MediaType.APPLICATION_JSON).body(new String("{\"id\":100002,\"iscycle\":1,\"isrealtime\":0}").getBytes());
		 this.mockMvc.perform(requestBuilder)
		 .andExpect(status().isOk());
		/*.andExpect(jsonPath("$.composers[0].id"),equalTo(100L));
		System.out.println(actions.andReturn().getResponse().getContentAsString());
		System.out.println(actions.andReturn().getResponse().getStatus());*/
	}
	
	
}

