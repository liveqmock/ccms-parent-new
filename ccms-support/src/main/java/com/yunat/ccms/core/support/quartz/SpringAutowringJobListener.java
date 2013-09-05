package com.yunat.ccms.core.support.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * 自动将spring的bean注入quartz job
 * 
 * @author hanzheng
 * 
 */
public class SpringAutowringJobListener implements JobListener {

	private static final Logger logger = LoggerFactory.getLogger(SpringAutowringJobListener.class.getName());
	
	public static final String QRTZ_APPLICATION_CONTEXT_KEY = "applicationContext";

	@Override
	public String getName() {
		return "SPRING_AUTO_WIRE";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();
			Job job = context.getJobInstance();
			ApplicationContext springApplicationContext = (ApplicationContext) schedulerContext.get(QRTZ_APPLICATION_CONTEXT_KEY);
			AutowireCapableBeanFactory beanFactory = springApplicationContext
					.getAutowireCapableBeanFactory();
			beanFactory.autowireBean(job);
		} catch (Exception e) {
			logger.error("Error autowiring spring based job", e);
		}
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,JobExecutionException jobException) {
	}

}

