package com.yunat.ccms.tradecenter.support.taobaoapi;

import com.taobao.api.domain.GroupMember;

import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-31
 * Time: 上午10:58
 */
public interface TaobaoWangwangManager {
    /**
     * 根据店铺名（主账号）获取子账号列表
     *
     * @param shopName
     * @param shopId
     * @return
     */
    List<String> getMemberListByShop(String shopName, String shopId);
}
