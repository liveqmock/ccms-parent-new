package com.yunat.ccms.tradecenter.urpay.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/security/applicationContext-security.xml", "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class AutoResponseTraderateTaskTest {
	
	@Test
	public void handleTest(){

		AutoResponseTraderateTask autoResponseTraderateTask = new AutoResponseTraderateTask();
		autoResponseTraderateTask.handle(null);
		
	}

}
