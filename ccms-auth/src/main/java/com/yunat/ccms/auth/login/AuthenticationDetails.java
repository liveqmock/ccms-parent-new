package com.yunat.ccms.auth.login;

import java.io.Serializable;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.yunat.ccms.core.support.cons.ProductEdition;

public class AuthenticationDetails implements Serializable {
	private static final long serialVersionUID = 3035369121558176182L;

	private final WebAuthenticationDetails webAuthenticationDetails;
	private final ProductEdition productEdition;

	public AuthenticationDetails(final WebAuthenticationDetails webAuthenticationDetails,
			final ProductEdition productEdition) {
		super();
		this.webAuthenticationDetails = webAuthenticationDetails;
		this.productEdition = productEdition;
	}

	public String getRemoteAddress() {
		return webAuthenticationDetails.getRemoteAddress();
	}

	public String getSessionId() {
		return webAuthenticationDetails.getSessionId();
	}

	public ProductEdition getProductEdition() {
		return productEdition;
	}

}