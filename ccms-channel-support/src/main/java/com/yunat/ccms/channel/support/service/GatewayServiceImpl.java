package com.yunat.ccms.channel.support.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.scs.handler.RetrievalGatewayHandler;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.vo.GatewayInfoResponse;

@Component
public class GatewayServiceImpl implements GatewayService {

	private static Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

	@Autowired
	private RetrievalGatewayHandler retrievalGatewayHandler;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<GatewayInfoResponse> retrievalAllGateways(String username, String password, ChannelType channelType,
			PlatEnum plat) {
		List<GatewayInfoResponse> allGateways = Lists.newArrayList();
		try {
			BaseResponse resp = retrievalGatewayHandler.getAllGateWayInfo(username, password, channelType,
					PlatEnum.taobao);

			if (resp.isSuccess()) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) resp.getRtnData();
				for (Map<String, Object> map : list) {
					GatewayInfoResponse gateway = new GatewayInfoResponse();
					gateway.setGatewayId(Long.valueOf(map.get("gatewayid").toString()));
					gateway.setGatewayName(map.get("gatewayname").toString());
					gateway.setGatewayPrice((Double) map.get("price") * 0.01);
					gateway.setGatewayBalance((Double) map.get("balance") * 0.01);
					gateway.setGatewayType(Integer.valueOf(map.get("gateway_type").toString()));
					gateway.setWordsLimit(Integer.valueOf(map.get("wordslimit").toString()));
					gateway.setUnsubscribeMessage(map.get("backorderinfo").toString());
					gateway.setMarkLength(Integer.valueOf(map.get("marklength").toString()));
					gateway.setSign(map.get("sign").toString());
					gateway.setNotice(map.get("notice").toString());
					allGateways.add(gateway);
				}
			} else {
				String errorMessage = resp.getErrMsg();
				logger.info("invoke method : getAllGateWayInfo has throws exception : {}", errorMessage);
			}

		} catch (Exception e) {
			logger.info("{}", e.getMessage());
		}

		return allGateways;
	}

	@Override
	public Map<String, Object> retrievalOneGateway(String username, String password, Long gatewayId) {
		// TODO Auto-generated method stub
		return null;
	}

}