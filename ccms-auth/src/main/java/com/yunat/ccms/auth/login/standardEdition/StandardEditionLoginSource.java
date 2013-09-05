package com.yunat.ccms.auth.login.standardEdition;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.login.LoginSource;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.role.RoleRepository;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.auth.user.UserType;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.utility.sign.SignParameters;
import com.yunat.utility.sign.Signer;
import com.yunat.utility.sign.Signer.ResultType;
import com.yunat.utility.sign.SignerImpl;

/**
 * 标准版嵌套基础版的页面,从标准版到基础版时的"登录"
 * 
 * @author wenjian.liang
 */
@Component
public class StandardEditionLoginSource implements LoginSource {

	protected static final long DEFAULT_ROLE_ID = 100001L;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected UserRepository userRepository;
	@Autowired
	protected RoleRepository roleRepository;
	@Autowired
	protected AppPropertiesRepository appPropertiesRepository;

	@Override
	public boolean support(final HttpServletRequest request, final ProductEdition productEdition) {
		return request.getParameter("username") != null//
				&& request.getParameter("operator") != null//
				&& request.getParameter("timestamp") != null//
				&& request.getParameter("sign") != null//
				&& request.getParameter("usertype") != null;
	}

	protected void validate(final HttpServletRequest request) throws IllegalLoginParamException {
		final String requestMethod = request.getMethod().toUpperCase();
		final String username = request.getParameter("username");
		if (!StringUtils.hasText(username)) {
			logger.debug("标准版-基础版登录参数有误:username=" + username);
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		final String operator = request.getParameter("operator");
		if (!StringUtils.hasText(operator)) {
			logger.debug("标准版-基础版登录参数有误:operator=" + operator);
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		final String userType = request.getParameter("usertype");
		if (!StringUtils.hasText(userType)) {
			logger.debug("标准版-基础版登录参数有误:userType=" + userType);
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		final String timestampStr = request.getParameter("timestamp");
		if (!StringUtils.hasText(timestampStr)) {
			logger.debug("标准版-基础版登录参数有误:timestamp=" + timestampStr);
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		final long timestamp;
		try {
			timestamp = Long.parseLong(timestampStr);
		} catch (final NumberFormatException e) {
			logger.debug("标准版-基础版登录参数有误:timestamp=" + timestampStr);
			throw IllegalLoginParamException.fromLoginSource(this);
		}

		final String sign = request.getParameter("sign");
		if (!StringUtils.hasText(sign)) {
			logger.debug("标准版-基础版登录参数有误:sign=" + sign);
			throw IllegalLoginParamException.fromLoginSource(this);
		}

		final SignParameters signParameters = new SignParameters();
		signParameters.setHttpMethod(requestMethod);
		signParameters.setTimestamp(timestamp);

		final String secret = appPropertiesRepository.retrieveConfiguration(
				CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_group(),
				CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_name());
		signParameters.setSecret(secret);
		final Map<String, String> arguments = Maps.newHashMapWithExpectedSize(2);
		arguments.put("username", username);
		arguments.put("operator", operator);
		arguments.put("usertype", userType);
		signParameters.setArguments(arguments);

		final Signer signer = new SignerImpl();
		final ResultType result = signer.validateSign(signParameters, sign);
		if (result == ResultType.EXPIRED) {
			throw new IllegalLoginParamException("登录信息已超时。");
		} else if (result == ResultType.FAILURE) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}
	}

	@Override
	public User authentication(final HttpServletRequest request) throws IllegalLoginParamException {
		validate(request);

		final String operator = request.getParameter("operator");
		final User user = userRepository.queryByLoginName(operator);
		return user == null ? createUser(operator) : user;
	}

	protected User createUser(final String operator) {
		final User user = new User();
		user.setLoginName(operator);
		user.setRealName(operator);
		user.setPassword("");
		user.setDisabled(false);
		user.setUserType(UserType.BUILD_IN.getName());
		final Role basicRole = roleRepository.findOne(DEFAULT_ROLE_ID);
		user.setRoles(Collections.singleton(basicRole));
		try {
			return userRepository.save(user);
		} catch (final Exception e) {
			logger.warn("新增用户出错:" + operator, e);
			throw new IllegalLoginParamException("抱歉，登录出错。");
		}
	}

	@Override
	public String loginUrl(final ProductEdition productEdition) {
		return "#";
	}

	@Override
	public String getPlatformName() {
		return "标准版";
	}

	public static void main(final String[] args) {
		final SignParameters signParameters = new SignParameters();
		signParameters.setHttpMethod("GET");
		signParameters.setTimestamp(System.currentTimeMillis());
		signParameters.setSecret("5325ddba94c446beac479f67bc06a180");
		final Map<String, String> arguments = Maps.newHashMapWithExpectedSize(2);
		arguments.put("username", "qiushi");
		arguments.put("operator", "wj_huaat");
		signParameters.setArguments(arguments);

		final Signer signer = new SignerImpl();
		System.out.println("@@@@@@StandardEditionLoginSource.main():" + signParameters.getTimestamp());
		System.out.println(signer.generateSign(signParameters));
	}
}
