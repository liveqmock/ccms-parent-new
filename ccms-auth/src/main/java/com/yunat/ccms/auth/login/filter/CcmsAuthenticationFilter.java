package com.yunat.ccms.auth.login.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.yunat.ccms.auth.exceptions.CcmsAuthenticationException;
import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.login.AuthenticationDetails;
import com.yunat.ccms.auth.login.LoginInfo;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.login.LoginSource;
import com.yunat.ccms.auth.role.RoleRepository;
import com.yunat.ccms.auth.springsecurity.CCMSUserDetails;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.auth.AuthCons;
import com.yunat.ccms.core.support.cons.ProductEdition;

public class CcmsAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements ApplicationContextAware {

	private static final ThreadLocal<User> USER_LOCAL = new ThreadLocal<User>();

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AppPropertiesRepository appPropertiesRepository;

	@Autowired
	private List<LoginSource> loginSources;

	public CcmsAuthenticationFilter() {
		super();
		setFilterProcessesUrl(AuthCons.LOGIN_INFO_PATH);
	}

	@Override
	protected String obtainPassword(final HttpServletRequest request) {
		final User user = getUser(request);
		return user.getPassword();
	}

	@Override
	protected String obtainUsername(final HttpServletRequest request) {
		//我们用user的id作为username,这是唯一的.loginName不一定唯一.
		final User user = getUser(request);
		return String.valueOf(user.getId());
	}

	protected User getUser(final HttpServletRequest request) throws CcmsAuthenticationException {
		User user = USER_LOCAL.get();
		if (user == null) {
			//获取他购买的ccms的版本
			final ProductEdition productEdition = getProductEdition(request);

			final LoginSource loginSource = getLoginSource(request, productEdition);
			if (loginSource == null) {
				throw new CcmsAuthenticationException("请重新登录", productEdition, "");
				//未登录,且没有登录来源.(这个怎么弄个?全让他登录框登录好了?)
			}

			_LoginInfoHolder._setLoginSource(loginSource);

			try {
				user = loginSource.authentication(request);
			} catch (final IllegalLoginParamException e) {
				//传上来的参数有问题,导致解析出错,需要让他重新登录
				throw new CcmsAuthenticationException("验证信息有误，请重新登录", productEdition,
						loginSource.loginUrl(productEdition));
			}
		}
		USER_LOCAL.set(user);
		return user;
	}

	/**
	 * @param request
	 * @return
	 */
	protected ProductEdition getProductEdition(final HttpServletRequest request) {
		return LoginInfoHolder.getProductEdition();
	}

	protected LoginSource getLoginSource(final HttpServletRequest request, final ProductEdition productEdition) {
		for (final LoginSource loginSource : loginSources) {
			if (loginSource.support(request, productEdition)) {
				return loginSource;
			}
		}
		return null;
	}

	public List<LoginSource> getLoginSources() {
		return loginSources;
	}

	public void setLoginSources(final List<LoginSource> loginSources) {
		this.loginSources = loginSources;
	}

	private static class _LoginInfoHolder extends LoginInfoHolder {
		public static void _setLoginInfo(final LoginInfo loginInfo, final HttpServletRequest request) {
			setLoginInfo(loginInfo, request);
		}

		public static void _setLoginSource(final LoginSource loginSource) {
			LOGIN_SOURCE_LOCAL.set(loginSource);
		}
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		if (loginSources == null) {
			final Map<String, LoginSource> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
					LoginSource.class);
			loginSources = new ArrayList<LoginSource>(map.values());
		}
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
			throws AuthenticationException {
		//TODO:上线前记得摘掉
		//如果链接中有登录参数,即使现在已登录,也直接使用链接中的登录参数重新登录.
		final ProductEdition productEdition = getProductEdition(request);
		final LoginSource loginSource = getLoginSource(request, productEdition);
		if (loginSource == null) {//链接中没有登录参数
			final LoginInfo loginInfo = LoginInfoHolder.getLoginInfo();
			if (loginInfo != null) {
				final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (!(authentication instanceof AnonymousAuthenticationToken)) {//已登录
					return authentication;
				}
			}
		}
		final Authentication authentication = super.attemptAuthentication(request, response);
		USER_LOCAL.set(null);
		return authentication;
//		return mock();
	}

	protected void saveLoginInfo(final HttpServletRequest request, final Authentication authentication) {
		final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		final CCMSUserDetails userDetails = (CCMSUserDetails) token.getPrincipal();
		final User user = userDetails.getUser();
		final Date loginTime = new Date();//暂定为"现在"吧
		final LoginInfo loginInfo = new LoginInfo() {
			@Override
			public User getUser() {
				return user;
			}

			@Override
			public Date getLoginTime() {
				return loginTime;
			}

			@Override
			public ProductEdition getProductEdition() {
				return LoginInfoHolder.getProductEdition();
			}
		};
		_LoginInfoHolder._setLoginInfo(loginInfo, request);
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authResult) throws IOException, ServletException {
		if (authResult instanceof UsernamePasswordAuthenticationToken) {
			saveLoginInfo(request, authResult);
		}
		super.successfulAuthentication(request, response, authResult);
	}

	@Override
	protected void setDetails(final HttpServletRequest request, final UsernamePasswordAuthenticationToken authRequest) {
		final WebAuthenticationDetails w = (WebAuthenticationDetails) authenticationDetailsSource.buildDetails(request);
		authRequest.setDetails(new AuthenticationDetails(w, getProductEdition(request)));
	}

	protected Authentication mock() {
		final User user = userRepository.findAll().get(0);
		user.setLoginName(MOCK_USER_NAME);

		return new UsernamePasswordAuthenticationToken(new CCMSUserDetails(user), "");
	}

	private static final String MOCK_USER_NAME = "虚构用户，用于测试";
}
