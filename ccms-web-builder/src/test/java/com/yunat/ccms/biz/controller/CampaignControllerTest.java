package com.yunat.ccms.biz.controller;

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

import com.yunat.ccms.biz.controller.CampaignController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml",
		"classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class CampaignControllerTest {
	@Autowired
	private CampaignController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testAddCampaign() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/campaign");
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body("{ \"campName\": \"123\" , \"platCode\" : \"taobao\" }".getBytes(Charset.forName("UTF-8")));
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
	public void testEditCampaign() {
		Long idPath = 1L;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/campaign/{id}", idPath);
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.body("{ \"campName\" : \"234\" }".getBytes(Charset.forName("UTF-8")));
		requestBuilder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testShowCampaign() {
		Long idPath = 1L;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/campaign/{id}", idPath)
				.contentType(MediaType.TEXT_PLAIN).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteCampaign() {
		Long idPath = 1L;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/campaign/{id}", idPath)
				.contentType(MediaType.TEXT_PLAIN).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testBatchDeleteCampaign() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/campaign").param("ids", "1,2")
				.contentType(MediaType.TEXT_PLAIN).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCampaignList() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/campaign")
				.body("{\"page\" : 1, \"qtype\" : \"\", \"query\" : \"\", \"rp\" : 10, \"show_activity\" : \"false\", \"sortname\" : \"\", \"sortorder\" : \"\", \"platCode\" : \"taobao\", \"version\" : \"BASIC\"}"
						.getBytes(Charset.forName("UTF-8"))).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testOpenWorkflow() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/campaign/workflow/snapshot")
				.param("campId", "1").contentType(MediaType.TEXT_PLAIN).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveWorkflow() {
		Long idPath = 1L;
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/campaign/workflow/snapshot/{id}", idPath)
				.body("{\"nodes\":[{\"id\":9,\"type\":\"tflowstart\",\"value\":\"开始\",\"style\":\"tflowstart;image=../3rd/mxGraph/images/icon/lc_begin.png\",\"x\":100,\"y\":200,\"vertex\":\"1\",\"width\":52,\"height\":52,\"asT\":\"geometry\",\"fill\":false},{\"id\":10,\"type\":\"tflowtime\",\"value\":\"时间\",\"style\":\"tflowtime;;image=../3rd/mxGraph/images/icon/lc_time.png\",\"x\":200,\"y\":200,\"vertex\":\"1\",\"width\":52,\"height\":52,\"asT\":\"geometry\",\"fill\":false}],\"connects\":[{\"id\":2,\"source\":9,\"target\":10,\"edge\":\"1\",\"relative\":\"1\",\"asT\":\"geometry\"}]}"
						.getBytes(Charset.forName("UTF-8"))).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void checkCampaign() {
		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/campaign/check")
				.param("campaignName", "123456").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");
		try {
			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
