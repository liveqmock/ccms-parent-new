package com.yunat.ccms.auth.exceptions;

import com.yunat.ccms.auth.login.LoginSource;
import com.yunat.ccms.core.support.exception.CcmsRuntimeException;

public class IllegalLoginParamException extends CcmsRuntimeException {

	private static final long serialVersionUID = -6807366757156271902L;

	public IllegalLoginParamException(final String msg, final Throwable t) {
		super(msg, t);
	}

	public IllegalLoginParamException(final String msg) {
		this(msg, null);
	}

	public static IllegalLoginParamException fromLoginSource(final LoginSource loginSource, final Throwable t) {
		return new IllegalLoginParamException(loginSource.getPlatformName() + "参数签名验证有误", t);
	}

	public static IllegalLoginParamException fromLoginSource(final LoginSource loginSource) {
		return fromLoginSource(loginSource, null);
	}
}
