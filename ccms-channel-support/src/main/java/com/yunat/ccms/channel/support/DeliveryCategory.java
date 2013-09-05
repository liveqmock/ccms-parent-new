package com.yunat.ccms.channel.support;

public enum DeliveryCategory {
	PERSON,
	ORDER;
	
	private DeliveryCategory() {}
	
	public String getCode() {
		return this.toString();
	}
}
