package com.yunat.ccms.event.bus.config.client;

import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.event.bus.config.AbstractRabbitConfiguration;
import com.yunat.ccms.event.bus.handler.SimpleMessageListener;

@Configuration
public class RabbitClientConfiguration extends AbstractRabbitConfiguration {

	private static final String ETL_TAOBAO_RECEIPT_EXCHANGE = "etl.taobao_receipt_exchange";
	private static final String ETL_TAOBAO_SHIPPING_EXCHANGE = "etl.taobao_shipping_exchange";

	@Bean(name = { "etlConnectionFactory" })
	public ConnectionFactory etlConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_HOST));
		connectionFactory.setPort(Integer.parseInt(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_PORT)));
		connectionFactory.setUsername(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_ETL_USERNAME));
		connectionFactory.setPassword(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_ETL_PASSWORD));
		connectionFactory.setVirtualHost(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_ETL_VIRTUALHOST));
		return connectionFactory;
	}

	@Bean(name = { "etlAdmin" })
	public AmqpAdmin etlAdmin() {
		return new RabbitAdmin(etlConnectionFactory());
	}

	@Bean(name = { "etlQueue" })
	public Queue etlQueue() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return new Queue(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.MQ_ETL_QUEUE),true, false, false,map);
	}

	@Bean(name = { "receiptExchange" })
	public Exchange receiptExchange() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return new DirectExchange(ETL_TAOBAO_RECEIPT_EXCHANGE,true, false,map);
	}

	@Bean(name = { "receiptBinding" })
	public Binding receiptBinding() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return BindingBuilder.bind(etlQueue()).to(receiptExchange())
				.with(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID)).and(map);
	}

	@Bean(name = { "shippingExchange" })
	public Exchange shippingExchange() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return new DirectExchange(ETL_TAOBAO_SHIPPING_EXCHANGE,true, false,map);
	}

	@Bean(name = { "shippingBinding" })
	public Binding shippingBinding() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("x-ha-policy", "all");
		return BindingBuilder.bind(etlQueue()).to(shippingExchange())
				.with(propertiesQuery().retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID)).and(map);
	}

	// -------- order listener config ---------------
	@Bean(name = { "orderMessageListenerContainer" })
	public SimpleMessageListenerContainer orderMessageListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(etlConnectionFactory());
		container.setAutoStartup(false);
		container.setRecoveryInterval(0);
		container.setQueues(etlQueue());
		container.setMessageListener(messageListener());
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return container;
	}

	@Bean
	public SimpleMessageListener messageListener() {
		return new SimpleMessageListener();
	}

}