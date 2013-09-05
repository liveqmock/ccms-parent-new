package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.AutoUrpayAndCareMonitoringVO;
import com.yunat.ccms.tradecenter.controller.vo.Today24HourOrderDataVO;
import com.yunat.ccms.tradecenter.controller.vo.TodayOrderChangeDataVO;
import com.yunat.ccms.tradecenter.controller.vo.TodayRealTimeOrderDataVO;
import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomain;

/**
 * 订单监控服务类
 *
 * @author shaohui.li
 * @version $Id: OrderMonitorService.java, v 0.1 2013-7-23 下午03:39:06 shaohui.li Exp $
 */
public interface OrderMonitorService {

    /**
     * 获取当日实时订单监控数据
     *
     * @param dpId
     * @return
     */
    public TodayRealTimeOrderDataVO getTodayRealTimeOrderData(String dpId);


    /**
     *
     * 获取当日24小时订单数据
     * @param dpId
     * @return
     */
    public Map<String,Today24HourOrderDataVO> getToday24HourOrderData(String dpId);


    /**
     *
     * 获取今日订单变更数据
     * @param dpId
     * @return
     */
    public TodayOrderChangeDataVO getTodayOrderChangeData(String dpId);
    
    
    /**
     * 获取 近30天订单流转监控信息
     * @param dpId
     * @return
     */
    public List<OrderFlowMonitoringDomain> findNearly30DaysOrderData(String dpId);
    
    /**
     * 返回所有 催付与关怀 开启状态
     * @param dpId
     * @return
     */
    public List<AutoUrpayAndCareMonitoringVO> findAutoUrpayAndCareMonitoringVO(String dpId);

}
