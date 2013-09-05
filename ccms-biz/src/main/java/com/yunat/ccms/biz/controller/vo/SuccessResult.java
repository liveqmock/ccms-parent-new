package com.yunat.ccms.biz.controller.vo;

public class SuccessResult implements Result {
	private String status;
	private String code;
	private Object[] data;

	public SuccessResult(Object... data) {
		this.status = "ok";
		this.code = "0";
		this.data = data;
	}
	
	public String getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public Object[] getData() {
		return data;
	}
}