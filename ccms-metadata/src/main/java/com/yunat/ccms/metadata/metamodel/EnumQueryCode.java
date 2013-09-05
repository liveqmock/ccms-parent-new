package com.yunat.ccms.metadata.metamodel;

public enum EnumQueryCode {

	CUSTOMER(1, "customer", "客户信息"), ORDER(2, "order", "订单总体信息"), ORDER_ITEM(3, "orderitem", "订单商品信息");

	private int key;
	private String code;
	private String chineseName;

	private EnumQueryCode(int key, String code, String chineseName) {
		this.key = key;
		this.code = code;
		this.chineseName = chineseName;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
}
