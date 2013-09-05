package com.yunat.ccms.node.support.cons;

public enum ControlGroupDataType {
	SENDING_GROUP(1), CONTROL_GROUP(0);

	private Integer code;

	private ControlGroupDataType(Integer _code) {
		this.code = _code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}