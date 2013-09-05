package com.yunat.ccms.rule.center.conf.plan;

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
import com.yunat.ccms.rule.center.conf.rule.Rule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/applicationContext.xml"},
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class PlanDrlFileArtifactTest {

	@Autowired
	private PlanDRLArtifact planDrlFileArtifact;

	@Test
	public void testToDRL() {
		Plan plan = new Plan();
		plan.setId(6L);
		plan.setName("方案1");
		plan.setPosition(1);
		plan.setActive(true);
		plan.setShopId("100571094");

		Rule rule = new Rule();
		rule.setId(8L);
		rule.setName("规则1");
		rule.setPosition(1);
		rule.setPlanId(6L);
		rule.setRemarkContent("送礼品A");

		List<Condition> conditions = Lists.newArrayList();
//		Condition condition = new Condition();
//		condition.setId(11L);
//		condition.setName("这是一个条件2013-06-05");
//		condition.setRuleId(8L);
//		condition.setRelation("OR");
//		condition.setType("customer");
//		condition.setPropertyId(500L);
//		condition.setConditionOpName("GE");
//		condition.setReferenceValue("20069579");
//		conditions.add(condition);
//
//		Condition condition2 = new Condition();
//		condition2.setId(14L);
//		condition2.setName("这是一个订单指标条件20130605");
//		condition2.setRuleId(8L);
//		condition2.setRelation("AND");
//		condition2.setType("order");
//		condition2.setPropertyId(601L);
//		condition2.setConditionOpName("EQ");
//		condition2.setReferenceValue("1");
//		conditions.add(condition2);
//		rule.setConditions(conditions);

		Condition condition = new Condition();
		condition.setId(11L);
		condition.setName("这是一个条件2013-06-05");
		condition.setRuleId(9L);
		condition.setRelation("AND");
		condition.setType("customer");
		condition.setPropertyId(501L);
		condition.setConditionOpName("EQ");
		condition.setReferenceValue("1");
		conditions.add(condition);

		Condition condition2 = new Condition();
		condition2.setId(14L);
		condition2.setName("这是一个订单指标条件20130605");
		condition2.setRuleId(8L);
		condition2.setRelation("OR");
		condition2.setType("order");
		condition2.setPropertyId(605L);
		condition2.setConditionOpName("LIKE");
		condition.setReferenceValue("吉林");

		conditions.add(condition2);
		rule.setConditions(conditions);
		List<Rule> rules = Lists.newArrayList();
		rules.add(rule);

		Rule rule2 = new Rule();
		rule2.setId(9L);
		rule2.setName("规则2");
		rule2.setPosition(3);
		rule2.setPlanId(6L);
		rule2.setRemarkContent("送礼品B");

		List<Condition> conditions2 = Lists.newArrayList();
		Condition condition21 = new Condition();
		condition21.setId(11L);
		condition21.setName("这是一个条件2013-06-06");
		condition21.setRuleId(9L);
		condition21.setRelation("AND");
		condition21.setType("customer");
		condition21.setPropertyId(505L);
		condition21.setConditionOpName("GE");
		condition21.setReferenceValue("20069579");
		conditions2.add(condition21);

		Condition condition12 = new Condition();
		condition12.setId(14L);
		condition12.setName("这是一个订单指标条件20130606");
		condition12.setRuleId(9L);
		condition12.setRelation("OR");
		condition12.setType("order");
		condition12.setPropertyId(608L);
		condition12.setConditionOpName("IN");
		condition12.setReferenceValue("123,456");
		conditions2.add(condition12);
		rule2.setConditions(conditions2);
		rules.add(rule2);

		plan.setRules(rules);

		System.out.println(planDrlFileArtifact.toDRL(plan).toFileContent());
	}

}
