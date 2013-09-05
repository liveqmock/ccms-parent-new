package com.yunat.ccms.rule.center;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.yunat.ccms.configuration.variable.ApplicationVariable;
import com.yunat.ccms.core.support.concurrent.CCMSThreadFactory;

@Configuration
@EnableScheduling
public class SpringScheduleConfiguration implements SchedulingConfigurer {

	@Autowired
	private ApplicationVariable appVar;

	@Bean(name = "taskScheduler")
	public Executor taskScheduler() {
		ThreadFactory factory = new CCMSThreadFactory(appVar.getTenentId(), "spring", "task_runner", 5);
		return Executors.newScheduledThreadPool(20, factory);
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler());
	}

	@Bean(name = "rcJobRunnerExecutor")
	public Executor rcJobRunnerExecutor() {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "rule_center", "job_runner",
				Thread.NORM_PRIORITY + 1);
		return new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
	}

	@Bean(name = "momoUpdaterExecutor")
	public Executor momoUpdaterExecutor() {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "rule_center", "memo_updater",
				Thread.NORM_PRIORITY + 1);
		return new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
	}

}
