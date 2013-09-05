package com.yunat.ccms.dashboard.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindingResult;

/**
 * ©Copyright：yunat Project：CCMS Module ID：首页测试 Comments：渠道控制类测试 JDK version
 * used：<JDK1.6> Author：yinwei Create Date： 2013-01-27 Version：1.0 Modified By：
 * Modified Date： Why & What is modified： Version：
 */
public class ChannelControllerTest {

	@Mock
	private BindingResult mockBindingResult;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(mockBindingResult.hasErrors()).thenReturn(true);
	}

	@Test
	public void open() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		DashBoardChannelController channelController = new DashBoardChannelController();
	}
}
