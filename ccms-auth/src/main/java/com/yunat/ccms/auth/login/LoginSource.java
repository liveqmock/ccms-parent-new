package com.yunat.ccms.auth.login;

import javax.servlet.http.HttpServletRequest;

import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.cons.ProductEdition;

public interface LoginSource {

	boolean support(HttpServletRequest request, ProductEdition productEdition);

	User authentication(HttpServletRequest request) throws IllegalLoginParamException;

	String loginUrl(ProductEdition productEdition);

	String getPlatformName();
}
