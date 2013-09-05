package com.yunat.ccms.channel.support.service;

import java.util.List;
import java.util.Map;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.vo.GatewayInfoResponse;

public interface GatewayService {
	List<GatewayInfoResponse> retrievalAllGateways(String tenantId, String tenantPassword, ChannelType channelType,
			PlatEnum plat);

	Map<String, Object> retrievalOneGateway(String tenantId, String tenantPassword, Long gatewayId);
}