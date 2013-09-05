package com.yunat.ccms.auth.exceptions;

import com.yunat.ccms.core.support.exception.CcmsRuntimeException;

public class NoAuthorizationException extends CcmsRuntimeException {

	private static final long serialVersionUID = -5447242676910544848L;

	public NoAuthorizationException(final Throwable t) {
		super("无权限", t);
	}

	public NoAuthorizationException() {
		this(null);
	}
}
