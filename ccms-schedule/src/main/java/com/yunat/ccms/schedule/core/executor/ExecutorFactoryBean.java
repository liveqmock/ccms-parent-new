package com.yunat.ccms.schedule.core.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.yunat.ccms.configuration.variable.ApplicationVariable;
import com.yunat.ccms.core.support.concurrent.CCMSThreadFactory;
import com.yunat.ccms.schedule.core.TaskExecutor;

@Configuration
public class ExecutorFactoryBean {

	@Autowired
	private ApplicationVariable appVar;

	/**
	 * 执行short-lived类型的task 该类型的任务：占用资源少，运行时间短 拥有最高优先级
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "FAST_MOVING")
	@Scope("singleton")
	public TaskExecutor getFastExecutor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "fast_executor", "node_runner", Thread.MAX_PRIORITY);
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(0, 100, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);
		QueuedTaskExecutor execotor = new QueuedTaskExecutor("FAST_MOVING", poolExecutor);
		return execotor;
	}

	/**
	 * 该类型任务：严重占用IO资源，如果IO慢可能导致运行时间长 一般是 查询类节点
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "IO_BOUND")
	@Scope("singleton")
	public TaskExecutor getIOLimitedExecutor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "io_executor", "node_runner",
				Thread.MAX_PRIORITY - 2);
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);
		QueuedTaskExecutor execotor = new QueuedTaskExecutor("IO_BOUND", poolExecutor);
		return execotor;
	}

	/**
	 * 该类型任务：严重占用内存资源，如果内存增长过快会导致OutOfMemory 一般是 渠道类节点
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "MEM_BOUND")
	@Scope("singleton")
	public TaskExecutor getMEMLimitedExecutor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "mem_executor", "node_runner",
				Thread.MAX_PRIORITY - 1);
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);
		QueuedTaskExecutor execotor = new QueuedTaskExecutor("MEM_BOUND", poolExecutor);
		return execotor;
	}

	/**
	 * 默认的executor
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "NORMAL")
	@Scope("singleton")
	public TaskExecutor getNormalExecutor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "normal_executor", "node_runner",
				Thread.MAX_PRIORITY - 3);
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 30, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);
		QueuedTaskExecutor execotor = new QueuedTaskExecutor("NORMAL", poolExecutor);
		return execotor;
	}

	/**
	 * 该类型任务：严重占用CPU资源，如果CPU使用率高可能导致运行时间长
	 * 目前没有这类型的节点
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "CPU_BOUND")
	@Scope("singleton")
	public TaskExecutor getCPULimitedExecutor() throws Exception {
		return null;
	}

}
