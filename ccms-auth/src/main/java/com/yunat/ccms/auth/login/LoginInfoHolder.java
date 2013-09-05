package com.yunat.ccms.auth.login;

import javax.servlet.http.HttpServletRequest;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.auth.AuthCons;
import com.yunat.ccms.core.support.cons.ProductEdition;

/**
 * 当前登录用户的LoginInfo信息的持有者
 * 
 * @author wenjian.liang
 */
public class LoginInfoHolder {

	protected static final ThreadLocal<LoginInfo> LOGIN_INFO_LOCAL = new ThreadLocal<LoginInfo>();
	protected static final ThreadLocal<ProductEdition> PRODUCT_EDITION_LOCAL = new ThreadLocal<ProductEdition>();
	protected static final ThreadLocal<LoginSource> LOGIN_SOURCE_LOCAL = new ThreadLocal<LoginSource>();

	public static ProductEdition getProductEdition() {
		return PRODUCT_EDITION_LOCAL.get();
	}

	public static LoginSource getLoginSource() {
		return LOGIN_SOURCE_LOCAL.get();
	}

	/**
	 * 获取当前登录的用户的LoginInfo对象
	 * 
	 * @return
	 */
	public static LoginInfo getLoginInfo() {
		return LOGIN_INFO_LOCAL.get();
	}

	/**
	 * 获取当前登录的用户的User对象.
	 * 
	 * @return
	 * @throws NeedLoginException
	 */
	public static User getCurrentUser() throws NeedLoginException {
		final LoginInfo loginInfo = getLoginInfo();
		if (loginInfo == null) {
			throw new NeedLoginException();
		}
		return loginInfo.getUser();
	}

	protected static void setLoginInfo(final LoginInfo loginInfo) {
		LOGIN_INFO_LOCAL.set(loginInfo);
	}

	protected static void setLoginInfo(final LoginInfo loginInfo, final HttpServletRequest request) {
		setLoginInfo(loginInfo);
		request.getSession(false).setAttribute(AuthCons.LOGIN_INFO_SESSION_ATTR_NAME, loginInfo);
	}
}
