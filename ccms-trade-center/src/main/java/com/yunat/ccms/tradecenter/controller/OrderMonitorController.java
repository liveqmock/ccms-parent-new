package com.yunat.ccms.tradecenter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.AutoUrpayAndCareMonitoringVO;
import com.yunat.ccms.tradecenter.controller.vo.Today24HourOrderDataVO;
import com.yunat.ccms.tradecenter.controller.vo.TodayOrderChangeDataVO;
import com.yunat.ccms.tradecenter.controller.vo.TodayRealTimeOrderDataVO;
import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomain;
import com.yunat.ccms.tradecenter.service.OrderMonitorService;


/**
 *
 * 订单监控控制器
 *
 * @author shaohui.li
 * @version $Id: SendGoodsController.java, v 0.1 2013-7-8 下午04:09:12 shaohui.li
 *          Exp $
 */
@Controller
@RequestMapping(value = "/ordermonitor/orders/*")
public class OrderMonitorController {

    @Autowired
    /** 订单监控服务 **/
    private OrderMonitorService orderMonitorService;

	/**
	 *
	 * 发货查询
	 *
	 * @param sendGoodsQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderMonitor", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public ControlerResult orderMonitor(String dpId){
		Map<String, Object> result = Maps.newHashMap();

		TodayRealTimeOrderDataVO realTimeOrderDataVo = orderMonitorService.getTodayRealTimeOrderData(dpId);
		Map<String,Today24HourOrderDataVO> hourData = orderMonitorService.getToday24HourOrderData(dpId);
		TodayOrderChangeDataVO todayOrderChangeDataVO = orderMonitorService.getTodayOrderChangeData(dpId);

		result.put("realTimeOrderDataVo", realTimeOrderDataVo);
		result.put("hourData", hourData);
		result.put("todayOrderChangeDataVO", todayOrderChangeDataVO);
		return ControlerResult.newSuccess(result);
	}

	@ResponseBody
	@RequestMapping(value = "/orderMonitorMonth", method = RequestMethod.GET)
	public ControlerResult orderMonitorMonth(String dpId){
		Map<String, Object> result = Maps.newHashMap();
		List<OrderFlowMonitoringDomain> OrderFlowMonitoringList = orderMonitorService.findNearly30DaysOrderData(dpId);
		result.put("orderFlowMonitoriingData", OrderFlowMonitoringList);
		return ControlerResult.newSuccess(result);
	}

	@RequestMapping(value = "/urpayAndCareMonitoring", method = RequestMethod.GET)
	@ResponseBody
	public ControlerResult urpayAndCareMonitoring(@RequestParam("dpId") String dpId){
		Map<String, Object> result = Maps.newHashMap();
		List<AutoUrpayAndCareMonitoringVO> autoUrpayAndCareMonitoringVO =  orderMonitorService.findAutoUrpayAndCareMonitoringVO(dpId);
		result.put("autoUrpayAndCareMonitoringVO", autoUrpayAndCareMonitoringVO);
		return ControlerResult.newSuccess(result);
	}



}
