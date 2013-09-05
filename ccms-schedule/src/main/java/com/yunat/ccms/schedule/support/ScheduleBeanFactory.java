package com.yunat.ccms.schedule.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTemplate;

@SuppressWarnings("deprecation")
@Configuration
public class ScheduleBeanFactory {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JpaTemplate getJpaTemplate() {
		return new JpaTemplate(entityManager);
	}

}
