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
import com.yunat.channel.client.ChannelClient;
import com.yunat.channel.request.info.SendTaskDateRequest;

@Component
public class ChannelSendHandler implements InvokerHandler {

	@Autowired
	private RemoteLoggerService httpLogService;

	private static final String SEND_TASKDATE = "send_taskdate";

	/**
	 * 获取已发送过任务的发送日期
	 * 
	 * @param taskId
	 * @param tenantId
	 * @param tenantPassword
	 * @param platEnum
	 * @return
	 */
	public BaseResponse<String> sendTaskDate(final String taskId, final String tenantId, final String tenantPassword,
			final PlatEnum platEnum) {
		return new RemoteHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(
				new RemoteCallable<BaseResponse<String>>() {
					@Override
					public BaseResponse<String> call() {
						SendTaskDateRequest req = new SendTaskDateRequest();
						req.setUserName(tenantId);
						req.setPassWord(tenantPassword);
						req.setTaskId(taskId);
						BaseResponse<String> response = ChannelClient.getInstance(AppEnum.CCMS, platEnum).excute(req);
						return response;
					}

					@Override
					public String getTaskId() {
						return taskId;
					}
				}, new InvokerLoggerWhenFailPolicy(SEND_TASKDATE, httpLogService), platEnum);
	}

}
