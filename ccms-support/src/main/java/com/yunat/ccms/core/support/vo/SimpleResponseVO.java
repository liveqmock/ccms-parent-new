package com.yunat.ccms.core.support.vo;

@Deprecated
public class SimpleResponseVO {

	private Boolean success; // 操作是否成功，成功true，失败false
	private String code; // 响应码
	private String message; // 消息

	public SimpleResponseVO(Boolean success, String code, String message) {
		super();
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
