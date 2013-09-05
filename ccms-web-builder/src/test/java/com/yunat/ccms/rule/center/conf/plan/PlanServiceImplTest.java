package com.yunat.ccms.rule.center.conf.plan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/applicationContext.xml"}, 
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class PlanServiceImplTest {
	private static final Long planId = 7L;
	@Autowired
	private PlanService planService;
	
	@Test
	public void testTurnOn() {
		System.out.println("return " + planService.turnOn(planId));
	}

	@Test
	public void testTurnOff() {
		System.out.println("return " + planService.turnOff(planId));
	}

	@Test
	public void testToggleOnOff() {
		System.out.println("return " + planService.toggleOnOff(planId));
	}

}