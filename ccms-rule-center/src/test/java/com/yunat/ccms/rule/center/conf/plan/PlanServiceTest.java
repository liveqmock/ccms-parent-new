package com.yunat.ccms.rule.center.conf.plan;

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yunat.ccms.rule.center.conf.plan.Plan;
import com.yunat.ccms.rule.center.conf.plan.PlanRepository;
import com.yunat.ccms.rule.center.conf.plan.PlanService;

public class PlanServiceTest {

	private static final String TEST_SHOP_ID = "testShopId";

	protected static ApplicationContext applicationContext;
	protected static PlanService planService;
	protected static PlanRepository planRepository;

	private static final int TEST_PLANS_COUNT = 3;
	private static Long[] TEST_PLAN_IDS = new Long[TEST_PLANS_COUNT];

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		planService = applicationContext.getBean(PlanService.class);
		planRepository = applicationContext.getBean(PlanRepository.class);

		//测试数据.这里已经测试save了
		for (int i = 0; i < TEST_PLANS_COUNT; ++i) {
			final Plan plan = new Plan();
			plan.setShopId(TEST_SHOP_ID);
			planService.save(plan);
			TEST_PLAN_IDS[i] = plan.getId();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
//		final Plan[] plans = new Plan[TEST_PLANS_COUNT];
//		for (int i = 0; i < TEST_PLAN_IDS.length; ++i) {
//			final Plan p = new Plan();
//			p.setId(TEST_PLAN_IDS[i]);
//			plans[i] = p;
//		}
//		planRepository.delete(Arrays.asList(plans));
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetIndex() {
		final List<Plan> p1 = planRepository.findByShopId(TEST_SHOP_ID);
		System.out.println("@@@@@@PlanServiceTest.testSetIndex():"//
				+ planService.setIndex(TEST_PLAN_IDS[0], 2));
		final List<Plan> p2 = planRepository.findByShopId(TEST_SHOP_ID);
		System.out.println("@@@@@@PlanServiceTest.testSetIndex():" + p1 + " " + p2);
	}

	@Test
	public void testTurnOn() {
		final Long planId = TEST_PLAN_IDS[0];
		System.out.println("@@@@@@PlanServiceTest.testTurnOn():" + planService.turnOn(planId));
		final Plan p2 = planService.getPlan(planId);
		Assert.assertTrue(p2.isActive());
	}

	@Test
	public void testTurnOff() {
		final Long planId = TEST_PLAN_IDS[0];
		System.out.println("@@@@@@PlanServiceTest.testTurnOn():" + planService.turnOff(planId));
		final Plan p2 = planService.getPlan(planId);
		Assert.assertFalse(p2.isActive());
	}

	@Test
	public void testPreviewPlans() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlan() {
		System.out.println("@@@@@@PlanServiceTest.testGetPlan():" + planService.getPlan(1));
	}

	@Test
	public void testToggleOnOff() {
		final Long planId = TEST_PLAN_IDS[0];
		final Plan p1 = planService.getPlan(planId);
		System.out.println("@@@@@@PlanServiceTest.testTurnOn():" + planService.toggleOnOff(planId));
		final Plan p2 = planService.getPlan(planId);
		Assert.assertEquals(p1.isActive(), !p2.isActive());
	}

}
