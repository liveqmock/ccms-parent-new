package com.yunat.ccms.tradecenter.support.taobaoapi.impl;

import com.taobao.api.domain.SubUserInfo;
import com.taobao.api.request.SellercenterSubusersGetRequest;
import com.taobao.api.response.SellercenterSubusersGetResponse;
import com.yunat.ccms.tradecenter.support.taobaoapi.BaseApiManager;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoSubuserManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-31
 * Time: 下午1:39
 */
@Component
public class TaobaoSubuserManagerImpl extends BaseApiManager implements TaobaoSubuserManager{

    @Override
    public List<SubUserInfo> getSubuserList(String nick, String shopId) {
        SellercenterSubusersGetRequest req=new SellercenterSubusersGetRequest();
        req.setNick(nick);
        SellercenterSubusersGetResponse response = execTaobao(shopId, req);

        List<SubUserInfo> subUserInfos = new ArrayList<SubUserInfo>();
        if (response != null) {
            subUserInfos =  response.getSubusers();
        }
        return subUserInfos;
    }
}
