package com.yunat.ccms.rule.center;

import com.yunat.ccms.core.support.exception.CcmsRuntimeException;

public class RuleCenterRuntimeException extends CcmsRuntimeException {

	private static final long serialVersionUID = 7890568402074678984L;

	public RuleCenterRuntimeException(final String msg, final Throwable t) {
		super(msg, t);
	}

	public RuleCenterRuntimeException(final String msg) {
		super(msg);
	}

}
