package com.yunat.ccms.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.yunat.ccms.core.support.cons.ProductEdition;

public class CcmsAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = -5301975374181871371L;

	public final ProductEdition productEdition;
	public final String loginUrl;

	public CcmsAuthenticationException(final String message, final ProductEdition productEdition, final String loginUrl) {
		super(message);
		this.loginUrl = loginUrl;
		this.productEdition = productEdition;
	}

	public CcmsAuthenticationException(final ProductEdition productEdition, final String loginUrl) {
		this("请重新登录", productEdition, loginUrl);
	}

	public CcmsAuthenticationException(final ProductEdition productEdition) {
		this(productEdition, "");
	}

	public ProductEdition getProductEdition() {
		return productEdition;
	}

	public String getLoginUrl() {
		return loginUrl;
	}
}
