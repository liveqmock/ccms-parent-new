package com.yunat.ccms.auth.login.kfxt;


public class KFXTParams {

	private String kfxt_appkey;
	private String kfxt_sign;
	private String kfxt_time;
	private String kfxt_member_name;
	private String kfxt_user_name;
	private String user_name;
	private String password;

	public KFXTParams() {
	}

	public String getKfxt_appkey() {
		return kfxt_appkey;
	}

	public void setKfxt_appkey(final String kfxt_appkey) {
		this.kfxt_appkey = kfxt_appkey;
	}

	public String getKfxt_sign() {
		return kfxt_sign;
	}

	public void setKfxt_sign(final String kfxt_sign) {
		this.kfxt_sign = kfxt_sign;
	}

	public String getKfxt_time() {
		return kfxt_time;
	}

	public void setKfxt_time(final String kfxt_time) {
		this.kfxt_time = kfxt_time;
	}

	public String getKfxt_member_name() {
		return kfxt_member_name;
	}

	public void setKfxt_member_name(final String kfxt_member_name) {
		this.kfxt_member_name = kfxt_member_name;
	}

	public String getKfxt_user_name() {
		return kfxt_user_name;
	}

	public void setKfxt_user_name(final String kfxt_user_name) {
		this.kfxt_user_name = kfxt_user_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(final String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}
