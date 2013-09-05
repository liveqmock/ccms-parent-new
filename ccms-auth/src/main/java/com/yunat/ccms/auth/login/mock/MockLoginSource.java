package com.yunat.ccms.auth.login.mock;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.login.LoginInfo;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.login.LoginInfoImpl;
import com.yunat.ccms.auth.login.LoginSource;
import com.yunat.ccms.auth.login.VisitInfo;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.role.RoleRepository;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.auth.user.UserType;
import com.yunat.ccms.core.support.cons.ProductEdition;

//:正式环境摘掉Component
//@Component
public class MockLoginSource implements LoginSource, InitializingBean {
	protected static final long DEFAULT_ROLE_ID = 100000L;

	@Autowired
	UserRepository userRepository;
	@Autowired
	protected RoleRepository roleRepository;

	@Override
	public boolean support(final HttpServletRequest request, final ProductEdition productEdition) {
		return true;
	}

	@Override
	public User authentication(final HttpServletRequest request) throws IllegalLoginParamException {
		return mock();
	}

	public User mock() {
		final List<User> users = userRepository.findAll();
		if (users == null || users.isEmpty()) {
			final User user = new User();
			user.setId(128L);
			user.setLoginName("mock");
			user.setRealName("mock");
			user.setUserType(UserType.TAOBAO.getName());
			user.setCreateTime(new Date());
			final Role basicRole = roleRepository.findOne(DEFAULT_ROLE_ID);
			user.setRoles(Collections.singleton(basicRole));
			return userRepository.save(user);
		}
		return users.get(0);
	}

	@Override
	public String loginUrl(final ProductEdition productEdition) {
		return "#";
	}

	@Override
	public String getPlatformName() {
		return "虚构";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final LoginInfo loginInfo = new LoginInfoImpl(new VisitInfo(ProductEdition.BASIC_L3, this), mock());
		_LoginInfoHolder._setLoginInfo(loginInfo);
		_LoginInfoHolder._setProductEdition(ProductEdition.BASIC_L3);
	}

	private static class _LoginInfoHolder extends LoginInfoHolder {
		static void _setProductEdition(final ProductEdition productEdition) {
			PRODUCT_EDITION_LOCAL.set(productEdition);
		}

		static void _setLoginInfo(final LoginInfo loginInfo) {
			setLoginInfo(loginInfo);
		}
	}
}
