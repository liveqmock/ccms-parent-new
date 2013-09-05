package com.yunat.ccms.ucenter.rest.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author xiaojing.qu
 * 
 * @param <T>
 */
@XmlRootElement
public class RESTResponse<T> {

	private String status;
	private String errCode;
	private String message;
	private List<T> data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
