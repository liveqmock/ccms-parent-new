package com.yunat.ccms.metadata.test.controller;

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

import com.yunat.ccms.metadata.controller.MetaDataRetrieveController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class MetaDataRetrieveControllerTest {

	@Autowired
	private MetaDataRetrieveController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void receiveCatalog() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/catalog").characterEncoding("UTF-8");
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveAttribute() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/catalog/{id}/attribute", 1L)
				.characterEncoding("UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveValueType() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/value-type")
				.characterEncoding("UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveOperator() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/operator").characterEncoding("UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveRelation() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/relation").characterEncoding("UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveDic() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/dic").characterEncoding("UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveAttrTypeMapOperator() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/attribute-type/{id}/operator",
				"STRING").characterEncoding("UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveOperatorValue() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/operator/{id}/value-type", "EQ")
				.characterEncoding("UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveDicValuesByDic() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/dic/{id}/value", 1).characterEncoding(
				"UTF-8");
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			this.mockMvc.perform(requestBuilder).andExpect(new StatusResultMatchers().isOk());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receiveDics() {

		DefaultRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/meta/dics").characterEncoding("UTF-8");
		requestBuilder.param("ids", "[57, 63]", new String[] {});
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {

			ResultActions actions = this.mockMvc.perform(requestBuilder);
			System.out.println(actions.andReturn().getResponse().getContentAsString());
			actions.andExpect(new StatusResultMatchers().isOk());
			;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
