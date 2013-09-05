package com.yunat.ccms.channel.support.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.utils.DESPlus;
import com.yunat.ccms.core.support.utils.MD5Utils;
import com.yunat.ccms.core.support.utils.UrlBuilder;
import com.yunat.ccms.core.support.vo.ControlerResult;

@Controller
@RequestMapping(value = "/tenant/*")
public class TenantInfoController {

	private static Logger logger = LoggerFactory.getLogger(TenantInfoController.class);

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@ResponseBody
	@RequestMapping(value = "/channel/recharge", method = RequestMethod.GET)
	public ControlerResult getChannelRechargeUrl() {
		String tenantId = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		String rechargeUrl = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_TENANT_RECHARGE_URL);
		String tenantInfo = getEncodedUserInfo(tenantId);
		UrlBuilder builder = new UrlBuilder(rechargeUrl);
		builder.addParam("userName", tenantId);
		builder.addParam("userInfo", tenantInfo);
		builder.addParam("time", "" + System.currentTimeMillis());
		String tenantRechargeUrl = builder.build();
		logger.info("用户{}充值链接为：{}", tenantId, tenantRechargeUrl);
		Map<String, Object> map = Maps.newHashMap();
		map.put("rechargeUrl", tenantRechargeUrl);
		return ControlerResult.newSuccess(map);
	}

	private String getEncodedUserInfo(String tenantId) {
		String userInfoDESPlus = "";
		try {
			String tenantIdmd5 = MD5Utils.EncodeByMd5(tenantId);
			DESPlus plus = new DESPlus();
			userInfoDESPlus = plus.encrypt("userName=" + tenantId + "&sign=" + tenantIdmd5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInfoDESPlus;
	}
}
