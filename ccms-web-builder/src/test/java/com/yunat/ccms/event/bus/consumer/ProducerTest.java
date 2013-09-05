package com.yunat.ccms.event.bus.consumer;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/applicationContext.xml" }, 
	loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class ProducerTest {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	public void runProducer() {
		List<Map<String, Object>> orders = Lists.newArrayList();
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("username", "smallye");
		map1.put("tid", "129304433");
		orders.add(map1);
		
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("username", "smallye");
		map2.put("tid", "129304433");
		orders.add(map2);
		
		rabbitTemplate.convertAndSend(orders);
		
	}
}
