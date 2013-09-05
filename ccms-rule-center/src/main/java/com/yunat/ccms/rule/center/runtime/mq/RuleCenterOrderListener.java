package com.yunat.ccms.rule.center.runtime.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.event.bus.handler.ConsumerEventType;
import com.yunat.ccms.event.bus.listener.ConsumerEvent;
import com.yunat.ccms.event.bus.listener.ConsumerEventListener;

@Component
public class RuleCenterOrderListener implements ConsumerEventListener {

	private static Logger logger = LoggerFactory.getLogger(RuleCenterOrderListener.class);

	@Autowired
	private RcOrderService rcOrderService;

	@Override
	public void handleEvent(final ConsumerEvent event) {
		if (ConsumerEventType.ORDER == event.getType()) {
			final long batchId = System.currentTimeMillis();
			final List<String> tidList = event.getMessage();
			logger.info("规则引擎：从MQ过来的订单消息,共{}个订单,批次{}", tidList.size(), batchId);
			rcOrderService.save(batchId, tidList);
			rcOrderService.updateStatus(batchId);
			rcOrderService.toRcJob(batchId);
		}
	}
}