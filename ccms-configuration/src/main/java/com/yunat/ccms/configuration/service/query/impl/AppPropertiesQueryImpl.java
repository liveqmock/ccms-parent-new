package com.yunat.ccms.configuration.service.query.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;

public class AppPropertiesQueryImpl implements AppPropertiesQuery {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AppPropertiesRepository repository;

	private static Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

	@Override
	public String retrieveConfigValue(CCMSPropertiesEnum enum_) {
		String propertyGroup = enum_.getProp_group();
		String propertyName = enum_.getProp_name();
		String propertyKey = propertyGroup + "." + propertyName;
		String value = cacheMap.get(propertyKey);
		if (StringUtils.isNotBlank(value)) {
			logger.debug("property hit in cache! {} : {}", propertyKey, value);
			return value;
		}

		value = repository.retrieveConfiguration(propertyGroup, propertyName);
		if (value == null) {
			logger.error("no such config value found (in cache and db) or empty value for key: {}", propertyKey);
		} else {
			cacheMap.put(propertyKey, value);
		}
		return value;
	}

	@Override
	public Map<String, String> retrieveAllConfiguration() {
		return repository.retrieveAllConfiguration();
	}

	@Scheduled(fixedDelay = 1000 * 60)
	private void refresh() {
		logger.debug("refresh execute start ......");
		Map<String, String> map_ = retrieveAllConfiguration();
		if (MapUtils.isNotEmpty(map_)) {
			for (Map.Entry<String, String> entry : map_.entrySet()) {
				cacheMap.put(entry.getKey(), entry.getValue());
			}
		}
	}

}