package com.yunat.ccms.rule.center.conf.planGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;

public class PlanGroupServiceTest {

	protected static ApplicationContext applicationContext;
	protected static PlanGroupService planGroupService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		planGroupService = applicationContext.getBean(PlanGroupService.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlanGroupOfShop() {
		final String shopId = "dagouzi";
		final PlanGroup planGroup = planGroupService.planGroupOfShop(shopId);
		System.out.println("@@@@@@PlanGroupServiceTest.testPlanGroupOfShop():" + planGroup);
	}

	@Test
	public void testPlanGroupOfShop_ShopIdIsNull() {
		try {
			final String shopId = null;
			final PlanGroup planGroup = planGroupService.planGroupOfShop(shopId);
			System.out.println("@@@@@@PlanGroupServiceTest.testPlanGroupOfShop():" + planGroup);
		} catch (final RuleCenterRuntimeException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			System.out.println("@@@@@@PlanGroupServiceTest.testPlanGroupOfShop_ShopIdIsNull():来到这里就对了");
		}
	}

	@Test
	public void testSaveSign() {
		final String shopId = "dagouzi";
		System.out.println("@@@@@@PlanGroupServiceTest.testSaveSign():"
				+ planGroupService.saveSign(shopId,
						"【数云】" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
	}

	@Test
	public void testSaveSign_signIsNull() {
		final String shopId = "dagouzi";
		System.out.println("@@@@@@PlanGroupServiceTest.testSaveSign():" + planGroupService.saveSign(shopId, null));
	}

	@Test
	public void testPreviewSign() {
		final String shopId = "dagouzi";
		System.out.println("@@@@@@PlanGroupServiceTest.testPreviewSign():" + planGroupService.previewSign(shopId));
	}
}
