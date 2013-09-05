package com.yunat.ccms.rule.center.conf.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yunat.ccms.rule.center.conf.Util;
import com.yunat.ccms.rule.center.conf.condition.ConditionService;
import com.yunat.ccms.rule.center.conf.plan.Plan;

@Service
public class RuleServiceImpl implements RuleService {

	@Autowired
	protected ConditionService conditionService;
	@Autowired
	protected RuleRepository ruleRepository;

	public List<Rule> rulesOfPlan(final long planId) {
		final List<Rule> rules = ruleRepository.findByPlanId(planId);
		Collections.sort(rules, Util.POSITION_COMPARATOR);
		return rules;
	}

	@Override
	public void fillRules(final Plan... plan) {
		fillRules(Arrays.asList(plan));
	}

	@Override
	public void fillRules(final Collection<Plan> plans) {
		if (plans == null || plans.isEmpty()) {
			return;
		}
		final Map<Long, Plan> idMap = Maps.newHashMap();
		for (final Plan o : plans) {
			idMap.put(o.getId(), o);
			o.setRules(new ArrayList<Rule>());
		}
		// 取得规则列表
		final List<Rule> rules = ruleRepository.findByPlanIdIn(idMap.keySet());
		for (final Rule o : rules) {
			idMap.get(o.getPlanId()).getRules().add(o);
		}
		// 填充条件
		conditionService.fillCondition(rules);
		// 对方案中的规则排序
		for (final Plan o : plans) {
			Collections.sort(o.getRules(), Util.POSITION_COMPARATOR);
		}
	}

	@Override
	public Rule getRule(Long ruleId) {
		return ruleRepository.findOne(ruleId);
	}

	@Override
	public List<Rule> getOrderedRule(Set<Long> ruleIdSet) {
		return ruleRepository.findOrderedRule(ruleIdSet);
	}
}
