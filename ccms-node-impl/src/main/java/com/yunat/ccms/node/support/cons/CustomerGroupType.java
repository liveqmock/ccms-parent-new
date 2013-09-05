package com.yunat.ccms.node.support.cons;

public enum CustomerGroupType {
	TARGET_GROUP(1), 
	RESPONSE_GROUP(2);

	private int type;

	private CustomerGroupType(int _type) {
		this.type = _type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}