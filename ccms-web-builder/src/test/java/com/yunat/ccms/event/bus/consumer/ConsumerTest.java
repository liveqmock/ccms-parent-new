package com.yunat.ccms.event.bus.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class ConsumerTest {
	@Autowired
	@Qualifier(value = "orderMessageListenerContainer")
	private SimpleMessageListenerContainer listenerContainer;
	
	@Test
	public void testConsumerRun() {
		listenerContainer.start();
	}

}
