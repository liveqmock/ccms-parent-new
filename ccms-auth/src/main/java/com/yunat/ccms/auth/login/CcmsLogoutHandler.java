package com.yunat.ccms.auth.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.yunat.ccms.core.support.auth.AuthCons;

public class CcmsLogoutHandler implements LogoutHandler {

	@Override
	public void logout(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) {
		final HttpSession session = request.getSession();
		session.removeAttribute(AuthCons.LOGIN_INFO_SESSION_ATTR_NAME);
		_LoginInfoHolder._clearLoginInfo();
	}

	private static class _LoginInfoHolder extends LoginInfoHolder {
		public static void _clearLoginInfo() {
			setLoginInfo(null);
			LOGIN_SOURCE_LOCAL.set(null);
			PRODUCT_EDITION_LOCAL.set(null);
		}
	}
}
