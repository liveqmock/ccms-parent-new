package com.yunat.ccms.event.bus.handler;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yunat.base.util.JackSonMapper;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.io.FileUtils;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.event.bus.listener.ConsumerEvent;
import com.yunat.ccms.event.bus.listener.ConsumerEventListener;

public class SimpleMessageListener implements MessageListener {
	private static Logger logger = LoggerFactory.getLogger(SimpleMessageListener.class);
	private static final String HEADERS_TYPE_KEY = "type";

	static ThreadPoolExecutor executorPool;
	static {
		// Get the ThreadFactory implementation to use
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		// creating the ThreadPoolExecutor
		executorPool = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
				threadFactory);

	}

	@Autowired(required = false)
	private List<ConsumerEventListener> registedListener;

	@Autowired
	private AppPropertiesQuery query;

	@Override
	public void onMessage(Message message) {
		MessageProperties properties = message.getMessageProperties();
		Map<String, Object> map = properties.getHeaders();
		String messageBody = new String(message.getBody());
		logger.info("Received data : {}", messageBody);
		System.out.println("Received data : " + messageBody);

		final ConsumerEventType type = ConsumerEventType.valueOfIgnoreCase((String) map.get(HEADERS_TYPE_KEY));
		logger.info("consumer event type : {}", type.getValue());
		System.out.println("consumer event type : " + type.getValue());
		
		// write file as log
		writeFileAsLogger(type, messageBody);

		// selection current user's data
		dispatchExecutor(type, messageBody);
	}

	private void writeFileAsLogger(final ConsumerEventType type, final String message) {
		final String fileStoreRoot = query.retrieveConfigValue(CCMSPropertiesEnum.CCMS_UPLOAD_DIR);
		final String secondDir = query.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		final String currentDate = DateUtils.getString(new Date());
		final String fileName = type.getValue() + "_" + DateUtils.getFullStringDate(new Date()) + ".txt";
		final String fileFullPath = FileUtils.concat(fileStoreRoot, "event-data", secondDir, currentDate, fileName);
		try {
			FileUtils.writeFileByUTF8(fileFullPath, message, false);
		} catch (IOException e) {
			logger.info("write file as logger happend exception.");
			e.printStackTrace();
		}
	}

	private void dispatchExecutor(final ConsumerEventType type, final String message) {
		List<String> validOrders = Lists.newArrayList();
		JsonNode jsonNode = JackSonMapper.toJsonNode(message);
		String[] arr = JackSonMapper.jsonNodeToClass(jsonNode, String[].class);
		for (String tid : arr) {
			validOrders.add(tid);
		}

		if (!CollectionUtils.isEmpty(validOrders)) {
			notifyConsumerEvent(type, validOrders);
		} else {
			logger.info("this consumer find order list is empty, not to notify all of event listeners");
		}

	}

	private void notifyConsumerEvent(final ConsumerEventType type, final List<String> message) {
		if (!CollectionUtils.isEmpty(registedListener)) {
			for (final ConsumerEventListener listener : registedListener) {
				executorPool.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							listener.handleEvent(new ConsumerEvent(type, message));
						} catch (Exception e) {
							logger.info("handle event happend exception, listener = " + listener.getClass());
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

}