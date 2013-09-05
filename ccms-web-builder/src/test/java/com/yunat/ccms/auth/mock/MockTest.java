package com.yunat.ccms.auth.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.yunat.ccms.auth.login.LoginInfoHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
public class MockTest {

	@Test
	public void testToLHS() {
		System.out.println("@@@@@@MockTest.testToLHS():" + LoginInfoHolder.getCurrentUser());
	}

}