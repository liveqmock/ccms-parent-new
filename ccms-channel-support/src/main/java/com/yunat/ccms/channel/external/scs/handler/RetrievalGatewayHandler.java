package com.yunat.ccms.channel.external.scs.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.base.enums.app.AppEnum;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.InvokerHandler;
import com.yunat.ccms.channel.external.InvokerLoggerWhenFailPolicy;
import com.yunat.ccms.channel.external.scs.RemoteCallable;
import com.yunat.ccms.channel.external.scs.RemoteHttp;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.service.RemoteLoggerService;
import com.yunat.channel.client.ChannelClient;
import com.yunat.channel.request.info.AllGateWayInfoRequest;
import com.yunat.channel.request.info.GateWayInfoRequest;

@Component
public class RetrievalGatewayHandler implements InvokerHandler {

	@Autowired
	private RemoteLoggerService httpLogService;

	private static final String GET_ALL_GATE_WAY_INFO = "getAllGateWayInfo";
	private static final String GET_GATE_WAY_INFO = "getGateWayInfo";

	public BaseResponse<List<Map<String, Object>>> getAllGateWayInfo(final String userName, final String password,
			final ChannelType channel, final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<List<Map<String, Object>>>>() {
					@Override
					public BaseResponse<List<Map<String, Object>>> call() {
						AllGateWayInfoRequest req = new AllGateWayInfoRequest();
						req.setUserName(userName);
						req.setPassWord(password);
						req.setChannelId(channel.getCode().longValue());
						BaseResponse<List<Map<String, Object>>> response = ChannelClient.getInstance(AppEnum.CCMS,
								platEnum).excute(req);

						return response;
					}

					@Override
					public String getTaskId() {
						return null;
					}
				}, new InvokerLoggerWhenFailPolicy(GET_ALL_GATE_WAY_INFO, httpLogService), platEnum);
	}

	public BaseResponse<Map<String, Object>> getGateWayInfo(final String username, final String password,
			final Long channelId, final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<Map<String, Object>>>() {
					@Override
					public BaseResponse<Map<String, Object>> call() {
						GateWayInfoRequest req = new GateWayInfoRequest();
						req.setUserName(username);
						req.setPassWord(password);
						req.setGatewayId(channelId);
						BaseResponse<Map<String, Object>> response = ChannelClient.getInstance(AppEnum.CCMS, platEnum)
								.excute(req);
						return response;
					}

					@Override
					public String getTaskId() {
						return null;
					}
				}, new InvokerLoggerWhenFailPolicy(GET_GATE_WAY_INFO, httpLogService), platEnum);
	}

}
