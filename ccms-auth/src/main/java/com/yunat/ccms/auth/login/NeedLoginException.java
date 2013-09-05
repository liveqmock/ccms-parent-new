package com.yunat.ccms.auth.login;

import com.yunat.ccms.core.support.exception.CcmsRuntimeException;

public class NeedLoginException extends CcmsRuntimeException {

	private static final long serialVersionUID = 3751523775042246324L;

	public NeedLoginException() {
		super("未登录", null);
	}

	public NeedLoginException(final String msg) {
		super(msg, null);
	}
}
