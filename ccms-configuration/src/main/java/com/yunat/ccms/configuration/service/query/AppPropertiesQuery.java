package com.yunat.ccms.configuration.service.query;

import java.util.Map;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;

public interface AppPropertiesQuery {
	public String retrieveConfigValue(CCMSPropertiesEnum enum_);
	public Map<String, String> retrieveAllConfiguration();
}
