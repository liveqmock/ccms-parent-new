package com.yunat.ccms.tradecenter.support.taobaoapi;

import com.taobao.api.domain.SubUserInfo;

import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-31
 * Time: 下午1:35
 */
public interface TaobaoSubuserManager {
    /**
     * 获取卖家子账号列表
     *
     *
     * @param nick
     * @param shopId
     * @return
     */
    List<SubUserInfo> getSubuserList(String nick, String shopId);
}
