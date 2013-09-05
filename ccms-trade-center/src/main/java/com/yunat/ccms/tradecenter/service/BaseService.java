package com.yunat.ccms.tradecenter.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;

/**
 * 服务父类
 * @author 李卫林
 *
 */
public class BaseService {
	@Autowired
	private AppPropertiesRepository appPropertiesRepository;

	/**
	 * 获取当前登录用户名
	 * @return
	 */
	protected String getLoginName() {
		return LoginInfoHolder.getCurrentUser().getLoginName();
	}

	/**
	 * 获取ccms渠道用户名
	 * @return
	 */
	protected String getTenantId() {
		return appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_group(),
				CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_name());
	}

	/**
	 * 获取渠道密码
	 * @return
	 */
	protected String getTenantPassword() {
		return appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_group(),
				CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_name());
	}
}
