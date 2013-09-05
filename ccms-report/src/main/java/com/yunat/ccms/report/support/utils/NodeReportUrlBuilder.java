package com.yunat.ccms.report.support.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.utils.DESPlus;
import com.yunat.ccms.core.support.utils.HStringUtils;
import com.yunat.ccms.core.support.utils.MD5Utils;

@Component
public class NodeReportUrlBuilder {

	private static Logger logger = LoggerFactory.getLogger(NodeReportUrlBuilder.class);

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	public String reportUrl(Long campaignId, Long nodeId) {
		return appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_REPORT_SEND_RESULT_URL) + "user="
				+ appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID) + "&task="
				+ makeReportUrlsParamater(campaignId, nodeId);
	}

	private String makeReportUrlsParamater(Long campaignId, Long nodeId) {
		String currentTimeString = HStringUtils.formatDatetime(new Date());
		StringBuffer encrypt = new StringBuffer("campid=").append(campaignId.toString()).append("&nodeid=")
				.append(nodeId.toString()).append("&time=").append(currentTimeString).append("&sign=")
				.append(MD5Utils.EncodeByMd5(campaignId.toString() + nodeId + currentTimeString));
		String encryptStr = null;
		try {
			DESPlus plus = new DESPlus();
			encryptStr = plus.encrypt(encrypt.toString());
		} catch (Exception e) {
			logger.info("make report url happend Exception : {}", e.getMessage());
		}
		return encryptStr;
	}
}
