package com.yunat.ccms.tradecenter.repository;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.queryobject.OrderQuery;

public interface IOrderRepository {

	/**
	 *
	 * @return
	 */
	List<OrderDomain> findOrders(OrderQuery orderQuery);

	/**
	 * 查询总记录条数
	 * @param orderQuery
	 */
	void findOrderCount(OrderQuery orderQuery);


	/**
	 * 查询待发货的订单记录
	 *
	 * @param request
	 * @return
	 */
	List<OrderDomain> findWaitSendGoodsOrders(SendGoodsQueryRequest request);

	/**
	 *
	 *查询查询待发货总记录条数
	 * @param request
	 * @return
	 */
	long findWaitSendGoodsOrdersCount(SendGoodsQueryRequest request);
	
	/**
	 * 根据条件 统计订单下单总数
	 * @param dpId
	 * @param created
	 * @param status 订单状态
	 * @return
	 */
	Integer countOrderCreateTime(String dpId, String created);
	
	/**
	 * 统计指定时间 订单付款总数
	 * @param dpId
	 * @param payTime
	 * @return
	 */
	Integer countOrderPayTime(String dpId, String created);
	
	/**
	 * 统计指定时间 订单发货总数
	 * @param dpId
	 * @param consignTime
	 * @return
	 */
	Integer countOrderConsignTime(String dpId, String created);
	
	/**
	 * 统计指定时间 订单确认总数
	 * @param dpId
	 * @param endTime
	 * @return
	 */
	Integer countOrderFinished(String dpId, String created, String status);
	
	/**
	 * 根据条件 统计订单 物流签收总数
	 * @param dpId
	 * @param created
	 * @param status 已付款订单状态
	 * @param shippingStatus 物流状态
	 * @return
	 */
	Integer countOrderAndTransitstepinfo(String dpId, String created, String status, Integer shippingStatus);
	
	
	/**
	 * 根据条件 统计订单 买家评价总数
	 * @param dpId
	 * @param created
	 * @param role	
	 * @return
	 */
	Integer countOrderAndTraderate(String dpId, String created);
	
	/**
	 * 根据条件 计算订单 付款间隔
	 * 例：avg（付款时间-下单时间）小时
	 * @param dpId
	 * @param created
	 * @param status 已付款订单状态
	 * @return
	 */
	Double countOrderPaymentIntervalTime(String dpId, String created);
	
	/**
	 * 根据条件 计算订单 发货间隔
	 * 例：avg（发货时间-付款时间）小时
	 * @param dpId
	 * @param created
	 * @param status 已发货订单状态
	 * @return
	 */
	Double countOrderSendGoodsIntervalTime(String dpId, String created);
	
	/**
	 * 根据条件 计算订单 签收间隔
	 * 例：avg（签收时间-发货时间）小时
	 * @param dpId
	 * @param created
	 * @param shippingStatus 已签收订单物流状态
	 * @return
	 */
	Double countOrderSignedIntervalTime(String dpId, String created, Integer shippingStatus);
	
	/**
	 * 根据条件 计算订单 确认收货间隔
	 * 例：avg（关闭时间-签收时间）小时
	 * @param dpId
	 * @param created
	 * @param status 已关闭订单状态
	 * @param shippingStatus 已签收订单物流状态
	 * @return
	 */
	Double countOrderFinishedIntervalTime(String dpId, String created, String status, Integer shippingStatus);
	
	
	/**
	 * 根据条件 计算订单 确认 评价间隔
	 * avg（评价时间-确认收货时间）小时
	 * @param dpId
	 * @param created
	 * param status 已评价订单状态
	 * @return
	 */
	Double countOrderTraderateIntervalTime(String dpId, String created);

    /**
     *
     * @return
     */
    List<OrderDomain> findLogisticsCareOpenOrders(Map<String, Integer> shopToLastConfigMap);
}
