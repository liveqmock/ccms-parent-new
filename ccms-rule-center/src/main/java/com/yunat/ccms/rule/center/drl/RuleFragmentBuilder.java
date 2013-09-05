package com.yunat.ccms.rule.center.drl;


/**
 * RuleDRLBuilder生成一个DRL的规则
 * 
 * @author 
 * 
 */
public interface RuleFragmentBuilder<T> {
	public RuleFragment toRule(T obj);
}
