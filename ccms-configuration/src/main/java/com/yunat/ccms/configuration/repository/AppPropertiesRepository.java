package com.yunat.ccms.configuration.repository;

import java.util.Map;

public interface AppPropertiesRepository {

	String retrieveConfiguration(String propertyGroup, String propertyName);

	Map<String, String> retrieveAllConfiguration();

}