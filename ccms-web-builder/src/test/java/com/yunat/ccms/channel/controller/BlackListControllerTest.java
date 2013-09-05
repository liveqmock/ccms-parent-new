package com.yunat.ccms.channel.controller;

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

import com.yunat.ccms.channel.support.cons.EnumBlackList;
import com.yunat.ccms.channel.support.controller.BlackListController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class BlackListControllerTest {

	@Autowired
	private BlackListController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testLoadMobile() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/blacklist/{type}",
				EnumBlackList.MOBILE.name()).characterEncoding("UTF-8");
		requestBuilder.param("value", "13817466666", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPageMobile() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/blacklist/{qtype}/page",
				EnumBlackList.MOBILE.name()).characterEncoding("UTF-8");

		requestBuilder.param("page", "1", new String[] {});
		requestBuilder.param("rp", "15", new String[] {});
		requestBuilder.param("query", "138174", new String[] {});
		requestBuilder.param("sortname", "contact", new String[] {});
		requestBuilder.param("sortorder", "asc", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateMobile() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/blacklist/{type}",
				EnumBlackList.MOBILE.name()).characterEncoding("UTF-8");

		requestBuilder.param("values", "[\"13817499991\",\"13817499992\"]", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteMobile() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/channel/blacklist/{type}",
				EnumBlackList.MOBILE.name()).characterEncoding("UTF-8");

		requestBuilder.param("values", "[\"13817499991\",\"13817499992\"]", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoadEmail() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/blacklist/{type}",
				EnumBlackList.EMAIL.name(), "kevin@yunat.net").characterEncoding("UTF-8");
		requestBuilder.param("value", "kevin.jiang@yunat.net", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPageEmail() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/blacklist/{qtype}/page",
				EnumBlackList.EMAIL.name()).characterEncoding("UTF-8");

		requestBuilder.param("page", "1", new String[] {});
		requestBuilder.param("rp", "15", new String[] {});
		requestBuilder.param("query", "kevin.jiang", new String[] {});
		requestBuilder.param("sortname", "contact", new String[] {});
		requestBuilder.param("sortorder", "asc", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateEmail() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/blacklist/{type}",
				EnumBlackList.EMAIL.name()).characterEncoding("UTF-8");

		requestBuilder.param("values", "[\"jiang@yunat.net\",\"kevin@yunat.net\"]", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteEmail() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/channel/blacklist/{type}",
				EnumBlackList.EMAIL.name()).characterEncoding("UTF-8");

		requestBuilder.param("values", "[\"jiang@yunat.net\",\"kevin@yunat.net\"]", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpload() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/blacklist/{type}/upload",
				EnumBlackList.MOBILE.name()).characterEncoding("UTF-8");
		requestBuilder.param("filePath", "xxx", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDownload() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/blacklist/{type}/download",
				EnumBlackList.MOBILE.name()).characterEncoding("UTF-8");
		requestBuilder.param("filePath", "xxx", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions ras = this.mockMvc.perform(requestBuilder);
			ras.andReturn().getResponse().setCharacterEncoding("UTF-8");
			System.out.println(ras.andReturn().getResponse().getContentAsString());
			ras.andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
