package com.yunat.ccms.channel.external.scs.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.base.enums.app.AppEnum;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.InvokerHandler;
import com.yunat.ccms.channel.external.InvokerLoggerWhenFailPolicy;
import com.yunat.ccms.channel.external.scs.RemoteCallable;
import com.yunat.ccms.channel.external.scs.RemoteHttp;
import com.yunat.ccms.channel.support.service.RemoteLoggerService;
import com.yunat.channel.business.sms.SMSDynamicZipUtil;
import com.yunat.channel.business.sms.SMSZipUtil;
import com.yunat.channel.client.ChannelClient;
import com.yunat.channel.request.service.sp.SMSDynamicRequest;
import com.yunat.channel.request.service.sp.SMSRequest;

@Component
public class SenderSMSHandler implements InvokerHandler {

	@Autowired
	private RemoteLoggerService httpLogService;

	private static final String SEND_TASK_SMS = "sendTaskSMS";
	private static final String SEND_TASK_SMS_BATCH = "sendTaskSMSBatch";

	public BaseResponse<String> sendTaskSMS(final Long gatewayId, final String username, final String password,
			final SMSZipUtil smsPack, final String sendContent, final String sendTime, final String taskId,
			final String campId, final String nodeId, final String fullName, final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<String>>() {
					@Override
					public BaseResponse<String> call() throws Exception {
						SMSRequest sr = new SMSRequest();
						sr.setContent(smsPack.getZipBytes());
						sr.setTaskId(taskId);
						sr.setRequestLen(smsPack.getTotal());// 请求总数
						sr.setActualLen(smsPack.getTotalSum());// 总发送数
						sr.setSmsContent(sendContent);

						sr.setSendTime(sendTime);
						sr.setGatewayId(gatewayId);
						sr.setUserName(username);
						sr.setPassWord(password);
						sr.setCampId(campId);
						sr.setNodeId(nodeId);
						sr.setFullName(fullName);
						BaseResponse<String> response = ChannelClient.getInstance(AppEnum.CCMS, platEnum).excute(sr);
						return response;
					}

					@Override
					public String getTaskId() {
						return taskId;
					}
				}, new InvokerLoggerWhenFailPolicy(SEND_TASK_SMS, httpLogService), platEnum);
	}

	public BaseResponse<String> sendTaskSMSBatch(final Long gatewayId, final String username, final String password,
			final SMSDynamicZipUtil smsDynamicPack, final String sendTime, final String taskId, final String campId,
			final String nodeId, final String fullName, final String userMsg, final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<String>>() {
					@Override
					public BaseResponse<String> call() throws Exception {
						SMSDynamicRequest sr = new SMSDynamicRequest();
						sr.setContent(smsDynamicPack.getZipBytes());
						sr.setTaskId(taskId);
						sr.setRequestLen(smsDynamicPack.getTotal());// 请求总数
						sr.setActualLen(smsDynamicPack.getTotalSum());// 总发送数
						sr.setSmsContent(userMsg);

						sr.setSendTime(sendTime);
						sr.setGatewayId(gatewayId);
						sr.setUserName(username);
						sr.setPassWord(password);
						sr.setCampId(campId);
						sr.setNodeId(nodeId);
						sr.setFullName(fullName);
						BaseResponse<String> response = ChannelClient.getInstance(AppEnum.CCMS, platEnum).excute(sr);
						return response;
					}

					@Override
					public String getTaskId() {
						return taskId;
					}
				}, new InvokerLoggerWhenFailPolicy(SEND_TASK_SMS_BATCH, httpLogService), platEnum);
	}

}
