package com.yunat.ccms.auth.login;

import java.util.Date;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.cons.ProductEdition;

public class LoginInfoImpl implements LoginInfo {

	private final VisitInfo visitInfo;
	private final User user;

	public LoginInfoImpl(final VisitInfo visitInfo, final User user) {
		super();
		this.visitInfo = visitInfo;
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public Date getLoginTime() {
		return visitInfo.getRequestTime();
	}

	@Override
	public ProductEdition getProductEdition() {
		return visitInfo.getProductEdition();
	}

	public VisitInfo getVisitInfo() {
		return visitInfo;
	}

}
