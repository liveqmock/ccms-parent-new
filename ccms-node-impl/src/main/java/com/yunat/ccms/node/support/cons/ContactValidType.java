package com.yunat.ccms.node.support.cons;

public enum ContactValidType {
	VALID(1),
	INVALID(0);

	private Integer code;

	private ContactValidType(Integer _code) {
		this.code = _code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}