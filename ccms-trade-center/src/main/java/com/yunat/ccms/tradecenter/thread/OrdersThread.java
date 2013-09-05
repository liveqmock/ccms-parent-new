package com.yunat.ccms.tradecenter.thread;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.CareService;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 *  订单线程任务
 *
 * @author ming.peng
 * @date 2013-6-18
 * @since 4.1.0
 */
public class OrdersThread implements Runnable {

	/** 日志对象 **/
	protected Logger logger = Logger.getLogger(getClass());

	/**
	 * key:dpId  value:tids
	 */
	protected Map<String, List<OrderDomain>> map;

	protected Integer type;

	protected OrderService orderService;

	protected CareService careService;

	public OrdersThread(Integer type,Map<String, List<OrderDomain>> map,OrderService orderService){
		this.type = type;
		this.map = map;
		this.orderService = orderService;
	}

	public OrdersThread(Integer type, Map<String, List<OrderDomain>> map, CareService careService) {
		super();
		this.map = map;
		this.type = type;
		this.careService = careService;
	}

	public void run() {
		//发货关怀
		if (UserInteractionType.SHIPMENT_CARE.getType().equals(type)) {
			orderService.shippingNotice(map);
		}
		//确认收货关怀
		else if (UserInteractionType.CONFIRM_CARE.getType().equals(type)) {
			careService.confirmCare(map);
		}

	}
}
