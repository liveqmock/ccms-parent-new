package com.yunat.ccms.event.bus.config;

import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.configuration.service.query.impl.AppPropertiesQueryImpl;

@Configuration
@Import(PropertyPlaceholderConfiguration.class)
public class AbstractRabbitConfiguration {

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JsonMessageConverter();
	}

	@Bean
	public AppPropertiesQuery propertiesQuery() {
		return new AppPropertiesQueryImpl();
	}
	
}

@Configuration
class PropertyPlaceholderConfiguration {
	@Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertyPlaceholderConfigurer();
	}
}
