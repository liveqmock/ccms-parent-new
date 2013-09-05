package com.yunat.ccms.tradecenter.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.OrderVO;
import com.yunat.ccms.tradecenter.controller.vo.PageVO;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsResultVO;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.queryobject.OrderQuery;

/**
 * 订单服务接口
 *
 * @author shaohui.li
 * @version $Id: OrderService.java, v 0.1 2013-6-4 下午08:21:58 shaohui.li Exp $
 */
public interface OrderService {

	/**
	 * 发货通知
	 * @param map
	 */
	void shippingNotice(Map<String, List<OrderDomain>> map);

    /**
     *根据店铺，买家查询最近2天所有付款的订单
     *
     * @param dpId：店铺id
     * @param buyer:买家
     * @return
     */
    List<OrderDomain> findPayedOrder(String dpId,String buyer,Date startDate,Date endDate);

    /**
     *获取当日未付款的订单且未催付的
     *+ 前一天未付款且需次日发送的订单
     *
     * @return
     */
    public List<OrderDomain> getNotPayedAndNotUrpayedOrders(String dpId);

    /**
     * 查询下单事务
     * @param orderQuery
     * @return
     */
    List<OrderVO> findWorkOrder(OrderQuery orderQuery);

    /**
     * 查询下单事务记录数
     * @param orderQuery
     * @return
     */
    PageVO findWorkOrderCount(OrderQuery orderQuery);


    /**
     * 获取预关闭的订单
     *
     * 订单的范围：
     *
     * 下单时间在：以当前时间为结束点，向前推3天的时间为开始点，所有未付款，未催付的订单,且不包括聚划算订单
     *
     **/
    List<OrderDomain> getPreclosedOrders(String dpId);


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
    List<OrderDomain> getCheapOrders(String dpId);


    /**
     * 查询待发货的订单数据
     *
     * @param request
     * @return
     */
    List<SendGoodsResultVO> querySendGoodsOrders(SendGoodsQueryRequest request);

    /**
     * 返回待发货的订单数据总条数据
     *
     * @param request
     * @return
     */
    long querySendGoodsOrdersCount(SendGoodsQueryRequest request);

}
