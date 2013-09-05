package com.yunat.ccms.core.support.ucenter;

import java.util.List;

public class UCenterResult<T> {
	private String status;
	private String errCode;
	private String message;
	private List<T> data;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "UCenterResult [status=" + status + ", errCode=" + errCode + ", message=" + message + ", data=" + data
				+ "]";
	}

}
