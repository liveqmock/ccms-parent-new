package com.yunat.ccms.amqp;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.ccms.event.bus.config.server.RabbitServerConfiguration;

public class Producer {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(RabbitServerConfiguration.class);
		RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);
		
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