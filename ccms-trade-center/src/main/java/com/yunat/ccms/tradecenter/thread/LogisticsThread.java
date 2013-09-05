/**
 *
 */
package com.yunat.ccms.tradecenter.thread;

import java.util.Map;

import org.apache.log4j.Logger;

import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.service.CareService;
import com.yunat.ccms.tradecenter.support.bean.AssociatBeanList;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-18 下午01:54:56
 */
public class LogisticsThread implements Runnable{

	/** 日志对象 **/
	protected Logger logger = Logger.getLogger(getClass());

	protected Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> map;

	protected Integer type;

	protected CareService careService;

	public LogisticsThread(Integer type,Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> map,CareService careService){
		this.type = type;
		this.map = map;
		this.careService = careService;
	}


	@Override
	public void run() {
		logger.info("==进入关怀过滤线程==类型【"+type+"】==");
		if (type == UserInteractionType.ARRIVED_CARE.getType()) {
			careService.cityCare(map);
			logger.info("触发同城关怀过滤程序");
		} else if (type == UserInteractionType.DELIVERY_CARE.getType()) {
			careService.deliveryCare(map);
			logger.info("触发派件关怀过滤程序");
		} else if (type == UserInteractionType.SIGNED_CARE.getType()) {
			careService.signCare(map);
			logger.info("触发签收关怀过滤程序");
		}
		logger.info("==结束关怀过滤线程==类型【"+type+"】==");

	}

}
