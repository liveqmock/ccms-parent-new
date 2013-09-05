package com.yunat.ccms.auth.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.core.support.utils.MD5Utils;

/**
 * 这个类似乎没有用了.没有用登录框登录的方式
 * 
 * @author wenjian.liang
 */
//@Component
public class UsernamePasswordLoginSource implements LoginSource {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean support(final HttpServletRequest request, final ProductEdition productEdition) {
		return productEdition.id >= ProductEdition.STANDARD.id;
	}

	@Override
	public User authentication(final HttpServletRequest request) throws IllegalArgumentException {
		final String loginName = request.getParameter("loginName");
		final String password = MD5Utils.md5ToHexString(request.getParameter("password"));
		final User user = userRepository.queryByLoginNamePassword(loginName, password);
		if (user == null) {
			throw new IllegalLoginParamException("用户名和密码不匹配。");
		}
		return user;
	}

	@Override
	public String loginUrl(final ProductEdition productEdition) {
		return "";
	}

	@Override
	public String getPlatformName() {
		return "系统内登录";
	}
}
