package com.yunat.ccms.configuration.variable;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;

@Component
class ApplicationVariableHolder implements ApplicationVariable, InitializingBean {

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	private String tenantId;
	private String tenantPassword;
	private String fileStorePath;

	@Override
	public void afterPropertiesSet() throws Exception {
		tenantId = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		tenantId = StringUtils.trimToEmpty(tenantId);
		Assert.isTrue(!StringUtils.isBlank(tenantId), "租户ID不能为空白");

		tenantPassword = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
		tenantPassword = StringUtils.trimToEmpty(tenantPassword);
		Assert.isTrue(!StringUtils.isBlank(tenantPassword), "租户密码不能为空白");

		fileStorePath = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_UPLOAD_DIR);
		fileStorePath = StringUtils.trimToEmpty(fileStorePath);
		Assert.isTrue(!StringUtils.isBlank(fileStorePath), "数据文件目录不能为空白");
		File file = new File(fileStorePath);
		if (file.exists()) {
			Assert.isTrue(file.isDirectory(), "数据文件路径不是目录：" + fileStorePath);
		} else {
			file.mkdirs();
		}

	}

	@Override
	public String getTenentId() {
		return tenantId;
	}

	@Override
	public String getTenentPassword() {
		return tenantPassword;
	}

	@Override
	public String getFileStorePath() {
		return fileStorePath;
	}

}
