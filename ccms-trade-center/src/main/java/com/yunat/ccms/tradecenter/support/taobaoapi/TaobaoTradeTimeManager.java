package com.yunat.ccms.tradecenter.support.taobaoapi;

import java.util.Date;

/**
 * 获取交易最近时间
 *
 * @author shaohui.li
 * @version $Id: TradesSoldGetterManager.java, v 0.1 2013-8-14 下午06:16:55 shaohui.li Exp $
 */
public interface TaobaoTradeTimeManager {


    /**
     * 查询店铺订单的最近创建时间
     *
     * @param shopId
     * @return
     */
    public Date getLastTradeDateByCreated(String shopId,String sessionKey);


    /**
     *
     * 查询店铺订单的最近修改时间
     * @param shopId
     * @return
     */
    public Date getLastTradeDateByModified(String shopId,String sessionKey);


}
