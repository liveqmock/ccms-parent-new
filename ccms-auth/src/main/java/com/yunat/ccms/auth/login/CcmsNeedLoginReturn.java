package com.yunat.ccms.auth.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import com.yunat.ccms.core.support.cons.ProductEdition;

public class CcmsNeedLoginReturn implements AuthenticationEntryPoint, AccessDeniedHandler, AuthenticationFailureHandler {
	private LoginSource defaultLoginSource;

	public CcmsNeedLoginReturn(final LoginSource defaultLoginSource) {
		super();
		this.defaultLoginSource = defaultLoginSource;
	}

	public CcmsNeedLoginReturn() {
		super();
	}

	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
			final AccessDeniedException accessDeniedException) throws IOException, ServletException {
		rt(response);
	}

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		rt(response);
	}

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException {
		rt(response);
	}

	/**
	 * @param response
	 * @param authException
	 * @throws IOException
	 */
	protected void rt(final HttpServletResponse response) throws IOException {
		final ProductEdition productEdition = LoginInfoHolder.getProductEdition();
		LoginSource loginSource = LoginInfoHolder.getLoginSource();
		if (loginSource == null) {
			loginSource = getDefaultLoginSource();
		}
		String loginUrl = loginSource == null ? "#" : loginSource.loginUrl(productEdition);
		if (!StringUtils.hasText(loginUrl)) {
			loginUrl = "#";
		}
		final String contentType = "application/json";
		response.setContentType(contentType);
		final PrintWriter out = response.getWriter();
		out.print("{\"needLogin\":true,\"edition\":" + productEdition.id//
				+ ",\"loginUrl\":\"" + loginUrl//
				+ "\",\"message\":\"登录验证失败，请重新登录\"}");
		out.flush();
		out.close();
	}

	public LoginSource getDefaultLoginSource() {
		return defaultLoginSource;
	}

	public void setDefaultLoginSource(final LoginSource defaultLoginSource) {
		this.defaultLoginSource = defaultLoginSource;
	}

}
