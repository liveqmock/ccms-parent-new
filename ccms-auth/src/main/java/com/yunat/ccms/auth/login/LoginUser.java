package com.yunat.ccms.auth.login;

import com.yunat.ccms.auth.user.User;

public class LoginUser {
	private final User user;

	public LoginUser(final User user) {
		super();
		this.user = user;
	}

	public Long getId() {
		return user.getId();
	}

	public String getLoginName() {
		return user.getLoginName();
	}

	@Override
	public String toString() {
		return "LoginUser [getId()=" + getId() + ", getLoginName()=" + getLoginName() + "]";
	}
}