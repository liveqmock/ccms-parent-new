package com.yunat.ccms.schedule.core.disruptor;

import java.io.Serializable;
import java.util.Map;

public class Event implements Serializable {

	private static final long serialVersionUID = -747803848295482920L;
	
	private volatile Map<String, ?> data;

	public Map<String, ?> getData() {
		return data;
	};

	public void setData(Map<String, ?> data) {
		this.data = data;
	}

}
