package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.SmsQueueService;
import com.yunat.ccms.tradecenter.support.cons.FilterResultType;

/**
 *
 * @author 李卫林
 *
 */
@Component("careFilterManager")
public class CareFilterManager {

	/** 日志对象 **/
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	/** 短信队列服务 **/
	SmsQueueService smsQueueService;

	@Autowired
	/** 过滤管理器 **/
	private FilterManager filterManager;


	/**
	 *
	 * 过滤订单数据 以下方法可以抽取出来，需要结合关怀部分的功能
	 *
	 * @param orderList
	 * @return
	 */
	public OrderFilterResult filterOrder(List<OrderDomain> orderList, CareConfigDomain config) {

		OrderFilterResult filterResult = new OrderFilterResult();

		// 待发送列表
		List<OrderDomain> smsList = new ArrayList<OrderDomain>();

		// 去重的订单
		List<OrderDomain> repeatList = new ArrayList<OrderDomain>();

		// 第二天需要发送的订单
		List<OrderDomain> sendNextDayList = new ArrayList<OrderDomain>();

	    //永远不会发送的订单
	    List<OrderDomain> notSendList = new ArrayList<OrderDomain>();

	    //被过滤掉的订单
	    List<OrderDomain> filteredList = new ArrayList<OrderDomain>();

		// 根据配置获取通用过滤器
	    List<IFilter> filters = filterManager.getCareFilters(config);

		for (OrderDomain order : orderList) {
			try {
				FilterResult result = null;

				// 日常过滤器过滤
				for (IFilter f : filters) {
					result = f.doFiler(order, config);
					// 如果被过滤，不能放入待发送列表
					if (result.isFilter()) {
						logger.info("订单:[" + order.getTid() + "] 被过滤器:" + f.getFilterName() + " 过滤！");
						break;
					}
				}

				//需要发送
				if (!result.isFilter()) {
					smsList.add(order);
				}
				//第二天发送
				else if (result.isFilter() && FilterResultType.NEXT_DAY.getType().equals(result.getFilteredStatus())) {
					sendNextDayList.add(order);
				}
				//去重的订单
				else if (result.isFilter() && FilterResultType.REPEAT.getType().equals(result.getFilteredStatus())) {
				    repeatList.add(order);
				}
				//不需要发送的
				else if (result.isFilter() && FilterResultType.NOT_SEND.getType().equals(result.getFilteredStatus())) {
				    notSendList.add(order);
                }else{
                    //被过滤掉的订单
                    filteredList.add(order);
                }

			} catch (Exception ex) {
				logger.error("过滤订单:[" + order.getTid() + "],出现错误", ex);
			}
		}

		filterResult.setSendNextDayList(sendNextDayList);
		filterResult.setRepeatList(repeatList);
		filterResult.setNotSendList(notSendList);
		filterResult.setSmsList(smsList);
		filterResult.setFilteredList(filteredList);
		return filterResult;
	}
}
