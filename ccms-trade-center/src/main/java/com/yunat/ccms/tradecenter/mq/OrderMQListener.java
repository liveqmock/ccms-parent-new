/**
 *
 */
package com.yunat.ccms.tradecenter.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.event.bus.handler.ConsumerEventType;
import com.yunat.ccms.event.bus.listener.ConsumerEvent;
import com.yunat.ccms.event.bus.listener.ConsumerEventListener;
import com.yunat.ccms.tradecenter.care.OrderAndPayCare;
import com.yunat.ccms.tradecenter.care.OrderCareThread;
import com.yunat.ccms.tradecenter.domain.CareStatusDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.repository.MQlogisticsRepository;
import com.yunat.ccms.tradecenter.service.CareService;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.support.cons.OrderStatusEnum;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.thread.OrdersThread;
import com.yunat.ccms.tradecenter.thread.ThreadApp;
import com.yunat.ccms.tradecenter.util.BeanUtil;

/**
 *订单MQ消息触发发货下单关怀的触发监听
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-17 下午06:29:12
 */
@Component
public class OrderMQListener implements ConsumerEventListener{

	private static Logger logger = LoggerFactory.getLogger(OrderMQListener.class);

	@Autowired
	private MQlogisticsRepository mqlogisticsRepository;

	@Autowired
	private OrderService orderService;


	@Autowired
	private CareService careService;

	@Autowired
	private OrderAndPayCare orderAndPayCare;

	@Override
	public void handleEvent(ConsumerEvent event) throws Exception {
		ConsumerEventType type = event.getType();
		if(type.getValue().equals(ConsumerEventType.ORDER.getValue())){
			List<String> tidList = event.getMessage();
			logger.info("==收到订单消息，触发订单监听 ，收到订单ID【"+tidList.size()+"】个==");
/*			MQInfoLogDomain mqinfo = new MQInfoLogDomain();
			mqinfo.setType(2);
			mqinfo.setStatus(0);
			mqinfo.setRemark(ConsumerEventType.ORDER.getValue());
			mqinfo.setCreated(new Date());
			mqinfo.setUpdated(new Date());
			mqinfo.setMsg(tidList.toString());
			mqinfo = mqInfoLogRepository.saveAndFlush(mqinfo);*/

			//组装订单数据
			Map<String,OrderDomain> orderMap = new HashMap<String,OrderDomain>();
			List<Map<String, Object>> orderList = mqlogisticsRepository.queryOrderTC(tidList);
			for(Map<String, Object> map : orderList){
				if(map.get("tid")!=null){
					OrderDomain order = BeanUtil.toBean(OrderDomain.class, map);
					orderMap.put(map.get("tid").toString(), order);
				}
			}
			logger.info("==查询出订单数据【"+orderMap.size()+"】个==");

			//组装订单状态数据
			Map<String,CareStatusDomain> careMap = new HashMap<String,CareStatusDomain>();
			List<Map<String, Object>> careStatusList = mqlogisticsRepository.queryCareStatus(tidList);
			for(Map<String, Object> map : careStatusList){
				if(map.get("tid")!=null){
					CareStatusDomain bean = BeanUtil.toBean(CareStatusDomain.class, map);
					careMap.put(map.get("tid").toString(), bean);
				}
			}
			logger.info("==查询出关怀状态数据【"+careMap.size()+"】个==");

			//发货列表
			Map<String,List<OrderDomain>> consignMap = new HashMap<String, List<OrderDomain>>();

			//下单列表
			Map<String,List<OrderDomain>> orderCareMap = new HashMap<String, List<OrderDomain>>();

			//确认收货列表
			Map<String, List<OrderDomain>> confirmCareMap = new HashMap<String, List<OrderDomain>>();

			for(String tid : tidList){
				OrderDomain domain = orderMap.get(tid);
				CareStatusDomain care = careMap.get(tid);
				if(domain!=null&&domain.getOrderStatus()!=null){
					//确认收货
					if (domain.getOrderStatus() == OrderStatusEnum.TRADE_FINISHED.getStatusValue()) {
						if (care == null || care.getConfirmCareStatus() == null  || care.getConfirmCareStatus() == 0) {

							//获得列表
							List<OrderDomain> finishedCareDomains = confirmCareMap.get(domain.getDpId());
							if (finishedCareDomains == null) {
								finishedCareDomains = new ArrayList<OrderDomain>();
								confirmCareMap.put(domain.getDpId(), finishedCareDomains);
							}

							finishedCareDomains.add(domain);
						}
					}
					//发货
					else if(domain.getOrderStatus()==OrderStatusEnum.WAIT_BUYER_CONFIRM_GOODS.getStatusValue()){

						if (domain.getConsignTime() != null && (care==null || care.getShipmentCareStatus() == null || care.getShipmentCareStatus() != 1)) {

							//获得列表
							List<OrderDomain> consignDomains = consignMap.get(domain.getDpId());
							if(consignDomains == null){
								consignDomains = new ArrayList<OrderDomain>();
								consignMap.put(domain.getDpId(), consignDomains);
							}

							consignDomains.add(domain);
						}
					}
					//订单在下单或者付款状态，且下单关怀的状态 未为催付或者 根本就没有下单状态，才会触发下单关怀
					else if(domain.getOrderStatus() == OrderStatusEnum.WAIT_BUYER_PAY.getStatusValue() || domain.getOrderStatus() == OrderStatusEnum.WAIT_SELLER_SEND_GOODS.getStatusValue()){

						if (care == null || care.getOrderCareStatus() == null  || care.getOrderCareStatus() == 0) {

							//获得列表
							List<OrderDomain> orderCareDomains = orderCareMap.get(domain.getDpId());
							if(orderCareDomains == null){
								orderCareDomains = new ArrayList<OrderDomain>();
								orderCareMap.put(domain.getDpId(), orderCareDomains);
	                        }

							orderCareDomains.add(domain);
						}

                    }

				}
			}
			if(consignMap.size() > 0){
				logger.info("==触发发货关怀过滤==初步判定需要过滤【"+consignMap.size()+"】个数据");
				ThreadApp.getSimpleThreadPool().runInThread(new OrdersThread(UserInteractionType.SHIPMENT_CARE.getType(),consignMap,orderService));
			}else{
				logger.info("==该次消息不需触发发货关怀过滤==");
			}

			if(orderCareMap.size() > 0){
                logger.info("==触发下单关怀过滤==初步判定需要过滤【"+orderCareMap.size()+"】个数据");
                ThreadApp.getSimpleThreadPool().runInThread(new OrderCareThread(orderCareMap,orderAndPayCare));
            } else{
				logger.info("==该次消息不需触发下单关怀过==");
			}

			if (confirmCareMap.size() > 0) {
				 logger.info("==触发确认收货关怀过滤==初步判定需要过滤【"+confirmCareMap.size()+"】个数据");
				 ThreadApp.getSimpleThreadPool().runInThread(new OrdersThread(UserInteractionType.CONFIRM_CARE.getType(),confirmCareMap, careService));
			} else{
				logger.info("==该次消息不需触发确认收货关怀过滤==");
			}

			logger.info("==该次订单消息处理完成，收到订单ID【"+tidList.size()+"】个 ==");
/*			mqinfo.setStatus(1);
			mqinfo.setUpdated(new Date());
			mqInfoLogRepository.saveAndFlush(mqinfo);*/

		}

	}

}
