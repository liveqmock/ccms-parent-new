package com.yunat.ccms.tradecenter.controller.vo;

public class AutoUrpayAndCareMonitoringVO {
	
	/**
	 * 功能名称
	 */
	private String name;
	
	/**
	 * 功能状态ID
	 */
	private Integer type;
	
	/**
	 * 是否打开功能,1:开,0:关'
	 */
	private Integer isOpen;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	
	

}
