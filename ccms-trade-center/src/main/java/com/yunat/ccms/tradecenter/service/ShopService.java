package com.yunat.ccms.tradecenter.service;

/**
 * 店铺相关服务
 *
 * @author shaohui.li
 * @version $Id: ShopService.java, v 0.1 2013-6-7 上午11:37:18 shaohui.li Exp $
 */
public interface ShopService {

    /**
     * 店铺是否有效
     *
     * @param dpId
     * @return
     */
    public boolean isValidShop(String dpId);
}
