package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 * 催付订单数据查询接口
 *
 * @author shaohui.li
 * @version $Id: UrpayOrderRespository.java, v 0.1 2013-6-5 下午04:50:23 shaohui.li Exp $
 */
public interface UrpayOrderRespository {
    /**
     *获取当日未付款的订单且未催付的
     *+ 前一天未付款且需次日发送的订单
     *
     * @return
     */
    public List<OrderDomain> getNotPayedAndNotUrpayedOrders(String dpId);


    /**
     * 获取预关闭的订单
     *
     * 订单的范围：
     *
     * 下单时间在：以当前时间为结束点，向前推3天的时间为开始点，所有未付款，未催付的订单,且不包括聚划算订单
     *
     **/
    public List<OrderDomain> getPreclosedOrders(String dpId);


    /**
    *
    * 获取聚划算的订单数据
    *
    * 订单的范围：
    *   只选择当日订单，未付款且未未催付
    *   且订单来源为聚划算催付
    *
    * @return
    */
    public List<OrderDomain> getCheapOrders(String dpId);

}
