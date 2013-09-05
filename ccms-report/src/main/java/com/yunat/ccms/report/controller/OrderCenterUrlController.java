package com.yunat.ccms.report.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.utils.MD5Utils;

/**
 * 订单中心URL：包含店铺诊断，店铺监控和订单中心三个功能的地址
 * 
 * @author kevin.jiang 2013-5-14
 */
@Controller
@RequestMapping(value = "/ucenter")
public class OrderCenterUrlController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	/**
	 * 店铺诊断
	 */
	@RequestMapping(value = "/shop_diagnosis", method = RequestMethod.GET)
	@ResponseBody
	public String getShopDiagnosisUrl() {

		String finalUrl = null;
		try {
			String shopId = "";
			String userName = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
			String url = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_SHOP_DIAGNOSIS_URL);

			String sign = genSign(userName, shopId);
			finalUrl = url.replace("{0}", userName).replace("{1}", sign);
			logger.info("店铺诊断URL:{}", finalUrl);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("caused by: " + e.getMessage());
			return null;
		}
		return finalUrl;
	}

	/**
	 * 订购中心-店铺诊断的sign生成
	 * 
	 * @param userName
	 *            租户名称
	 * @param shopId
	 *            店铺ID
	 * @return
	 */
	private String genSign(String userName, String shopId) {

		if (StringUtils.isEmpty(userName)) {
			userName = "";
		}

		if (StringUtils.isEmpty(shopId)) {
			shopId = "";
		}

		String str = "_" + userName + "_UDP_" + shopId + "_";
		String sign = str;

		try {
			sign = MD5Utils.EncodeByMd5(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 店铺监控
	 */
	@RequestMapping(value = "/shop_monitor", method = RequestMethod.GET)
	@ResponseBody
	public String getShopMonitorUrl() {

		String finalUrl = null;
		try {
			String userName = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
			String password = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
			String secretKey = appPropertiesQuery
					.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_ORDERCENTER_SECRET_KEY);
			String url = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_SHOP_MONITOR_URL);

			String key = genKey(secretKey, userName, password);
			finalUrl = url.replace("{0}", userName).replace("{1}", password).replace("{2}", key);
			logger.info("店铺监控URL:{}", finalUrl);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("caused by: " + e.getMessage());
			return null;
		}
		return finalUrl;
	}

	/**
	 * 订单中心
	 */
	@RequestMapping(value = "/order_center", method = RequestMethod.GET)
	@ResponseBody
	public String getOrderCenterUrl() {

		String finalUrl = null;
		try {
			String userName = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
			String password = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
			String secretKey = appPropertiesQuery
					.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_ORDERCENTER_SECRET_KEY);
			String url = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_ORDER_CENTER_URL);

			String key = genKey(secretKey, userName, password);
			finalUrl = url.replace("{0}", userName).replace("{1}", password).replace("{2}", key);
			logger.info("订单那中心URL:{}", finalUrl);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("caused by: " + e.getMessage());
			return null;
		}
		return finalUrl;
	}

	/**
	 * 店铺诊断和订单中心:key生成
	 * 
	 * @param secretKey
	 *            订购中心提供的一个固定的字符串
	 * @param userName
	 *            租户名称
	 * @param password
	 *            租户密码
	 * @return
	 */
	private String genKey(String secretKey, String userName, String password) {

		if (StringUtils.isEmpty(secretKey)) {

			secretKey = "";
		}
		if (StringUtils.isEmpty(userName)) {

			userName = "";
		}
		if (StringUtils.isEmpty(password)) {

			password = "";
		}
		return MD5Utils.EncodeByMd5(secretKey + userName + password);
	}
}
