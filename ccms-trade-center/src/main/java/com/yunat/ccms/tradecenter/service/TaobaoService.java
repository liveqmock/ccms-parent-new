package com.yunat.ccms.tradecenter.service;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.external.taobao.handler.CommonInvokerHandler;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

/**
 * 调用淘宝服务
 *
 * @author ming.peng
 * @date 2013-7-10
 * @since 4.2.0
 */
@Component("taobaoService")
public class TaobaoService {

	@Autowired
	private AccessTokenService accessTokenService;

	@Autowired
	private CommonInvokerHandler invokerHandler;

	public <T extends TaobaoResponse> T execTaobao(String shopId, TaobaoRequest<T> req){
		final AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, shopId);
		return ObjectUtils.equals(accessToken, null) ? null : invokerHandler.execute(req, accessToken.getAccessToken());
	}

}
