package com.yunat.ccms.auth.bizdata;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;

import com.yunat.ccms.auth.login.LoginInfo;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.login.LoginInfoImpl;
import com.yunat.ccms.auth.login.VisitInfo;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.module.ModuleFromDB;
import com.yunat.ccms.module.ModuleRepository;

public class AclCreaterTest {

	static UserRepository userRepository;
	static ModuleRepository moduleRepository;
	static AclService aclService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		moduleRepository = context.getBean(ModuleRepository.class);
		aclService = context.getBean(AclService.class);
		userRepository = context.getBean(UserRepository.class);
		//模拟登录
		final User user = userRepository.findAll().get(0);
		_LoginInfoHolder._setLoginInfo(new LoginInfoImpl(new VisitInfo(ProductEdition.STANDARD, null), user));
	}

	@Test
	public void a() {
		ModuleFromDB module = new ModuleFromDB();
		module = moduleRepository.save(module);

		final Acl acl = aclService.readAclById(new ObjectIdentityImpl(module));
		System.out.println("@@@@@@AclCreaterTest.a():" + acl);
		System.out.println("@@@@@@AclCreaterTest.a():" + acl.getEntries());
	}

	private static class _LoginInfoHolder extends LoginInfoHolder {
		static void _setLoginInfo(final LoginInfo loginInfo) {
			setLoginInfo(loginInfo);
		}
	}
}
