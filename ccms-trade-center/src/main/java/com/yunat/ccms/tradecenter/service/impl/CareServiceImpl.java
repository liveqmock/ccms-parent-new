package com.yunat.ccms.tradecenter.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.CareStatusRepository;
import com.yunat.ccms.tradecenter.repository.SmsQueueRepository;
import com.yunat.ccms.tradecenter.service.CareService;
import com.yunat.ccms.tradecenter.service.VariableReplaceService;
import com.yunat.ccms.tradecenter.support.bean.AssociatBeanList;
import com.yunat.ccms.tradecenter.support.cons.CareFilterConditionType;
import com.yunat.ccms.tradecenter.support.cons.SendStatus;
import com.yunat.ccms.tradecenter.support.cons.TradeCenterCons;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.util.DateUtil;
import com.yunat.ccms.tradecenter.urpay.filter.CareFilterManager;
import com.yunat.ccms.tradecenter.urpay.filter.OrderFilterResult;

/**
 *
 * @author 李卫林
 *
 */
@Component("careService")
public class CareServiceImpl implements CareService {
    /** 日志对象 **/
    protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CareFilterManager careFilterManager;

	@Autowired
	private CareConfigRepository careConfigRepository;

    @Autowired
    VariableReplaceService variableReplaceService;

    @Autowired
    private CareStatusRepository careStatusRepository;

    @Autowired
    SmsQueueRepository smsQueueRepository;

	@Override
	public void cityCare(Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> dpIdToOrderShip) {
		logger.info("同城关怀开始，店铺数量：{}", dpIdToOrderShip.keySet().size());

		for (Map.Entry<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> entry : dpIdToOrderShip.entrySet()) {

			CareConfigDomain careConfigDomain = careConfigRepository.getByCareTypeAndDpIdAndIsOpen(UserInteractionType.ARRIVED_CARE.getType(), entry.getKey(), 1);

			if (careConfigDomain != null) {
				long startTime = System.currentTimeMillis();
				logger.info("处理店铺同城关怀开始，店铺Id：{},  订单数量：{}", entry.getKey(), entry.getValue().size());

				Map<OrderDomain, TransitstepinfoDomain> orderTransMap = entry.getValue().getAbMap();

				//做初始过滤
				List<OrderDomain> orderDomains = entry.getValue().getAs();
				//List<OrderDomain> filterOrderDomians = new ArrayList<OrderDomain>();
				List<OrderDomain> filterOrderDomians = arriveCareFilter(orderDomains, orderTransMap, careConfigDomain);

				//获得过滤结果
				orderDomains.removeAll(filterOrderDomians);
				OrderFilterResult orderFilterResult = careFilterManager.filterOrder(orderDomains, careConfigDomain);


				/*  处理过滤玩的订单  */

/*		        List<OrderDomain> noSendList = orderFilterResult.getNoSendList();
		        noSendList.addAll(filterOrderDomians);
		        //更新同城通知状态为不发送
		        for (OrderDomain orderDomain : noSendList) {
		        	careStatusRepository.inOrUpArriveCareStatus(orderDomain.getTid(), SendStatus.DONT_SEND.getStatus());
		        }*/

		        /*  第二天需要发送的订单  */
/*
		        List<OrderDomain> sendNextDayList = orderFilterResult.getSendNextDayList();
		        //更新同城通知状态为第二天发送
		        for (OrderDomain orderDomain : sendNextDayList) {
		        	careStatusRepository.inOrUpArriveCareStatus(orderDomain.getTid(), SendStatus.NEXT_DAY.getStatus());
		        }*/

				/* 处理不需要发送的订单，处理方式为标记为已发送  */
				List<OrderDomain> notSendNextDayList = orderFilterResult.getRepeatList();
				for (OrderDomain orderDomain : notSendNextDayList) {
					careStatusRepository.inOrUpArriveCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
				}


		        /*  处理马上要发送的订单  */
		        List<OrderDomain> orderDomainList = orderFilterResult.getSmsList();
		        //更新同城通知状态为已发送
		        for (OrderDomain orderDomain : orderDomainList) {
		        	careStatusRepository.inOrUpArriveCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
		        }

		        //去重，去重的手机号会被认为是已发送，所以先更新状态后去重
		        if (StringUtils.contains(careConfigDomain.getFilterCondition(), CareFilterConditionType.TODAY_HAS_SEND.getType())) {
		        	  orderDomainList = toRepeat(orderDomainList);
		        }

		        //保存入下发列表
		        List<SmsQueueDomain> smsList = convertOrder2SmsQueue(orderDomainList, orderTransMap, careConfigDomain);
		        smsQueueRepository.save(smsList);


				long endTime = System.currentTimeMillis();
				logger.info("处理店铺同城关怀结束，店铺Id：{},  花费时间{}ms", entry.getKey(), (endTime - startTime));
			} else {
				logger.info("处理店铺同城关怀失败，配置未开启，店铺Id：{}", entry.getKey());
			}


		}
	}

	@Override
	public void deliveryCare(Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> dpIdToOrderShip) {
		logger.info("派送关怀开始，店铺数量：{}", dpIdToOrderShip.keySet().size());

		for (Map.Entry<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> entry : dpIdToOrderShip.entrySet()) {

			CareConfigDomain careConfigDomain = careConfigRepository.getByCareTypeAndDpIdAndIsOpen(UserInteractionType.DELIVERY_CARE.getType(), entry.getKey(), 1);

			if (careConfigDomain != null) {
				logger.info("处理店铺派送关怀开始，店铺Id：{},  订单数量：{}", entry.getKey(), entry.getValue().size());
				long startTime = System.currentTimeMillis();
				Map<OrderDomain, TransitstepinfoDomain> orderTransMap = entry.getValue().getAbMap();

				//做初始过滤
				List<OrderDomain> orderDomains = entry.getValue().getAs();
				List<OrderDomain> filterOrderDomians = deliveryCareFilter(orderDomains, orderTransMap, careConfigDomain);

				//获得过滤结果
				orderDomains.removeAll(filterOrderDomians);
				OrderFilterResult orderFilterResult = careFilterManager.filterOrder(orderDomains, careConfigDomain);


				/*  处理不需要发送的订单  */

/*		        List<OrderDomain> noSendList = orderFilterResult.getNoSendList();
		        noSendList.addAll(filterOrderDomians);

		        //跟新派件通知状态为不发送
		        for (OrderDomain orderDomain : noSendList) {
		        	careStatusRepository.inOrUpDeliveryCareStatus(orderDomain.getTid(), SendStatus.DONT_SEND.getStatus());
		        }*/

		        /*  第二天需要发送的订单  */

	            //第二天发送的处理逻辑是 -- 未完成
/*		        List<OrderDomain> sendNextDayList = orderFilterResult.getSendNextDayList();

		        //跟新派件通知状态为第二天发送
		        for (OrderDomain orderDomain : sendNextDayList) {
		        	careStatusRepository.inOrUpDeliveryCareStatus(orderDomain.getTid(), SendStatus.NEXT_DAY.getStatus());
		        }*/

				/* 处理不需要发送的订单，处理方式为标记为已发送  */
				List<OrderDomain> notSendNextDayList = orderFilterResult.getRepeatList();
				for (OrderDomain orderDomain : notSendNextDayList) {
					careStatusRepository.inOrUpDeliveryCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
				}


		        /*  处理马上要发送的订单  */
		        List<OrderDomain> orderDomainList = orderFilterResult.getSmsList();
		        //跟新派件通知状态为已发送
		        for (OrderDomain orderDomain : orderDomainList) {
		        	careStatusRepository.inOrUpDeliveryCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
		        }

		        //去重，去重的手机号会被认为是已发送，所以先更新状态后去重
		        if (StringUtils.contains(careConfigDomain.getFilterCondition(), CareFilterConditionType.TODAY_HAS_SEND.getType())) {
		        	orderDomainList = toRepeat(orderDomainList);
		        }

		        //保存入下发列表
		        List<SmsQueueDomain> smsList = convertOrder2SmsQueue(orderDomainList, orderTransMap, careConfigDomain);
		        smsQueueRepository.save(smsList);

				long endTime = System.currentTimeMillis();
				logger.info("处理店铺派送关怀结束，店铺Id：{},  花费时间{}ms", entry.getKey(), (endTime - startTime));
			} else {
				logger.info("处理店铺派送关怀失败，配置未开启，店铺Id：{}", entry.getKey());
			}



		}
	}

	@Override
	public void signCare(Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> dpIdToOrderShip) {
		logger.info("签收关怀开始，店铺数量：{}", dpIdToOrderShip.keySet().size());

		for (Map.Entry<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> entry : dpIdToOrderShip.entrySet()) {

			CareConfigDomain careConfigDomain = careConfigRepository.getByCareTypeAndDpIdAndIsOpen(UserInteractionType.SIGNED_CARE.getType(), entry.getKey(), 1);

			if (careConfigDomain != null) {
				logger.info("处理店铺签收关怀开始，店铺Id：{},  订单数量：{}", entry.getKey(), entry.getValue().size());
				long startTime = System.currentTimeMillis();

				Map<OrderDomain, TransitstepinfoDomain> orderTransMap = entry.getValue().getAbMap();

				//做初始过滤
				List<OrderDomain> orderDomains = entry.getValue().getAs();
				List<OrderDomain> filterOrderDomians = signCareFilter(orderDomains, orderTransMap, careConfigDomain);

				//获得过滤结果
				orderDomains.removeAll(filterOrderDomians);
				logger.info("初始过滤后，订单数量:{}", orderDomains.size());
				OrderFilterResult orderFilterResult = careFilterManager.filterOrder(orderDomains, careConfigDomain);


/*				  处理不需要发送的订单

		        List<OrderDomain> noSendList = orderFilterResult.getNoSendList();
		        noSendList.addAll(filterOrderDomians);

		        //跟新签收通知状态为不发送
		        for (OrderDomain orderDomain : noSendList) {
		        	careStatusRepository.inOrUpSignCareStatus(orderDomain.getTid(), SendStatus.DONT_SEND.getStatus());
		        }*/

		        /*  第二天需要发送的订单  */

/*	            //第二天发送的处理逻辑是 -- 未完成
		        List<OrderDomain> sendNextDayList = orderFilterResult.getSendNextDayList();

		        //跟新签收通知状态为第二天发送
		        for (OrderDomain orderDomain : sendNextDayList) {
		        	careStatusRepository.inOrUpSignCareStatus(orderDomain.getTid(), SendStatus.NEXT_DAY.getStatus());
		        }*/

				/* 处理不需要发送的订单，处理方式为标记为已发送  */
				List<OrderDomain> notSendNextDayList = orderFilterResult.getRepeatList();
				logger.info("过滤器过滤后不再需要处理的订单个数：{}", notSendNextDayList.size());
				for (OrderDomain orderDomain : notSendNextDayList) {
					careStatusRepository.inOrUpSignCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
				}


		        /*  处理马上要发送的订单  */
		        List<OrderDomain> orderDomainList = orderFilterResult.getSmsList();
		        logger.info("过滤器过滤后需要发送短信的订单个数：{}", orderDomainList.size());

		        //跟新签收通知状态为已发送
		        for (OrderDomain orderDomain : orderDomainList) {
		        	careStatusRepository.inOrUpSignCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
		        }

		        //去重，去重的手机号会被认为是已发送，所以先更新状态后去重
		        if (StringUtils.contains(careConfigDomain.getFilterCondition(), CareFilterConditionType.TODAY_HAS_SEND.getType())) {
		        	orderDomainList = toRepeat(orderDomainList);
		        }

		        //保存入下发列表
		        List<SmsQueueDomain> smsList = convertOrder2SmsQueue(orderDomainList, orderTransMap, careConfigDomain);
		        smsQueueRepository.save(smsList);

				long endTime = System.currentTimeMillis();
				logger.info("处理店铺签收关怀结束，店铺Id：{},  花费时间{}ms", entry.getKey(), (endTime - startTime));
			} else {
				logger.info("处理店铺签收关怀失败，配置未开启，店铺Id：{}", entry.getKey());
			}

		}

	}

	@Override
	public void confirmCare(Map<String, List<OrderDomain>> dpIdOrderDomainsMap) {

		logger.info("确认收货关怀开始，店铺数量：{}", dpIdOrderDomainsMap.size());

		for (Map.Entry<String, List<OrderDomain>> entry : dpIdOrderDomainsMap.entrySet()) {

			CareConfigDomain careConfigDomain = careConfigRepository.getByCareTypeAndDpIdAndIsOpen(UserInteractionType.CONFIRM_CARE.getType(), entry.getKey(), 1);

			if (careConfigDomain != null) {
				logger.info("处理店铺确认收货关怀开始，店铺Id：{},  订单数量：{}", entry.getKey(), entry.getValue().size());
				long startTime = System.currentTimeMillis();

				List<OrderDomain> orderDomains = entry.getValue();

				//获得过滤结果
				OrderFilterResult orderFilterResult = careFilterManager.filterOrder(orderDomains, careConfigDomain);

				/*   处理被过滤掉的订单   */
				 List<OrderDomain> filteredList = orderFilterResult.getFilteredList();
		        //跟新签收通知状态为不发送
		        for (OrderDomain orderDomain : filteredList) {
		        	careStatusRepository.inOrUpConfirmCareStatus(orderDomain.getTid(), SendStatus.DONT_SEND.getStatus());
		        }

				/*   处理不需要发送的订单   */
		        List<OrderDomain> notSendList = orderFilterResult.getNotSendList();

		        //跟新签收通知状态为不发送
		        for (OrderDomain orderDomain : notSendList) {
		        	careStatusRepository.inOrUpConfirmCareStatus(orderDomain.getTid(), SendStatus.DONT_SEND.getStatus());
		        }

		        /*  第二天需要发送的订单  */

		        List<OrderDomain> sendNextDayList = orderFilterResult.getSendNextDayList();

		        //跟新签收通知状态已发送
		        for (OrderDomain orderDomain : sendNextDayList) {
		        	careStatusRepository.inOrUpConfirmCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
		        }

		        //插入队列表
		        List<SmsQueueDomain> nextDaySmsList = convertOrder2SmsQueue(sendNextDayList, careConfigDomain);
		        String nextTimeStr = new SimpleDateFormat("HH:mm:ss").format(careConfigDomain.getCareStartTime());
				Date nextDateTime = DateUtil.getNextTime(nextTimeStr);
		        for (SmsQueueDomain sms : nextDaySmsList) {
		        	sms.setSend_time(nextDateTime);
		        }

				smsQueueRepository.save(nextDaySmsList);


				/* 处理被重复过滤的订单  */
				List<OrderDomain> repeatList = orderFilterResult.getRepeatList();
				logger.info("过滤器过滤后不再需要处理的订单个数：{}", repeatList.size());
				for (OrderDomain orderDomain : repeatList) {
					careStatusRepository.inOrUpConfirmCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
				}


		        /*  处理马上要发送的订单  */
		        List<OrderDomain> orderDomainList = orderFilterResult.getSmsList();
		        logger.info("过滤器过滤后需要发送短信的订单个数：{}", orderDomainList.size());

		        //跟新签收通知状态为已发送
		        for (OrderDomain orderDomain : orderDomainList) {
		        	careStatusRepository.inOrUpConfirmCareStatus(orderDomain.getTid(), SendStatus.SEND.getStatus());
		        }

		        //去重，去重的手机号会被认为是已发送，所以先更新状态后去重
		        if (StringUtils.contains(careConfigDomain.getFilterCondition(), CareFilterConditionType.TODAY_HAS_SEND.getType())) {
		        	orderDomainList = toRepeat(orderDomainList);
		        }

		        //保存入下发列表
		        List<SmsQueueDomain> smsList = convertOrder2SmsQueue(orderDomainList, careConfigDomain);
		        smsQueueRepository.save(smsList);

				long endTime = System.currentTimeMillis();
				logger.info("处理店铺确认收货关怀结束，店铺Id：{},  花费时间{}ms", entry.getKey(), (endTime - startTime));
			} else {
				logger.info("处理店铺确认收货关怀失败，配置未开启，店铺Id：{}", entry.getKey());
			}

		}


	}

	/**
	 * 动态拼接短信内容
	 * @param orderList
	 * @param config
	 * @return
	 */
    private List<SmsQueueDomain> convertOrder2SmsQueue(List<OrderDomain> orderList, Map<OrderDomain, TransitstepinfoDomain> orderTransMap, CareConfigDomain config){
    	List<SmsQueueDomain> smsQueueList = new ArrayList<SmsQueueDomain>();

    	for (OrderDomain orderDomain : orderList) {

            SmsQueueDomain sms = new SmsQueueDomain();
            sms.setBuyer_nick(orderDomain.getCustomerno());
            sms.setCreated(new Date());
            sms.setDpId(orderDomain.getDpId());
            sms.setMobile(orderDomain.getReceiverMobile());
            //自动发送设置为系统管理员
            sms.setSend_user("system");
            sms.setTid(orderDomain.getTid());
            sms.setTrade_created(orderDomain.getCreated());
            sms.setType(config.getCareType());
            sms.setUpdated(new Date());
            sms.setGatewayId(config.getGatewayId().longValue());

            List<Object> list = new ArrayList<Object>();
            list.add(orderDomain);
            list.add(orderTransMap.get(orderDomain));
            String smsContent = variableReplaceService.replaceSmsContent(config.getSmsContent(), list);
            sms.setSms_content(smsContent);

            if (!"000".equals(smsContent)) {
            	smsQueueList.add(sms);
            }
    	}

    	return  smsQueueList;
    }

    /**
	 * 动态拼接短信内容 -- 订单
	 * @param orderList
	 * @param config
	 * @return
	 */
    private List<SmsQueueDomain> convertOrder2SmsQueue(List<OrderDomain> orderList, CareConfigDomain config){
    	List<SmsQueueDomain> smsQueueList = new ArrayList<SmsQueueDomain>();

    	for (OrderDomain orderDomain : orderList) {

            SmsQueueDomain sms = new SmsQueueDomain();
            sms.setBuyer_nick(orderDomain.getCustomerno());
            sms.setCreated(new Date());
            sms.setDpId(orderDomain.getDpId());
            sms.setMobile(orderDomain.getReceiverMobile());
            //自动发送设置为系统管理员
            sms.setSend_user("system");
            sms.setTid(orderDomain.getTid());
            sms.setTrade_created(orderDomain.getCreated());
            sms.setType(config.getCareType());
            sms.setUpdated(new Date());
            sms.setGatewayId(config.getGatewayId().longValue());

            List<Object> list = new ArrayList<Object>();
            list.add(orderDomain);
            String smsContent = variableReplaceService.replaceSmsContent(config.getSmsContent(), list);
            sms.setSms_content(smsContent);

            if (!"000".equals(smsContent)) {
            	smsQueueList.add(sms);
            }
    	}

    	return  smsQueueList;
    }

    /**
     * 去重
     * @param orderDomainList
     * @return
     */
    private List<OrderDomain> toRepeat(List<OrderDomain> orderDomainList) {
    	List<OrderDomain> repeatOrderDomainList = new ArrayList<OrderDomain>();

    	List<String> mobiles = new ArrayList<String>();
    	List<String> customernos = new ArrayList<String>();
    	for (OrderDomain orderDomain : orderDomainList) {
    		if (mobiles.contains(orderDomain.getReceiverMobile())) {
    			logger.info("订单被去重， 订单id：{}", orderDomain.getTid());
    			continue;
    		}

    		if (customernos.contains(orderDomain.getCustomerno())) {
    			logger.info("订单被去重， 订单id：{}", orderDomain.getTid());
    			continue;
    		}

    		mobiles.add(orderDomain.getReceiverMobile());
    		customernos.add(orderDomain.getCustomerno());
    		repeatOrderDomainList.add(orderDomain);
    	}

    	return repeatOrderDomainList;
    }


    /**
     * 同城默认过滤
     * @param orderList
     * @param orderTransMap
     * @param careConfigDomain
     * @return
     */
    private List<OrderDomain> arriveCareFilter(List<OrderDomain> orderList, Map<OrderDomain, TransitstepinfoDomain> orderTransMap, CareConfigDomain careConfigDomain) {
    	//被过滤掉的订单列表
    	List<OrderDomain> filterOrderDomains = new ArrayList<OrderDomain>();

    	for (OrderDomain orderDomain : orderList) {
    		TransitstepinfoDomain transitstepinfoDomain = orderTransMap.get(orderDomain);

            if (filterTransitstepinfo(careConfigDomain,transitstepinfoDomain.getArrivedTime(), orderDomain.getTid())) {
            	filterOrderDomains.add(orderDomain);
            }
    	}

    	return filterOrderDomains;

    }

    /**
     * 派送默认过滤
     * @param orderList
     * @param orderTransMap
     * @param careConfigDomain
     * @return
     */
    private List<OrderDomain> deliveryCareFilter(List<OrderDomain> orderList, Map<OrderDomain, TransitstepinfoDomain> orderTransMap, CareConfigDomain careConfigDomain) {
    	//被过滤掉的订单列表
    	List<OrderDomain> filterOrderDomains = new ArrayList<OrderDomain>();

    	for (OrderDomain orderDomain : orderList) {
    		TransitstepinfoDomain transitstepinfoDomain = orderTransMap.get(orderDomain);

            if (filterTransitstepinfo(careConfigDomain,transitstepinfoDomain.getDeliveryTime(), orderDomain.getTid())) {
            	filterOrderDomains.add(orderDomain);
            }
    	}

    	return filterOrderDomains;

    }

    /**
     * 签收默认过滤
     * @param orderList
     * @param orderTransMap
     * @param careConfigDomain
     * @return
     */
    private List<OrderDomain> signCareFilter(List<OrderDomain> orderList, Map<OrderDomain, TransitstepinfoDomain> orderTransMap, CareConfigDomain careConfigDomain) {
    	//被过滤掉的订单列表
    	List<OrderDomain> filterOrderDomains = new ArrayList<OrderDomain>();

    	for (OrderDomain orderDomain : orderList) {
    		TransitstepinfoDomain transitstepinfoDomain = orderTransMap.get(orderDomain);

            if (filterTransitstepinfo(careConfigDomain,transitstepinfoDomain.getSignedTime(), orderDomain.getTid())) {
            	filterOrderDomains.add(orderDomain);
            }
    	}

    	return filterOrderDomains;

    }

    private boolean filterTransitstepinfo(CareConfigDomain careConfigDomain, Date transDate, String tid) {
		//开始时间
		Date startDate = careConfigDomain.getStartDate();

		//结束时间
		Date endDate = null;
		Integer dateNumber = careConfigDomain.getDateNumber();
		if(dateNumber == null){
		    endDate = careConfigDomain.getEndDate();
		}else{
		    //总是开始，表示结束时间为100年后
		    if(dateNumber == 0){
		        endDate = DateUtils.addDays(startDate, 365 * 100);
		    }else{
		        endDate = careConfigDomain.getEndDate();
		    }
		}
		//如果同城时间在开始时间之间，或者在结束时间 + 1 之后,则此订单被过滤
		if(transDate.before(startDate)||transDate.after(endDate)){
			logger.info("关怀被过滤， 同城时间不在配置范围内，订单Id:{}, 关怀时间：{}", tid, transDate);
			return true;
		}

		//到达超过12小时不发送
		Date lastDate = DateUtils.addHours(transDate, TradeCenterCons.LOGIS_CARE_INTERVAL_HOUR);
		//如果过了最后期限, 被过滤掉
		if (lastDate.before(new Date())) {
			logger.info("关怀被过滤， 超出{}小时 ：订单id:{}", TradeCenterCons.LOGIS_CARE_INTERVAL_HOUR, tid);
			return true;
		}

		//如果日期隔天
		if (!DateUtils.isSameDay(transDate, new Date())) {

			// 如果配置过滤为隔天不发送， 被过滤掉
			if (careConfigDomain.getNotifyOption() == 0) {
				logger.info("关怀被过滤， 已经隔天，并且配置未不发送 ：订单id:{}", tid);
				return true;
			}
		}
		return false;
	}
}
