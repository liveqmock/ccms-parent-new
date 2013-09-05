package com.yunat.ccms.biz.controller.vo;

public class ErrorResult implements Result {
	private String status;
	private String code;
	private Object[] data;

	public ErrorResult(Object... data) {
		this.status = "error";
		this.code = "-1";
		this.data = data;
	}
	
	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public Object[] getData() {
		return this.data;
	}
}