package com.yunat.ccms.amqp;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.yunat.ccms.event.bus.config.client.RabbitClientConfiguration;

public class Consumer {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(RabbitClientConfiguration.class);
		SimpleMessageListenerContainer listenerContainer = context.getBean(SimpleMessageListenerContainer.class);
		listenerContainer.start();
	}

}
