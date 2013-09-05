package com.yunat.ccms.tradecenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.service.GatewayService;
import com.yunat.ccms.channel.support.vo.GatewayInfoResponse;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;

@Controller
@RequestMapping(value = "/gateway/*")
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private AppPropertiesRepository appPropertiesRepository;

	@ResponseBody
	@RequestMapping(value = "/sms_list", method = {RequestMethod.POST, RequestMethod.GET})
	public List<GatewayInfoResponse> getGatewayList(){
		String channelName = appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_group(),
				CCMSPropertiesEnum.CCMS_TENANT_ID.getProp_name());

		String channelPwd = appPropertiesRepository.retrieveConfiguration(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_group(),
				CCMSPropertiesEnum.CCMS_TENANT_PASSWORD.getProp_name());

		List<GatewayInfoResponse> gatewayList = gatewayService.retrievalAllGateways(channelName, channelPwd, ChannelType.SMS, PlatEnum.taobao);

		return gatewayList;
	}

}
