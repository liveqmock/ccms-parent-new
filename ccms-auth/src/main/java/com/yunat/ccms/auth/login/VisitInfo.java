package com.yunat.ccms.auth.login;

import java.util.Date;

import com.yunat.ccms.core.support.cons.ProductEdition;

public class VisitInfo {

	private final Date requestTime = new Date();
	private final ProductEdition productEdition;
	private final LoginSource loginSource;

	public VisitInfo(final ProductEdition productEdition, final LoginSource loginSource) {
		super();
		this.productEdition = productEdition;
		this.loginSource = loginSource;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public ProductEdition getProductEdition() {
		return productEdition;
	}

	public LoginSource getLoginSource() {
		return loginSource;
	}
}
