package com.yunat.ccms.schedule.core.disruptor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.MultiThreadedClaimStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.yunat.ccms.configuration.variable.ApplicationVariable;
import com.yunat.ccms.core.support.concurrent.CCMSThreadFactory;

@Configuration
public class DisruptorFactoryBean {

	@Autowired
	private ApplicationVariable appVar;

	@Bean(name = "startDisruptor")
	@Scope("singleton")
	public Disruptor<Event> getStartDisruptor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "start_event", "handler",
				Thread.MAX_PRIORITY - 1);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);

		EventFactory<Event> eventFactory = new EventFactory<Event>() {
			@Override
			public Event newInstance() {
				return new Event();
			}
		};
		ClaimStrategy claimStrategy = new MultiThreadedClaimStrategy(1024);
		WaitStrategy waitStrategy = new BlockingWaitStrategy();
		Disruptor<Event> disruptor = new Disruptor<Event>(eventFactory, executor, claimStrategy, waitStrategy);
		return disruptor;
	}

	@Bean(name = "readyDisruptor")
	@Scope("singleton")
	public Disruptor<Event> getReadyDisruptor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "ready_event", "handler",
				Thread.MAX_PRIORITY - 1);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);

		EventFactory<Event> eventFactory = new EventFactory<Event>() {
			@Override
			public Event newInstance() {
				return new Event();
			}
		};
		ClaimStrategy claimStrategy = new MultiThreadedClaimStrategy(1024);
		WaitStrategy waitStrategy = new BlockingWaitStrategy();
		Disruptor<Event> disruptor = new Disruptor<Event>(eventFactory, executor, claimStrategy, waitStrategy);
		return disruptor;
	}

	@Bean(name = "endDisruptor")
	@Scope("singleton")
	public Disruptor<Event> getEndDisruptor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "end_event", "handler", Thread.MAX_PRIORITY);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);

		EventFactory<Event> eventFactory = new EventFactory<Event>() {
			@Override
			public Event newInstance() {
				return new Event();
			}
		};
		ClaimStrategy claimStrategy = new MultiThreadedClaimStrategy(1024);
		WaitStrategy waitStrategy = new BlockingWaitStrategy();
		Disruptor<Event> disruptor = new Disruptor<Event>(eventFactory, executor, claimStrategy, waitStrategy);
		return disruptor;
	}

	@Bean(name = "skipDisruptor")
	@Scope("singleton")
	public Disruptor<Event> getSkipDisruptor() throws Exception {
		ThreadFactory threadFactory = new CCMSThreadFactory(appVar.getTenentId(), "skip_event", "handler",
				Thread.MAX_PRIORITY - 2);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), threadFactory);

		EventFactory<Event> eventFactory = new EventFactory<Event>() {
			@Override
			public Event newInstance() {
				return new Event();
			}
		};
		ClaimStrategy claimStrategy = new MultiThreadedClaimStrategy(1024);
		WaitStrategy waitStrategy = new BlockingWaitStrategy();
		Disruptor<Event> disruptor = new Disruptor<Event>(eventFactory, executor, claimStrategy, waitStrategy);
		return disruptor;
	}

}
