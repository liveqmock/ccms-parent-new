/**
 *
 */
package com.yunat.ccms.tradecenter.urpay.task;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.yunat.ccms.configuration.variable.ApplicationVariable;
import com.yunat.ccms.core.support.concurrent.CCMSThreadFactory;
import com.yunat.ccms.core.support.quartz.SpringAutowringJobListener;

/**
 * quartz配置类
 * 
 * @author xiahui.zhang
 * @version 创建时间：2013-6-5 上午09:10:30
 */
@Configuration
public class QuartzTaskFactoryBean {

	public static final String QRTZ_APPLICATION_CONTEXT_KEY = "applicationContext";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ApplicationVariable appVar;

	@Bean
	@Scope("singleton")
	JobListener SpringAutowringJobListener() {
		return new SpringAutowringJobListener();
	}

	@Bean
	@Scope("singleton")
	Executor executor() {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "trade_center", "runner",
				Thread.NORM_PRIORITY + 1);
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 20, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);
		return poolExecutor;
	}

	@Bean(name = "quartz-tc")
	@Scope("singleton")
	public org.springframework.scheduling.quartz.SchedulerFactoryBean factory() {
		SchedulerFactoryBean scheduleFactory = new SchedulerFactoryBean();
		scheduleFactory.setApplicationContextSchedulerContextKey(QRTZ_APPLICATION_CONTEXT_KEY);
		scheduleFactory.setConfigLocation(new ClassPathResource("quartz-tc.properties"));
		scheduleFactory.setGlobalJobListeners(new JobListener[] { SpringAutowringJobListener() });
		scheduleFactory.setDataSource(dataSource);
		scheduleFactory.setTaskExecutor(executor());
		scheduleFactory.setAutoStartup(false);// 需要等到重启后的初始化结束后才能开始触发trigger，否者数据不全
		return scheduleFactory;
	}

}
