package com.yunat.ccms.rule.center.conf.plan;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public class NoSuchPlanException extends RuleCenterRuntimeException {
	private static final long serialVersionUID = -5704556685934116414L;

	public NoSuchPlanException(final Throwable t) {
		super("没有这个方案", t);
	}

	public NoSuchPlanException() {
		this(null);
	}

}
