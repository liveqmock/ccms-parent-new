package com.yunat.ccms.channel.external.taobao;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.yunat.ccms.channel.external.InvokerHandler;

public interface TaobaoApiCaller extends InvokerHandler {

	public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request, String sessionKey);
}
