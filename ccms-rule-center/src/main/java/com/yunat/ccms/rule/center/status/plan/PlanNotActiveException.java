package com.yunat.ccms.rule.center.status.plan;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public class PlanNotActiveException extends RuleCenterRuntimeException {

	private static final long serialVersionUID = 8293651188440288915L;

	public PlanNotActiveException() {
		super("该方案未开启");
	}

}
