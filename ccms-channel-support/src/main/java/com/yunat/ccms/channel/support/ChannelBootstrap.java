package com.yunat.ccms.channel.support;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.channel.external.taobao.handle.TaobaoClientInit;
import com.yunat.channel.util.PropertiesReader;

@Component
public class ChannelBootstrap {

	private Logger logger = LoggerFactory.getLogger(ChannelBootstrap.class);

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@PostConstruct
	public void init() {
		Map<String, String> config = new HashMap<String, String>();
		// 设置渠道channel-info,channel-service的值
		String channelQueryUrl = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_SERVICE_QUERY_URL);
		String channelCommandUrl = appPropertiesQuery
				.retrieveConfigValue(CCMSPropertiesEnum.CHANNEL_SERVICE_COMMAND_URL);
		config.put("info", channelQueryUrl);
		config.put("service", channelCommandUrl);
		logger.info("渠道channel_info地址设置为:" + channelQueryUrl);
		logger.info("渠道channel_service地址设置为:" + channelQueryUrl);

		String TOP_REST_Url = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.TOP_SERVICE_REST_URL);
		String TOP_REST_Appkey = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.TOP_CCMS_APPKEY);
		String TOP_REST_AppSecret = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.TOP_CCMS_APPSECRET);
		logger.info("TaobaoClient url地址设置为:" + TOP_REST_Url);
		logger.info("TaobaoClient Appkey地址设置为:" + TOP_REST_Appkey);
		logger.info("TaobaoClient Secret地址设置为:" + TOP_REST_AppSecret);
		config.put("Url", TOP_REST_Url);
		config.put("Appkey", TOP_REST_Appkey);
		config.put("Secret", TOP_REST_AppSecret);

		TaobaoClientInit.setConfig(TOP_REST_Url, TOP_REST_Appkey, TOP_REST_AppSecret);
		PropertiesReader.setKeyValue(config);
	}
}