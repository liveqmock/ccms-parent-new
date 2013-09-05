/**
 *
 */
package com.yunat.ccms.tradecenter.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.support.cons.SendStatus;
import com.yunat.ccms.tradecenter.thread.OrdersThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.event.bus.handler.ConsumerEventType;
import com.yunat.ccms.event.bus.listener.ConsumerEvent;
import com.yunat.ccms.event.bus.listener.ConsumerEventListener;
import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.domain.CareStatusDomain;
import com.yunat.ccms.tradecenter.domain.DictDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.repository.MQlogisticsRepository;
import com.yunat.ccms.tradecenter.service.CareService;
import com.yunat.ccms.tradecenter.service.DictService;
import com.yunat.ccms.tradecenter.service.LogisticsService;
import com.yunat.ccms.tradecenter.support.bean.AssociatBeanList;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.thread.LogisticsThread;
import com.yunat.ccms.tradecenter.thread.ThreadApp;
import com.yunat.ccms.tradecenter.util.BeanUtil;

/**
 *物流MQ消息触发同城、派送、签收关怀的触发监听
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-17 下午06:29:48
 */
@Component
public class LogisticsMQListener implements ConsumerEventListener {

	private static Logger logger = LoggerFactory.getLogger(LogisticsMQListener.class);

	@Autowired
	private MQlogisticsRepository mqlogisticsRepository;

	@Autowired
	private LogisticsService logisticsService;

	@Autowired
	private CareService careService;

	@Autowired
	private DictService dictService;

    @Autowired
    private OrderService orderService;

	@Override
	public void handleEvent(ConsumerEvent event) throws Exception {

        logger.info("==物流监听处理开始type:{}", event.getType().getValue());

		ConsumerEventType type = event.getType();
		if(type.getValue().equals(ConsumerEventType.SHIPPING.getValue())){
			List<String> tidList = event.getMessage();
			logger.info("==收到物流消息，触发物流监听 ，收到订单ID【"+tidList.size()+"】个==");
			/*MQInfoLogDomain mqinfo = new MQInfoLogDomain();
			mqinfo.setType(1);
			mqinfo.setStatus(0);
			mqinfo.setRemark(ConsumerEventType.SHIPPING.getValue());
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

			List<Map<String, Object>> infoList = mqlogisticsRepository.queryStepinfoTmp(tidList);
			logger.info("==通过物流消息获取到的物流数据【"+infoList.size()+"】个==");



		    String SIGNED = "";
		    String REJECT = "";
		    String DELIVERY = "";
		    List<DictDomain> dictList = dictService.getByType(ConstantTC.LOGISTICS_KEY_WORDS);
		    for(DictDomain dict : dictList){
		    	if(dict.getCode().equals("SIGNED")){
		    		SIGNED = dict.getRemark();
		    		logger.info("==获取物流流转解析签收的关键字【"+SIGNED+"】==");
		    	}
		    	if(dict.getCode().equals("REJECT")){
		    		REJECT = dict.getRemark();
		    		logger.info("==获取物流流转解析拒签的关键字【"+REJECT+"】==");
		    	}
		    	if(dict.getCode().equals("DELIVERY")){
		    		DELIVERY = dict.getRemark();
		    		logger.info("==获取物流流转解析派送的关键字【"+DELIVERY+"】==");
		    	}
		    }

		    //解析物流流转数据
			Map<String,TransitstepinfoDomain> stepinfoMap = logisticsService.analysisTransitstepinfoList(infoList, orderMap, SIGNED, REJECT, DELIVERY);
			logger.info("==解析物流数据完成，获取物流数据【"+stepinfoMap.size()+"】个==");

			Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> arriveMap = new HashMap<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>>();
			Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> deliveryMap = new HashMap<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>>();
			Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> signMap = new HashMap<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>>();
            //发货列表
            Map<String,List<OrderDomain>> consignMap = new HashMap<String, List<OrderDomain>>();

			for(String tid : tidList){
				TransitstepinfoDomain domain = stepinfoMap.get(tid);
				OrderDomain order = orderMap.get(tid);
				CareStatusDomain care = careMap.get(tid);
				logger.info("==tid:[{}], domain:[{}] , order:[{}], care:[{}]", new Object[]{tid, domain==null?null:domain.getShippingStatus() + "," + domain.getLogisticsStatus(), order, care==null?null:care.getSignCareStatus()});
				if(domain!=null&&order!=null){
					if(domain.getShippingStatus()==3){//组装签收触发需要的数据
						if ((care==null || care.getSignCareStatus() == null || care.getSignCareStatus() != 1)) {
							if(signMap.get(order.getDpId())!=null){
								signMap.get(order.getDpId()).add(order, domain);
							}else{
								AssociatBeanList<OrderDomain,TransitstepinfoDomain> beanList = new AssociatBeanList<OrderDomain, TransitstepinfoDomain>();
								beanList.add(order, domain);
								signMap.put(order.getDpId(), beanList);
							}
						}

					}else if(domain.getShippingStatus()==2){//组装派送触发需要的数据

						if (care==null || care.getDeliveryCareStatus() == null || care.getDeliveryCareStatus()!=1) {
							if(deliveryMap.get(order.getDpId())!=null){
								deliveryMap.get(order.getDpId()).add(order, domain);
							}else{
								AssociatBeanList<OrderDomain,TransitstepinfoDomain> beanList = new AssociatBeanList<OrderDomain, TransitstepinfoDomain>();
								beanList.add(order, domain);
								deliveryMap.put(order.getDpId(), beanList);
							}
						}

					}else if(domain.getShippingStatus()==1){//组装同城触发需要的数据
						if (care==null|| care.getArriveCareStatus() == null || care.getArriveCareStatus()!=1) {
							if(arriveMap.get(order.getDpId())!=null){
								arriveMap.get(order.getDpId()).add(order, domain);
							}else{
								AssociatBeanList<OrderDomain,TransitstepinfoDomain> beanList = new AssociatBeanList<OrderDomain, TransitstepinfoDomain>();
								beanList.add(order, domain);
								arriveMap.put(order.getDpId(), beanList);
							}
						}
					}
                    //组装未同城的消息,触发同城关怀
                    else if (domain.getShippingStatus() == 4) {
                        if (care != null && care.getShipmentCareStatus() != null && care.getShipmentCareStatus() == SendStatus.MISS_LOGISTICS.getStatus() ) {
                            List<OrderDomain> orderDomains = consignMap.get(order.getDpId());
                            if (orderDomains == null) {
                                orderDomains = new ArrayList<OrderDomain>();
                                consignMap.put(order.getDpId(), orderDomains);
                            }

                            orderDomains.add(order);
                        }
                    }
				}

			}


			if(arriveMap.size()>0){
				logger.info("==触发同城过滤==初步判定需要过滤【"+arriveMap.size()+"】个数据");
				ThreadApp.getSimpleThreadPool().runInThread(new LogisticsThread(UserInteractionType.ARRIVED_CARE.getType(),arriveMap,careService));
			}else{
				logger.info("==该次消息不需触发同城过滤==");
			}
			if (deliveryMap.size() > 0) {
				logger.info("==触发派送过滤==初步判定需要过滤【"+deliveryMap.size()+"】个数据");
				ThreadApp.getSimpleThreadPool().runInThread(new LogisticsThread(UserInteractionType.DELIVERY_CARE.getType(),deliveryMap,careService));
			}else{
				logger.info("==该次消息不需触发派送过滤==");
			}
			if (signMap.size() > 0) {
				logger.info("==触发签收过滤==初步判定需要过滤【"+signMap.size()+"】个数据");
				ThreadApp.getSimpleThreadPool().runInThread(new LogisticsThread(UserInteractionType.SIGNED_CARE.getType(),signMap,careService));
			}else{
				logger.info("==该次消息不需触发签收过滤==");
			}

            if (consignMap.size() > 0) {
                logger.info("==通过物流触发发货关怀过滤==初步判定需要过滤【"+consignMap.size()+"】个数据");
                ThreadApp.getSimpleThreadPool().runInThread(new OrdersThread(UserInteractionType.SHIPMENT_CARE.getType(),consignMap,orderService));
            }else {
                logger.info("==该次消息没有需要通过物流触发的发货关怀==");
            }
			logger.info("==该次物流消息解析完成，收到订单ID【"+tidList.size()+"】个 ==");
			mqlogisticsRepository.deleteStepinfoTmp(tidList);
			//mqinfo.setStatus(1);
			//mqinfo.setUpdated(new Date());
			//mqInfoLogRepository.saveAndFlush(mqinfo);

		}
	}
}
