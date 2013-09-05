package com.yunat.ccms.tradecenter.support.taobaoapi.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.taobao.api.request.TradeGetRequest;
import com.taobao.api.response.TradeGetResponse;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.taobaoapi.BaseApiManager;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeGetManger;

/**
 *
 *判断订单是否付款
 * @author shaohui.li
 * @version $Id: TaobaoTradeGetMangerImpl.java, v 0.1 2013-8-15 上午11:19:10 shaohui.li Exp $
 */
@Service("taobaoTradeGetManger")
public class TaobaoTradeGetMangerImpl extends BaseApiManager implements TaobaoTradeGetManger{

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 当接口调用出错的情况下，认为该订单未付款
     *
     * @see com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeGetManger#isTradePayed(java.lang.String, java.lang.Long)
     */
    @Override
    public boolean isTradePayed(String shopId,Long tid,String sessionKey) {
        TradeGetRequest req=new TradeGetRequest();
        req.setFields("status,pay_time");
        req.setTid(tid);
        TradeGetResponse res = null;
        try{
            res = callTaobao(sessionKey, req);
        }catch(Exception ex){
            logger.error("获取店铺:[" + shopId + "] 订单信息出错",ex);
            return false;
        }
        if(res != null && res.isSuccess()){
            Date payTime = res.getTrade().getPayTime();
            if(payTime != null){
                return true;
            }else{
                String status = res.getTrade().getStatus();
                if(status.equals(OrderStatus.WAIT_BUYER_PAY.getStatus())){
                    return false;
                }
                return true;
            }
        }else{
            if(res != null){
                logger.info("获取店铺:[" + shopId + "]最近下单时间出错,错误代码:" + res.getErrorCode() + ",错误原因:" + res.getMsg());
            }
            return false;
        }
    }
}
