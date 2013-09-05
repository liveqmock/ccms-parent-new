package com.yunat.ccms.auth.repository;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;

public class UserManageRepositoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private UserRepository repository;

	@Before
	public void setUp() throws Exception {
		final ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		repository = context.getBean(UserRepository.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testQueryUser() {
		final List<User> user = repository.queryByUserType("built-in");
		System.out.println("@@@@@@UserManageRepositoryTest.testQueryUser():" + user);
	}

	@Test
	public void testFindById() {
		final User user = repository.findOne(1L);
		System.out.println("@@@@@@UserManageRepositoryTest.testFindById():" + user);
	}

}
