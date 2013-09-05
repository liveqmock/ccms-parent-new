package com.yunat.ccms.tradecenter.support.taobaoapi;

/**
 * 获取交易接口
 *
 * @author shaohui.li
 * @version $Id: TaobaoTradeGetManger.java, v 0.1 2013-8-15 上午11:11:14 shaohui.li Exp $
 */
public interface TaobaoTradeGetManger {

    /**
     * 判断订单是否已经支付
     *
     * @param tid
     * @return
     */
    public boolean isTradePayed(String shopId,Long tid,String sessionKey);

}
