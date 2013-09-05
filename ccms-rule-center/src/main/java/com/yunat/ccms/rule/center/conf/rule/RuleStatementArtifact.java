package com.yunat.ccms.rule.center.conf.rule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.rule.center.conf.condition.Condition;
import com.yunat.ccms.rule.center.drl.LHSFragment;
import com.yunat.ccms.rule.center.drl.LHSFragmentBuilder;
import com.yunat.ccms.rule.center.drl.RHSFragment;
import com.yunat.ccms.rule.center.drl.RuleFragment;
import com.yunat.ccms.rule.center.drl.RuleFragmentBuilder;

@Component
public class RuleStatementArtifact implements RuleFragmentBuilder<Rule> {

	@Autowired
	private LHSFragmentBuilder<Condition> cea;

	@Override
	public RuleFragment toRule(final Rule rule) {
		final RuleFragment rs = new RuleFragment();
		rs.setName("rule_" + rule.getId());
		rs.setSalience(-rule.getPosition());// 注意:这里有一个取负操作,别因为负号太小看漏了
		rs.setActivationGroup("plan_" + rule.getPlanId());

		// Left Hand side
		final List<Condition> conditions = rule.getConditions();
		final List<LHSFragment> lhsList = Lists.newArrayListWithExpectedSize(conditions.size());
		for (final Condition condition : conditions) {
			lhsList.add(cea.toLHS(condition));
		}
		rs.setLhs(lhsList);

		// Right Hand side
		final RHSFragment rhs = new RHSFragment();
		rhs.setExpression("$order.hit(" + rule.getPlanId() + "L," + rule.getId() + "L);");
		rs.setRhs(rhs);

		return rs;
	}

}