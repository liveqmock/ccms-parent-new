package com.yunat.ccms.rule.center.conf.rule;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.yunat.ccms.rule.center.conf.plan.Plan;

public interface RuleService {

	public Rule getRule(Long ruleId);

	public void fillRules(Plan... plan);

	public void fillRules(Collection<Plan> plans);

	/**
	 * 根据规则id获取按方案position，规则position排序的规则列表
	 * 
	 * @param ruleIdSet
	 * @return
	 */
	public List<Rule> getOrderedRule(Set<Long> ruleIdSet);

}
