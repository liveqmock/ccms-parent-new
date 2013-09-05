package com.yunat.ccms.rule.center.conf.plan;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public class PlanNoRulesException extends RuleCenterRuntimeException {

	private static final long serialVersionUID = 690819719284824360L;

	public PlanNoRulesException(final Throwable t) {
		super("该方案没有创建规则，不能开启", t);
	}

	public PlanNoRulesException() {
		this(null);
	}

}
