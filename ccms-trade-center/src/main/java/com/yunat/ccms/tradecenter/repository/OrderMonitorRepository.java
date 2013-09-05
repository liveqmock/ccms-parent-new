package com.yunat.ccms.tradecenter.repository;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.Today24HourOrderDataVO;

/**
 * 订单监控数据访问
 *
 * @author shaohui.li
 * @version $Id: OrderMonitorRepository.java, v 0.1 2013-7-23 下午03:13:36 shaohui.li Exp $
 */
public interface OrderMonitorRepository {

    /**
     * 获取今天付款的订单数以及金额
     *
     * @param dpId
     * @return
     */
    public Map<String,Object> getPayTimeIsTodayOrderNumAndAmount(String dpId);


    /**
     * 获取今天付款的订单，其子订单的大于1的主订单数
     *
     * @param dpId
     * @return
     */
    public Long getPayTimeIsTodayItemNumMoreOneOrderNum(String dpId);

    /**
     *
     *  获取今天付款的订单的商品数
     * @param dpId
     * @return
     */
    public Long getPayTimeIsTodayItemGoodsNums(String dpId);


    /**
     * 获取今天下单的订单数据
     *
     * @param dpId
     */
    public Long getCreatedIsTodayOrderNum(String dpId);


    /**
     *
     * 获取今天下单的订单 且 付款的订单数
     * @param dpId
     * @return
     */
    public Long getCreatedIsTodayPayedOrderNum(String dpId);


    /**
    *
    * 获取今天下单的订单 且 未付款的订单数
    * @param dpId
    * @return
    */
    public Long getCreatedIsTodayNotPayedOrderNum(String dpId);


    /**
     *获取获取今天下单的订单，未付款且催付的订单数
     *
     * @param dpId
     * @return
     */
    public Long getNotPayAndNotifyOrderNum(String dpId);


    /**
     * 获取今日下单24小时的清单金额
     * @param dpId
     * @param type
     * @return
     */
    public List<Today24HourOrderDataVO> getToday24HourOrderData(String dpId,boolean limitPayedOrder);


    /**
     * 根据不同的表和字段获取物流相关的订单数
     *
     * @param dpId
     * @param tableName
     * @param fieldName
     * @return
     */
    public Long getLogisNumByFieldName(String dpId,String fieldName);


    /**
     * 根据不同的表和字段获取订单数
     *
     * @param dpId
     * @param tableName
     * @param fieldName
     * @return
     */
    public Long getOrderNumByTableAndFieldName(String dpId,String tableName,String fieldName);


    /**
     * 获取今日确认收货的订单数
     *
     * @param dpId
     * @return
     */
    public Long getConfirmTodayOrderNum(String dpId);

}
