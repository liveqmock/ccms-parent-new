package com.yunat.ccms.auth.login.taobao.controller;

public class TaobaoUserViewResult {
	private Long id;
	private String platUserId;
	private String platUserName;
	private String shopName;
	private boolean isSubuser;
	private Boolean disabled; // 用户状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlatUserId() {
		return platUserId;
	}

	public void setPlatUserId(String platUserId) {
		this.platUserId = platUserId;
	}

	public String getPlatUserName() {
		return platUserName;
	}

	public void setPlatUserName(String platUserName) {
		this.platUserName = platUserName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isSubuser() {
		return isSubuser;
	}

	public void setSubuser(boolean isSubuser) {
		this.isSubuser = isSubuser;
	}

}
