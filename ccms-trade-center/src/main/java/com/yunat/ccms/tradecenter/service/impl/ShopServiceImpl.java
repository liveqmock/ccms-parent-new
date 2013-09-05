package com.yunat.ccms.tradecenter.service.impl;

import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.service.ShopService;

/**
 *
 * 店铺相关服务
 *
 * @author shaohui.li
 * @version $Id: ShopService.java, v 0.1 2013-6-7 上午11:33:51 shaohui.li Exp $
 */
@Service("shopService")
public class ShopServiceImpl implements ShopService{

    /**
     * 判断店铺是否有效
     *
     * @param dpId
     * @return
     */
    public boolean isValidShop(String dpId){
        //TODO 暂时未实现
        return true;
    }
}
