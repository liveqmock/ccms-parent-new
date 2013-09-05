package com.yunat.ccms.auth.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;

public class CcmsAuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler//
		implements AuthenticationSuccessHandler {
	private static final Pageable FIRST_USER_PAGE = new PageRequest(0, 1, Direction.ASC, "createTime");

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {
		final String contentType = "application/json";
		response.setContentType(contentType);
		final PrintWriter out = response.getWriter();
		final User user = LoginInfoHolder.getCurrentUser();
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("needLogin", false);
		map.put("loginUser", new LoginUser(user));
		//首日弹框
		final Page<User> userPage = userRepository.findAll(FIRST_USER_PAGE);
		final User firstUser = userPage.getContent().get(0);
		assert firstUser != null;
		final Date firstUserCreateTime = firstUser.getCreateTime();
		if (firstUserCreateTime.getTime() + TimeUnit.DAYS.toMillis(1) >= System.currentTimeMillis()) {
			map.put("pop", true);
		} else {
			map.put("pop", false);
		}

		objectMapper.writeValue(out, map);
		try {
			out.flush();
			out.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

}
