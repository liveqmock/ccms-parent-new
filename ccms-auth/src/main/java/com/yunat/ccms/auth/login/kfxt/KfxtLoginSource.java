package com.yunat.ccms.auth.login.kfxt;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.login.LoginSource;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.auth.role.RoleRepository;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserRepository;
import com.yunat.ccms.auth.user.UserType;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.core.support.utils.MD5Utils;

@Component
public class KfxtLoginSource implements LoginSource {
	private static final String KFXT_LOGIN_URL = "https://xiaowei-ccms.fenxibao.com/";

	private static Logger logger = LoggerFactory.getLogger(KfxtLoginSource.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@Override
	public String loginUrl(final ProductEdition productEdition) {
		return KFXT_LOGIN_URL;
	}

	@Override
	public String getPlatformName() {
		return "客服系统";
	}

	@Override
	public boolean support(final HttpServletRequest request, final ProductEdition productEdition) {
		return request.getParameter("kfxt_appkey") != null;//从ccms3来的逻辑
	}

	protected static boolean loginTimeValidate(final String kfxt_timeOut) {
		final Date now = new Date();
		try {
			final Date timeout = new Date(Long.parseLong(kfxt_timeOut));
			if (timeout.after(now)) {
				logger.info("时间戳验证成功!");
				return true;
			} else {
				logger.info("时间超时,时间戳验证失败");
				return false;
			}
		} catch (final NumberFormatException e) {
			logger.info("超时时间解析错误" + kfxt_timeOut);
			return false;
		}
	}

	/**
	 * desc:验证从ucerter过来的参数签名是否合法
	 */
	protected boolean validate(final KFXTParams params) {
		final String kfxt_user_name = params.getKfxt_user_name();
		final String kfxt_time = params.getKfxt_time();
		final String kfxt_member_name = params.getKfxt_member_name();
		final String kfxt_sign = params.getKfxt_sign();
		try {
			final String password = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
			final String str = kfxt_member_name + /* password + */kfxt_time + kfxt_user_name;
			final String validate = MD5Utils.EncodeByMd5(str);
			logger.debug("ucerter过来的参数," + "kfxt_member_name:" + kfxt_member_name + "," + "password:" + password + ","
					+ "kfxt_time:" + kfxt_time + "," + "kfxt_user_name:" + kfxt_user_name);
			logger.debug("手动加密MD5:" + validate);
			logger.debug("ucerter的MD5:" + kfxt_sign);
			if (validate.equals(kfxt_sign) && loginTimeValidate(kfxt_time)) {
				return true;
			}
		} catch (final Exception e) {
			logger.info("ucenter验证参数错误：", e);
			e.printStackTrace();
		}
		return false;
	}

	protected String getParam(final HttpServletRequest request, final String paramName)
			throws IllegalLoginParamException {
		final String value = request.getParameter(paramName);
		if (value == null) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		return value;
	}

	protected String getParam(final Map<String, String> map, final String paramName) throws IllegalLoginParamException {
		final String value = map.get(paramName);
		if (value == null) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		return value;
	}

	protected KFXTParams parse(final HttpServletRequest request) throws IllegalLoginParamException {
		final KFXTParams params = new KFXTParams();
		params.setKfxt_appkey(getParam(request, "kfxt_appkey"));
		params.setKfxt_member_name(getParam(request, "kfxt_member_name"));
		params.setKfxt_sign(getParam(request, "kfxt_sign"));
		params.setKfxt_time(getParam(request, "kfxt_time"));
		params.setKfxt_user_name(getParam(request, "kfxt_user_name"));
		if (!validate(params)) {
			throw IllegalLoginParamException.fromLoginSource(this);
		}
		return params;
	}

	@Override
	public User authentication(final HttpServletRequest request) {
		final KFXTParams params = parse(request);

		User user = userRepository.queryByLoginNameUserType(params.getKfxt_member_name(), UserType.KFXT.getName());
		if (user == null) {
			user = new User();
			user.setLoginName(params.getKfxt_member_name());
			user.setPassword("");
			user.setRealName("huaat技术支持");
			user.setDisabled(false);
			user.setUserType(UserType.KFXT.getName());
			final Set<Role> roles = getRoles();
			user.setRoles(roles);
			userRepository.save(user);
			logger.info(params.getKfxt_member_name() + "用户保存成功");
		} else {
			if (Boolean.TRUE.equals(user.getDisabled())) {//XXX:客服系统用户会不会被停用?
				logger.error(user.getLoginName() + "在系统中已经被停用");
				throw new PreAuthenticatedCredentialsNotFoundException(user.getLoginName() + "在系统中已经被停用");
			}
		}
		return user;
	}

	/**
	 * @return
	 */
	protected Set<Role> getRoles() {
		final Set<Role> roles = new HashSet<Role>();
		final String defual_yunat_user_role = appPropertiesQuery
				.retrieveConfigValue(CCMSPropertiesEnum.CCMS_DEFAULT_YUNAT_USER_ROLE);

		final String[] role = defual_yunat_user_role.split(",");
		for (final String element : role) {
			if (element != null && element.length() > 0) {
				try {
					final Role basicRole = roleRepository.findOne(Long.valueOf(element));
					roles.add(basicRole);
				} catch (final NumberFormatException e) {
					logger.warn("客服系统用户权限配置(" + CCMSPropertiesEnum.CCMS_DEFAULT_YUNAT_USER_ROLE.getProp_name() + ","//
							+ CCMSPropertiesEnum.CCMS_DEFAULT_YUNAT_USER_ROLE.getProp_name() + ")有误:"//
							+ Arrays.toString(role) + ",已舍弃角色:" + element);
				}
			}
		}
		return roles;
	}

}
