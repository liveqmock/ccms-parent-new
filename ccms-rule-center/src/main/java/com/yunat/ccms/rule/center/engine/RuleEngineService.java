package com.yunat.ccms.rule.center.engine;

import java.io.Serializable;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;

public interface RuleEngineService {

	public FactResult execute(String planGroupId, Serializable factId, Class factType)
			throws RuleCenterBusinessException;

}
