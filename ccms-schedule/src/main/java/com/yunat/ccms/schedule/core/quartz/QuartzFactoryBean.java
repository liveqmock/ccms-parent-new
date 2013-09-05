package com.yunat.ccms.schedule.core.quartz;

import javax.sql.DataSource;

import org.quartz.JobListener;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.yunat.ccms.core.support.quartz.SpringAutowringJobListener;
import com.yunat.ccms.schedule.core.quartz.listener.JobExecuteLogListener;
import com.yunat.ccms.schedule.core.quartz.listener.TriggerExecuteLogListener;

@Configuration
public class QuartzFactoryBean {

	public static final String QRTZ_APPLICATION_CONTEXT_KEY = SpringAutowringJobListener.QRTZ_APPLICATION_CONTEXT_KEY;

	@Autowired
	private DataSource dataSource;

	@Bean
	@Scope("singleton")
	JobListener SpringAutowringJobListener() {
		return new SpringAutowringJobListener();
	}
	
	@Bean
	@Scope("singleton")
	JobListener JobExecuteLogListener() {
		return new JobExecuteLogListener();
	}
	
	@Bean
	@Scope("singleton")
	TriggerListener TriggerExecuteLogListener() {
		return new TriggerExecuteLogListener();
	}

	@Bean(name = "timer")
	@Scope("singleton")
	public org.springframework.scheduling.quartz.SchedulerFactoryBean factory() {
		SchedulerFactoryBean scheduleFactory = new SchedulerFactoryBean();
		scheduleFactory.setApplicationContextSchedulerContextKey(QRTZ_APPLICATION_CONTEXT_KEY);
		scheduleFactory.setConfigLocation(new ClassPathResource("config/quartz-timer.properties"));
		scheduleFactory.setGlobalJobListeners(new JobListener[] { SpringAutowringJobListener(),JobExecuteLogListener() });
		scheduleFactory.setGlobalTriggerListeners(new TriggerListener[]{TriggerExecuteLogListener()});
		scheduleFactory.setDataSource(dataSource);
		scheduleFactory.setAutoStartup(false);// 需要等到重启后的初始化结束后才能开始触发trigger，否者数据不全
		return scheduleFactory;
	}

}
