package com.yunat.ccms.core.support.exception;

public class CcmsRuntimeException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = -5539232539772910239L;

	public CcmsRuntimeException(final String msg, final Throwable t) {
		super(msg, t);
	}

	public CcmsRuntimeException(final String msg) {
		super(msg);
	}
}
