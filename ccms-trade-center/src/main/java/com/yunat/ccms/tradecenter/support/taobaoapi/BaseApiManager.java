package com.yunat.ccms.tradecenter.support.taobaoapi;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.external.taobao.handler.CommonInvokerHandler;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

/**
 * User: weilin.li
 * Date: 13-7-31
 * Time: 下午1:40
 */
public class BaseApiManager {
    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private CommonInvokerHandler invokerHandler;

    public <T extends TaobaoResponse> T execTaobao(String shopId, TaobaoRequest<T> req){
        final AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, shopId);
        return ObjectUtils.equals(accessToken, null) ? null : invokerHandler.execute(req, accessToken.getAccessToken());
    }

    public <T extends TaobaoResponse> T callTaobao(String sessionKey, TaobaoRequest<T> req){
        return ObjectUtils.equals(sessionKey, null) ? null : invokerHandler.execute(req,sessionKey);
    }
}
