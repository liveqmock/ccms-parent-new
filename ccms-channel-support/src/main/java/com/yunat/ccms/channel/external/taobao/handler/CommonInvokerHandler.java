package com.yunat.ccms.channel.external.taobao.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.yunat.ccms.channel.external.InvokerLoggerWhenFailPolicy;
import com.yunat.ccms.channel.external.taobao.TaobaoApiCaller;
import com.yunat.ccms.channel.external.taobao.TaobaoCallable;
import com.yunat.ccms.channel.external.taobao.TaobaoHttp;
import com.yunat.ccms.channel.support.service.RemoteLoggerService;
import com.yunat.channel.external.taobao.handle.TaobaoClientInit;

@Component
public class CommonInvokerHandler implements TaobaoApiCaller {

	@Autowired
	private RemoteLoggerService httpLogService;

	@Override
	public <T extends TaobaoResponse> T execute(final TaobaoRequest<T> request, final String sessionKey) {
		return new TaobaoHttp(CHANNEL_REMOTE_SEND_MTD_INITIAL_RETRY_INTERVAL).call(new TaobaoCallable<T>() {

			@Override
			public T call() throws Exception {
				return TaobaoClientInit.getInstance().execute(request, sessionKey);
			}
		}, new InvokerLoggerWhenFailPolicy(request.getApiMethodName(), httpLogService));
	}

}
