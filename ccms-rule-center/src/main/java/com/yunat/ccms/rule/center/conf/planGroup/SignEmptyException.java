package com.yunat.ccms.rule.center.conf.planGroup;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public class SignEmptyException extends RuleCenterRuntimeException {

	private static final long serialVersionUID = 328531700982128712L;

	public SignEmptyException() {
		super("请填写默认签名。");
	}
}
