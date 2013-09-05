package com.yunat.ccms.schedule.core.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

@Component("disruptorDelegate")
@Scope("singleton")
public class DisruptorDelegate<T> {

	/** 开始队列，被处理后节点做执行准备 */
	@Autowired
	@Qualifier("startDisruptor")
	private Disruptor<T> startDisruptor;

	/** 准备队列，被处理后进入执行队列 */
	@Autowired
	@Qualifier("readyDisruptor")
	private Disruptor<T> readyDisruptor;

	/** 完成队列，被处理后相应任务结束 */
	@Autowired
	@Qualifier("endDisruptor")
	private Disruptor<T> endDisruptor;

	/** 跳过队列，被处理后相应任务被标志为跳过 */
	@Autowired
	@Qualifier("skipDisruptor")
	private Disruptor<T> skipDisruptor;

	private static final Logger logger = LoggerFactory.getLogger(DisruptorDelegate.class);

	/**
	 * 产生任务正式执行的事件
	 */
	public void fireStartEvent(EventTranslator<T> eventTranslator) {
		if (eventTranslator == null) {
			return;
		}
		startDisruptor.publishEvent(eventTranslator);
		logger.info("产生开始事件：" + eventTranslator.toString());
	}

	public void handStartEventWith(EventHandler<T> handler) {
		startDisruptor.handleEventsWith(handler);
		startDisruptor.start();
		logger.info("startDisruptor started!");
	}

	/**
	 * 产生任务准备执行的事件
	 */
	public void fireReadyEvent(EventTranslator<T> eventTranslator) {
		if (eventTranslator == null) {
			return;
		}
		readyDisruptor.publishEvent(eventTranslator);
		logger.info("产生就绪事件：" + eventTranslator.toString());
	}

	public void handReadyEventWith(EventHandler<T> handler) {
		readyDisruptor.handleEventsWith(handler);
		readyDisruptor.start();
		logger.info("readyDisruptor started!");
	}

	/**
	 * 产生任务执行结束的事件
	 */
	public void fireEndEvent(EventTranslator<T> eventTranslator) {
		if (eventTranslator == null) {
			return;
		}
		endDisruptor.publishEvent(eventTranslator);
		logger.info("产生结束事件：" + eventTranslator.toString());
	}

	public void handEndEventWith(EventHandler<T> handler) {
		endDisruptor.handleEventsWith(handler);
		endDisruptor.start();
		logger.info("endDisruptor started!");
	}

	/**
	 * 产生任务执行跳过的事件
	 */
	public void fireSkipEvent(EventTranslator<T> eventTranslator) {
		if (eventTranslator == null) {
			return;
		}
		skipDisruptor.publishEvent(eventTranslator);
		logger.info("产生跳过事件：" + eventTranslator.toString());
	}

	public void handSkipEventWith(EventHandler<T> handler) {
		skipDisruptor.handleEventsWith(handler);
		skipDisruptor.start();
		logger.info("skipDisruptor started!");
	}

}
