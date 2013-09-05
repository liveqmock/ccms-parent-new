package com.yunat.ccms.module.auth;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModuleServiceTest {

	public static final long ID_1 = 1;//复用了
	static final String MODULE_NAME = "index";

	private final ModuleServiceForTest service = new ModuleServiceForTest();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		service.printCache();
	}

	@Test
	public void testGetModuleTypeById() {
		System.out.println("@@@@@@ModuleServiceTest.testGetModuleTypeById():" + service.getModuleTypeById(ID_1));
	}

	@Test
	public void testGetModuleById() {
		System.out.println("@@@@@@ModuleServiceTest.testGetModuleById():" + service.getModuleById(ID_1));
	}

	@Test
	public void testGetModuleByFullName() {
		System.out.println("@@@@@@ModuleServiceTest.testGetModuleByFullName():"
				+ service.getModuleByFullName(MODULE_NAME));
	}

}
