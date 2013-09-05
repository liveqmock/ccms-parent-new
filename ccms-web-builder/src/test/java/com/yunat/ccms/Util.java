package com.yunat.ccms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.MvcResult;
import org.springframework.test.web.server.ResultActions;
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.result.StatusResultMatchers;

public class Util {

	public static void request(final MockMvc mockMvc, final DefaultRequestBuilder requestBuilder) {
		requestBuilder.characterEncoding("UTF-8");
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		try {
			final ResultActions perform = mockMvc.perform(requestBuilder);
			final ResultActions expect = perform.andExpect(new StatusResultMatchers().isOk());
			final MvcResult andReturn = expect.andReturn();
			final MockHttpServletResponse response = andReturn.getResponse();
			response.setCharacterEncoding("UTF-8");
			System.out.println("@@@@@@PlanGroupControllerTest.testPlanGroupOfShopString():"//
					+ response.getContentAsString());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static String now() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
}
