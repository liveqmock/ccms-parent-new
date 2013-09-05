package com.yunat.ccms.core.support.exception;

public class CcmsBusinessException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8877699626575969298L;

	public CcmsBusinessException(String msg, Throwable t) {
		super(msg, t);
	}

	public CcmsBusinessException(String msg) {
		super(msg);
	}
}
