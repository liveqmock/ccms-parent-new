package com.yunat.ccms.rule.center.drl;

import org.junit.Test;

public class RuleFragmentTest {

	@Test
	public void testRemoveConditionRelationInAnd() {
		String lhsExpression = " && customer.uniId = \"12344\"";
		RuleFragment rf = new RuleFragment();
		System.out.println(rf.removeConditionRelationIn(lhsExpression));
	}

	@Test
	public void testRemoveConditionRelationInOr() {
		String lhsExpression = " || customer.uniId = \"12344\"";
		RuleFragment rf = new RuleFragment();
		System.out.println(rf.removeConditionRelationIn(lhsExpression));
	}
}
