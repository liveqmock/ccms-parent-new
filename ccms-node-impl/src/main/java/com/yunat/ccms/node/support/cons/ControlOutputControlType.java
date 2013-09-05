package com.yunat.ccms.node.support.cons;

public enum ControlOutputControlType {
	EC_SUCCESS_RESPONSE_TYPE(10L), EC_FAILURE_RESPONSE_TYPE(11L);

	private Long code;

	private ControlOutputControlType(Long _code) {
		this.code = _code;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

}