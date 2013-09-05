package com.yunat.ccms.rule.center.conf.condition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.yunat.ccms.rule.center.drl.LHSFragment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/applicationContext.xml"},
		loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class ConditionExpressionArtifactTest {

	@Autowired
	private ConditionExpressionArtifact conditionExpressionArtifact;

	@Test
	public void testToLHS() {
		Condition condition = new Condition();
//		condition.setId(11L);
//		condition.setName("这是一个条件2013-06-05");
//		condition.setRuleId(9L);
//		condition.setRelation("AND");
//		condition.setType("customer");
//		condition.setPropertyId(501L);
//		condition.setConditionOpName("EQ");
//		condition.setReferenceValue("1");

		condition.setId(14L);
		condition.setName("这是一个订单指标条件20130605");
		condition.setRuleId(8L);
		condition.setRelation("OR");
		condition.setType("order");
		condition.setPropertyId(605L);
		condition.setConditionOpName("LIKE");
		condition.setReferenceValue("吉林");

		LHSFragment lhs = conditionExpressionArtifact.toLHS(condition);
		System.out.println(lhs.getExpression());
	}

}