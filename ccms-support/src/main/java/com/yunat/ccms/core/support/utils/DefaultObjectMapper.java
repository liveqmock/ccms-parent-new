package com.yunat.ccms.core.support.utils;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

public class DefaultObjectMapper extends ObjectMapper {

	public DefaultObjectMapper() {
		super();
		configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
	}

}
