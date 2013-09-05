package com.yunat.ccms.tradecenter.repository;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;

public class LogisticsRepositoryTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	private MQlogisticsRepository logisticsRepository;

	@Test
	public void testGetStepInfo() {

		Map<String, Object> aMap = logisticsRepository.queryStansitStepInfo("368088803585230");
		System.out.println(aMap.get("transit_step_info"));
	}
}
