package com.yunat.ccms.rule.center.conf.plan;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.request.MockMvcRequestBuilders;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.google.common.collect.Lists;
import com.yunat.ccms.Util;
import com.yunat.ccms.rule.center.conf.condition.Condition;
import com.yunat.ccms.rule.center.conf.condition.ConditionOp;
import com.yunat.ccms.rule.center.conf.condition.ConditionType;
import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.conf.plan.PlanController;
import com.yunat.ccms.rule.center.conf.rule.Rule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {//
"classpath:config/spring/spring-servlet.xml",//
		"classpath:config/spring/applicationContext.xml",//
		"classpath:config/security/applicationContext-security.xml" },//
loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class PlanControllerTest {

	@Autowired
	private PlanController controller;

	private MockMvc mock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		mock = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSave() {
		final DefaultRequestBuilder post = MockMvcRequestBuilders.post("/plan");
		final Plan plan = new Plan();
		plan.setName("这是一个方案" + Util.now());
		plan.setShopId("100571094");

		final List<Rule> rules = Lists.newArrayList();
		{// rule
			final Rule rule = new Rule();
			rule.setName("这是一个规则" + Util.now());
			rule.setRemarkContent("送小样儿A");
			final List<Condition> conditions = Lists.newArrayList();
			{// condition
				final Condition condition = new Condition();
				condition.setName("这是一个条件" + Util.now());
				condition.setType(ConditionType.CUSTOMER.name());
				// condition.setPropertyId(1L);// 客户统一ID
				condition.setConditionOpName(ConditionOp.GE.name());
				condition.setReferenceValue("200695759");
				conditions.add(condition);
			}
			{// condition
				final Condition condition = new Condition();
				condition.setName("这是一个条件" + Util.now());
				condition.setType(ConditionType.CUSTOMER.name());
				// condition.setPropertyId(18L);// 买家信用等级
				condition.setConditionOpName(ConditionOp.EQ.name());
				condition.setReferenceValue("1013");// 三心
				conditions.add(condition);
			}
			{// condition
				final Condition condition = new Condition();
				condition.setName("这是一个条件" + Util.now());
				condition.setType(ConditionType.ORDER.name());
				// condition.setPropertyId(54L);// 交易创建时间
				condition.setConditionOpName(ConditionOp.LE.name());
				condition.setReferenceValue("2013-10-01 00:00:00");
				conditions.add(condition);
			}
			rule.setConditions(conditions);
			rules.add(rule);
		}
		{// rule
			final Rule rule = new Rule();
			rule.setName("这是一个规则" + Util.now());
			rule.setRemarkContent("发顺丰");
			final List<Condition> conditions = Lists.newArrayList();
			{// condition
				final Condition condition = new Condition();
				condition.setName("这是一个条件" + Util.now());
				condition.setType(ConditionType.CUSTOMER.name());
				// condition.setPropertyId(1L);// 客户统一ID
				condition.setConditionOpName(ConditionOp.GE.name());
				condition.setReferenceValue("200695759");
				conditions.add(condition);
			}
			rule.setConditions(conditions);
			rules.add(rule);
		}
		plan.setRules(rules);

		body(post, plan);
		Util.request(mock, post);
	}

	/**
	 * @param post
	 * @param map
	 * @throws UnsupportedEncodingException
	 */
	protected void body(final DefaultRequestBuilder post, final Object o) {
		try {
			post.body(JSONObject.fromObject(o).toString().getBytes("UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// @Test
	// public void testUpdatePlan() {
	//
	// }

}
