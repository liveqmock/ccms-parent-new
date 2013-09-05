package com.yunat.ccms.rule.center.engine;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;

/**
 * 
 * 与RuleEngine交互的会话
 * 
 * @author xiaojing.qu
 * 
 */
public interface RuleEngineSession {

	FactResult execute(Fact fact) throws RuleCenterBusinessException;

}
