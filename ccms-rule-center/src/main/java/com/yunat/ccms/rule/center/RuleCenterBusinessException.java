package com.yunat.ccms.rule.center;

public class RuleCenterBusinessException extends Exception {

	private static final long serialVersionUID = 7890568402074678984L;

	public RuleCenterBusinessException(final String msg) {
		super(msg);
	}

	public RuleCenterBusinessException(final String msg, final Throwable t) {
		super(msg, t);
	}

}
