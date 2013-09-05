package com.yunat.ccms.node.support.cons;

public enum ControlGroupSelectType {
	PERCENTAGE(1), // 百分比
	AMOUNT(2); // 数量

	private int type;

	private ControlGroupSelectType(int _type) {
		this.type = _type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}