package com.yunat.ccms.auth.exceptions;

import com.yunat.ccms.core.support.exception.CcmsWrongValueException;

public class VerifyCodeWrongValueException extends CcmsWrongValueException {

	private static final long serialVersionUID = 5793671736982338271L;

	public VerifyCodeWrongValueException(final Throwable t) {
		super("验证码", t);
	}

	public VerifyCodeWrongValueException() {
		this(null);
	}

}
