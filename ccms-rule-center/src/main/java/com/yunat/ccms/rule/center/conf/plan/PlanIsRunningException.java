package com.yunat.ccms.rule.center.conf.plan;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public class PlanIsRunningException extends RuleCenterRuntimeException {

	private static final long serialVersionUID = 8366950687543586649L;

	public final Plan plan;

	public PlanIsRunningException(final Plan plan) {
		super("方案开启状态下不允许修改。");
		this.plan = plan;
	}

}
