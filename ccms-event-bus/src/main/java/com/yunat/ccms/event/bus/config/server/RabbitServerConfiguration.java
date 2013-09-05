package com.yunat.ccms.event.bus.config.server;

import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.event.bus.config.AbstractRabbitConfiguration;

@Configuration
public class RabbitServerConfiguration extends AbstractRabbitConfiguration {

	private final static String AMQP_EXCHANGE_NAME = "ccms_event_bus_exchange";
	private final static String AMQP_QUEUE_NAME = "ccms_event_bus_queue";
	private final static String AMQP_ROUTING_KEY_NAME = "ccms_event_bus";

	@Bean(name = { "connectionFactory" })
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_HOST));
		connectionFactory.setPort(Integer.parseInt(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_PORT)));
		connectionFactory.setUsername(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_ETL_USERNAME));
		connectionFactory.setPassword(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_ETL_PASSWORD));
		connectionFactory.setVirtualHost(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_ETL_VIRTUALHOST));
		return connectionFactory;
	}

	@Bean(name = {"amqpAdmin"})
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean(name = { "rabbitTemplate" })
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setExchange(AMQP_EXCHANGE_NAME);
		template.setRoutingKey(AMQP_ROUTING_KEY_NAME);
		template.setQueue(AMQP_QUEUE_NAME);
		//template.setMessageConverter(jsonMessageConverter());
		return template;
	}

	@Bean(name = {"amqpQueue"})
	public Queue amqpQueue() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return new Queue(AMQP_QUEUE_NAME,true, false, false,map);
	}

	@Bean(name = {"amqpExchange"})
	public Exchange amqpExchange() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return new DirectExchange(AMQP_EXCHANGE_NAME,true, false,map);
	}

	@Bean(name = {"amqpBinding"})
	public Binding amqpBinding() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return BindingBuilder.bind(amqpQueue()).to(amqpExchange()).with(AMQP_ROUTING_KEY_NAME).and(map);
	}

}