package com.yunat.ccms.rule.center.conf.rule;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.common.collect.Lists;
import com.yunat.ccms.rule.center.conf.condition.Condition;
import com.yunat.ccms.rule.center.drl.RuleFragment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/applicationContext.xml"}, 
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class RuleStatementArtifactTest {

	@Autowired
	private RuleStatementArtifact ruleStatementArtifact;
	
	@Test
	public void testToRule() {
		Rule rule = new Rule();
		rule.setId(8L);
		rule.setName("规则1");
		rule.setPosition(1);
		rule.setPlanId(6L);
		rule.setRemarkContent("送礼品A");
		
		List<Condition> conditions = Lists.newArrayList();
		Condition condition = new Condition();
		condition.setId(11L);
		condition.setName("这是一个条件2013-06-05");
		condition.setRuleId(8L);
		condition.setRelation("OR");
		condition.setType("customer");
		condition.setPropertyId(500L);
		condition.setConditionOpName("GE");
		condition.setReferenceValue("20069579");
		conditions.add(condition);
		
		Condition condition2 = new Condition();
		condition2.setId(14L);
		condition2.setName("这是一个订单指标条件20130605");
		condition2.setRuleId(8L);
		condition2.setRelation("AND");
		condition2.setType("order");
		condition2.setPropertyId(601L);
		condition2.setConditionOpName("EQ");
		condition2.setReferenceValue("1");
		conditions.add(condition2);
		rule.setConditions(conditions);
		
		RuleFragment rf = ruleStatementArtifact.toRule(rule);
		System.out.println(rf.toStatement());
	}

}
