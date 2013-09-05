package com.yunat.ccms.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.controller.vo.BuyerStatisVO;
import com.yunat.ccms.tradecenter.controller.vo.OrderItemVO;
import com.yunat.ccms.tradecenter.controller.vo.OrderVO;
import com.yunat.ccms.tradecenter.controller.vo.PageVO;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest;
import com.yunat.ccms.tradecenter.controller.vo.SendGoodsResultVO;
import com.yunat.ccms.tradecenter.controller.vo.SendLogVO;
import com.yunat.ccms.tradecenter.controller.vo.TradeMemoVO;
import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.domain.BuyerInteractionStatisticDomain;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.CustomerOrdersShipDomain;
import com.yunat.ccms.tradecenter.domain.MemberDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.OrderItemDomain;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.repository.AffairRepository;
import com.yunat.ccms.tradecenter.repository.BuyerInteractionStatisticRepository;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.CareStatusRepository;
import com.yunat.ccms.tradecenter.repository.CustomerOrdersShipRepository;
import com.yunat.ccms.tradecenter.repository.IOrderRepository;
import com.yunat.ccms.tradecenter.repository.MemberGradeRepository;
import com.yunat.ccms.tradecenter.repository.OrderItemRepository;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.SendLogRepository;
import com.yunat.ccms.tradecenter.repository.SmsQueueRepository;
import com.yunat.ccms.tradecenter.repository.TransitstepinfoRepository;
import com.yunat.ccms.tradecenter.repository.UrpayOrderRespository;
import com.yunat.ccms.tradecenter.service.BuyerStatisticService;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.service.TaobaoMemoService;
import com.yunat.ccms.tradecenter.service.VariableReplaceService;
import com.yunat.ccms.tradecenter.service.queryobject.OrderQuery;
import com.yunat.ccms.tradecenter.support.cons.CareFilterConditionType;
import com.yunat.ccms.tradecenter.support.cons.MemberType;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;
import com.yunat.ccms.tradecenter.support.cons.PropertiesNameEnum;
import com.yunat.ccms.tradecenter.support.cons.SendStatus;
import com.yunat.ccms.tradecenter.support.cons.TradeCenterCons;
import com.yunat.ccms.tradecenter.support.cons.TradeFromType;
import com.yunat.ccms.tradecenter.support.cons.UrpayQueryType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.util.ListUtil;
import com.yunat.ccms.tradecenter.urpay.filter.CareFilterManager;
import com.yunat.ccms.tradecenter.urpay.filter.OrderFilterResult;

/**
 *
 *OrderService 接口服务实现类
 *
 * @author shaohui.li
 * @version $Id: OrderServiceImpl.java, v 0.1 2013-6-4 下午08:23:55 shaohui.li Exp $
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService{

	/** 日志对象 **/
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    UrpayOrderRespository urpayOrderRespository;

    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private MemberGradeRepository memberGradeRepository;

    @Autowired
    private SendLogRepository sendLogRepository;

    @Autowired
    private BuyerInteractionStatisticRepository buyerInteractionStatisticRepository;

    @Autowired
    private CustomerOrdersShipRepository customerOrdersShipRepository;

    @Autowired
    private BuyerStatisticService buyerStatisticService;

	@Autowired
	private CareConfigRepository careConfigRepository;

	@Autowired
	private CareFilterManager careFilterManager;

    @Autowired
    private SmsQueueRepository smsQueueRepository;

    @Autowired
    private CareStatusRepository careStatusRepository;

    @Autowired
    private VariableReplaceService variableReplaceService;

    @Autowired
    PropertiesConfigManager propertiesConfigManager;

	@Autowired
	TransitstepinfoRepository transitstepinfoRepository;

    @Autowired
    AffairRepository affairRepository;

    @Autowired
    private TaobaoMemoService taobaoMemoService;

    /**
     * 发货通知
     */
	public void shippingNotice(Map<String, List<OrderDomain>> map) {
		logger.info("发货通知开始，店铺数量：{}", map.keySet().size());
		for (Map.Entry<String, List<OrderDomain>> entry : map.entrySet()) {
			CareConfigDomain careConfigDomain = careConfigRepository.getByCareTypeAndDpIdAndIsOpen(UserInteractionType.SHIPMENT_CARE.getType(), entry.getKey(), 1);
			if (ObjectUtils.notEqual(careConfigDomain, null)){// 当前店铺是否配置任务
				long startTime = System.currentTimeMillis();
				// 发货时间超出通知时间过滤设置
				List<OrderDomain> filterOrderDomians = beyondNoticeTimeFilter(entry.getValue(), careConfigDomain);
				entry.getValue().removeAll(filterOrderDomians);
				// 共同过滤
				OrderFilterResult orderFilterResult = careFilterManager.filterOrder(entry.getValue(), careConfigDomain);

				// 短信队列数据
				List<SmsQueueDomain> smsQueueList = new ArrayList<SmsQueueDomain>();

				// 处理不需要发送的订单，标记为已发送
				List<OrderDomain> notSendNextDayList = orderFilterResult.getRepeatList();

				// 对次日催付的需要修改状态, 标记为已发送【标记为第二天发送】, 入短信队列表
				List<OrderDomain> sendNextDayList = orderFilterResult.getSendNextDayList();
				List<OrderDomain> missLogisticsOrderDomains1 = convertOrder2SmsQueue(sendNextDayList, careConfigDomain, true, smsQueueList);

				// 处理马上要发送的订单，标记为已发送, 去重，入下发列表
				List<OrderDomain> orderDomainList = orderFilterResult.getSmsList();
				if (StringUtils.contains(careConfigDomain.getFilterCondition(), CareFilterConditionType.TODAY_HAS_SEND.getType())) {
					  orderDomainList = toRepeat(orderDomainList);  //去重的手机号会被认为是已发送，所以先更新状态后去重
				}
				List<OrderDomain> missLogisticsOrderDomains2 =  convertOrder2SmsQueue(orderDomainList, careConfigDomain, false, smsQueueList);

				//对于由于物流信息缺失而没有发出去的短息，更新状态为物流缺失，等待物流信息来之后更新
				missLogisticsOrderDomains1.addAll(missLogisticsOrderDomains2);

				Map<SendStatus, List<OrderDomain>> transactionMapData = new HashMap<SendStatus, List<OrderDomain>>();
				notSendNextDayList.addAll(sendNextDayList);
				notSendNextDayList.addAll(orderFilterResult.getSmsList()); // 不使用orderDomainList , 因为会根据手机号码去重，订单的状态还需要修改。
				transactionMapData.put(SendStatus.SEND, notSendNextDayList);
				transactionMapData.put(SendStatus.MISS_LOGISTICS, missLogisticsOrderDomains1);

				transaction(transactionMapData, smsQueueList);
		        long endTime = System.currentTimeMillis();
		        logger.info("处理店铺发货通知结束，店铺Id：{},  花费时间: {}ms", entry.getKey(), (endTime - startTime));
			} else {
				logger.info("处理店铺发货通知失败，配置未开启，店铺Id：{}", entry.getKey());
			}
		}
	}

	@Transactional(rollbackFor = {java.lang.Exception.class})
	public void transaction(Map<SendStatus, List<OrderDomain>> transactionMapData, List<SmsQueueDomain> smsQueueList){
		for (Map.Entry<SendStatus,List<OrderDomain>> entry : transactionMapData.entrySet()) {
			for (OrderDomain orderDomain : entry.getValue()) {
				careStatusRepository.inOrUpShipmentCareStatus(orderDomain.getTid(), entry.getKey().getStatus());
			}
		}
		if (!CollectionUtils.isEmpty(smsQueueList)){
    		smsQueueRepository.save(smsQueueList);// 保存队列
    	}
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
	 * 入短信发送队列
	 * @param orderList
	 * @param config
	 * @param flag
	 * @return
	 */
    private List<OrderDomain>  convertOrder2SmsQueue(List<OrderDomain> orderList, CareConfigDomain config, boolean flag, List<SmsQueueDomain> smsQueueList) {
        //物流信息缺失的订单
        List<OrderDomain> missLogisticsOrderDomains = new ArrayList<OrderDomain>();
		Calendar ca = null;
		if (flag) {
			ca = Calendar.getInstance();
			Calendar hoursEnd = Calendar.getInstance();
			hoursEnd.setTime(config.getCareEndTime());
			// 如果当前时间大于最后时间限制，设定为第二天开始发送时间，如果不是，则就为当天的开始发送时间
			if (ca.get(Calendar.HOUR_OF_DAY) >= hoursEnd.get(Calendar.HOUR_OF_DAY)){
				ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			}
			Calendar hoursStart = Calendar.getInstance();
			hoursStart.setTime(config.getCareStartTime());
			ca.set(Calendar.HOUR_OF_DAY, hoursStart.get(Calendar.HOUR_OF_DAY));
        	ca.set(Calendar.MINUTE, 0);
        	ca.set(Calendar.SECOND, 0);
		}
    	for (OrderDomain orderDomain : orderList) {
    		// 获取当前订单对应的物流信息。
    		TransitstepinfoDomain transitstepinfoDomain = transitstepinfoRepository.findOne(orderDomain.getTid());
			List<Object> list = new ArrayList<Object>(2);
	        list.add(orderDomain);
	        if (ObjectUtils.notEqual(transitstepinfoDomain, null)){
	        	list.add(transitstepinfoDomain);
	        }
    		String smsContent = variableReplaceService.replaceSmsContent(config.getSmsContent(), list);
    		if(StringUtils.equalsIgnoreCase(smsContent, "000")){
                logger.info("订单替换变量失败，订单:[" + orderDomain.getTid() + "],被过滤");

                if (ObjectUtils.equals(transitstepinfoDomain, null)) {
                    missLogisticsOrderDomains.add(orderDomain);
                }
                continue;
            }else{
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
	        	sms.setSend_time(flag ? ca.getTime() : null);
	            sms.setSms_content(smsContent);
	    		smsQueueList.add(sms);
    		}
    	}
        return missLogisticsOrderDomains;
	}

	/**
     * 发货时间超出通知时间过滤设置
     * @param orderList
     * @param careCon
     * @return
     */
    private List<OrderDomain> beyondNoticeTimeFilter(List<OrderDomain> orderList, CareConfigDomain careCon) {
    	//被过滤掉的订单列表
    	List<OrderDomain> filterOrderDomains = new ArrayList<OrderDomain>();
    	for (OrderDomain orderDomain : orderList) {
    		// 根据订单范围配置时间与订单发货时间比对过滤
    		if (!withinTimeRange(orderDomain.getConsignTime(), careCon.getStartDate(), careCon.getEndDate())) {
    			filterOrderDomains.add(orderDomain);
    			continue;
    		}
    		// 发货时间距现在时间超出12小时过滤掉
    		Calendar time = Calendar.getInstance();
    		time.setTime(orderDomain.getConsignTime());
    		time.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY) + 12);
    		if (!time.after(Calendar.getInstance())){
    			filterOrderDomains.add(orderDomain);
    		}
    	}
    	return filterOrderDomains;
    }

	public static boolean withinTimeRange(final Date date, final Date rangeStart, final Date rangeEnd){
		return date.getTime() >= rangeStart.getTime() && (rangeEnd==null ? true : date.getTime() <= rangeEnd.getTime());
	}

	@Override
    public List<OrderDomain> findPayedOrder(String dpId, String buyer,Date startDate,Date endDate) {
        return orderRepository.findPayedOrder(dpId, buyer,startDate,endDate);
    }

    @Override
    public List<OrderDomain> getNotPayedAndNotUrpayedOrders(String dpId) {
        return urpayOrderRespository.getNotPayedAndNotUrpayedOrders(dpId);
    }

	@Override
	public List<OrderVO> findWorkOrder(OrderQuery orderQuery) {
		List<OrderVO> orderVOs = new ArrayList<OrderVO>();

		//用户交互信息统计
		if (UrpayQueryType.DEPRECATED_URPAY.getType().equals(orderQuery.getUrpayStatus()) || UrpayQueryType.ADVICE_URPAY.getType().equals(orderQuery.getUrpayStatus())) {
			long s0 = System.currentTimeMillis();

			//统计3天的订单
			buyerStatisticService.staticBuyerInteraction(orderQuery.getDpId(), 3, new Date());
			long e0 = System.currentTimeMillis();
			logger.info("用户交互统计分析耗时：{}", (e0 - s0));
		}

		//long s0 = System.currentTimeMillis();
		//客服引导下单分析
/*		customerOrdersService.saveCustomerOrdersShipData();
		long e0 = System.currentTimeMillis();
		System.out.println("客服下单引导分析耗时："+ (e0 - s0));*/


		//获得订单列表
		long s1 = System.currentTimeMillis();
		List<OrderDomain> orderDomains = iOrderRepository.findOrders(orderQuery);
		long e1 = System.currentTimeMillis();
		logger.info("查询订单耗时：{}", (e1-s1));

		if (orderDomains.size() > 0) {

			long s2 = System.currentTimeMillis();
			//获得子订单列表
			List<String> tids = ListUtil.getPropertiesFromList(orderDomains, "tid");
			List<OrderItemDomain> orderItemDomains = orderItemRepository.findOrderItems(tids);
			long e2 = System.currentTimeMillis();
			logger.info("查询子订单耗时：{}", (e2-s2));

			long s3 = System.currentTimeMillis();
			//获得客户信息（等级）
			List<String> customernos = ListUtil.getPropertiesFromList(orderDomains, "customerno");
			List<MemberDomain> memberDomains = memberGradeRepository.getMembersByCustomers(orderQuery.getDpId(), customernos);
			long e3 = System.currentTimeMillis();
			logger.info("查询客户等级耗时：{}", (e3-s3));

			//统计已知用户交互信息统计
			if (!UrpayQueryType.DEPRECATED_URPAY.getType().equals(orderQuery.getUrpayStatus()) && !UrpayQueryType.ADVICE_URPAY.getType().equals(orderQuery.getUrpayStatus())) {
				long s8 = System.currentTimeMillis();

				buyerStatisticService.staticBuyerInteractionByCus(orderQuery.getDpId(), 3, new Date(), customernos);
				long e8 = System.currentTimeMillis();
				logger.info("制定用户交互统计分析耗时：{}", (e8 - s8));
			}

			long s4 = System.currentTimeMillis();
			//获得催付历史
			List<Integer> types = new ArrayList<Integer>();
			types.add(UserInteractionType.AUTO_URPAY.getType());
			types.add(UserInteractionType.CHEAP_URPAY.getType());
			types.add(UserInteractionType.MANUAL_URPAY.getType());
			types.add(UserInteractionType.MANUAL_WANGWANG_URPAY.getType());
			types.add(UserInteractionType.PRE_CLOSE_URPAY.getType());
			List<SendLogDomain> sendLogDomains = sendLogRepository.getSendLogByTids(tids, types);
			long e4 = System.currentTimeMillis();
			logger.info("查询下发列表耗时：{}" ,(e4-s4));

			long s5 = System.currentTimeMillis();
			//获得统计记录
			List<BuyerInteractionStatisticDomain> buyerInteractionStatisticDomains = buyerInteractionStatisticRepository.getByCustomernos(orderQuery.getDpId(), customernos);
			long e5 = System.currentTimeMillis();
			logger.info("查询统计记录耗时：{}" , (e5-s5));

			long s6 = System.currentTimeMillis();
			//获得订单隐藏记录
			List<CustomerOrdersShipDomain> customerOrdersShipDomains = customerOrdersShipRepository.getByTidIn(tids);
			long e6 = System.currentTimeMillis();
			logger.info("查询隐藏信息耗时：{}", (e6-s6));

			long s7 = System.currentTimeMillis();

            Map<String,TradeMemoVO> memoMap = taobaoMemoService.getTaobaoMemo(tids, orderQuery.getDpId());

			for (OrderDomain orderDomain : orderDomains) {
				OrderVO orderVO = new OrderVO();
				orderVOs.add(orderVO);
				orderVO.setBuyerMessage(orderDomain.getBuyerMessage());
				orderVO.setCreated(DateUtils.getStringDate(orderDomain.getCreated()));
				Date endDate = null;
				//如果来自聚划算，增加30分钟
				if (TradeFromType.JHS.getType().equals(orderDomain.getTradeFrom())) {
					endDate = DateUtils.addMinute(orderDomain.getCreated(), 30);
				}
				//其他情况下，增加3天
				else {
					endDate = DateUtils.addDay(orderDomain.getCreated(), 3);
				}
				orderVO.setEndTime(DateUtils.getStringDate(endDate));
				orderVO.setPayment(orderDomain.getPayment());
				orderVO.setPostFee(orderDomain.getPostFee());
				orderVO.setReceiverAddress(orderDomain.getReceiverAddress());
				orderVO.setReceiverDistrict(orderDomain.getReceiverDistrict());
				orderVO.setReceiverName(orderDomain.getReceiverName());
				orderVO.setReceiverPhone(orderDomain.getReceiverMobile());
				orderVO.setReceiverState(orderDomain.getReceiverState());
				orderVO.setReceiverCity(orderDomain.getReceiverCity());
				orderVO.setReceiverZip(orderDomain.getReceiverZip());
				orderVO.setSellerFlag(orderDomain.getSellerFlag());
				orderVO.setServiceStaff(orderDomain.getCustomerno());
				orderVO.setStatus(OrderStatus.getMessage(orderDomain.getStatus()));
				orderVO.setTid(orderDomain.getTid());
				orderVO.setTradeFrom(orderDomain.getTradeFrom());

				//子订单
				List<OrderItemVO> orderItemVOs = new ArrayList<OrderItemVO>();
				for (OrderItemDomain orderItemDomain : orderItemDomains) {
					if (orderDomain.getTid().equals(orderItemDomain.getTid())) {
						OrderItemVO orderItemVO = new OrderItemVO();
						orderItemVO.setPicPath(orderItemDomain.getPicPath());
						orderItemVO.setNum(orderItemDomain.getNum());
						orderItemVO.setPrice(orderItemDomain.getPrice());
						orderItemVO.setTitle(orderItemDomain.getTitle());
						orderItemVO.setDetailUrl(TradeCenterCons.ITEM_PREFIX + orderItemDomain.getNumIid());
						orderItemVO.setSkuPropertiesName(orderItemDomain.getSkuPropertiesName());
						orderItemVOs.add(orderItemVO);
					}
				}
				orderVO.setGoodsItems(orderItemVOs);

				//会员等级
				for (MemberDomain memberDomain : memberDomains) {
					if (orderDomain.getCustomerno().equals(memberDomain.getId().getCustomerNo())) {

						String gradeDes = "";
						if ("0".equals(memberDomain.getGrade())) {
							//为什么null异常不出来
							if (memberDomain.getTradeCount() != null && memberDomain.getTradeCount() > 0) {
								gradeDes = "未分级";
							} else {
								gradeDes = "新用户";
							}
						} else {
							gradeDes = MemberType.getMessage(memberDomain.getGrade());
						}

						orderVO.setGrade(gradeDes);
						break;
					}
				}
				//如果没有找到会员等级，则为新用户
				if (orderVO.getGrade() == null) {
					orderVO.setGrade("新用户");
				}

				//催付历史
				List<SendLogVO> sendLogVOs = new ArrayList<SendLogVO>();
				for (SendLogDomain sendLogDomain : sendLogDomains) {
					if (orderDomain.getTid().equals(sendLogDomain.getTid())) {
						SendLogVO sendLogVO = new SendLogVO();
						sendLogVO.setDate(DateUtils.getStringDate(sendLogDomain.getCreated()));

						//短信催付
						if (sendLogDomain.getType() <= 4) {
							sendLogVO.setManualUrpayStatus(1);
						}
						//旺旺催付
						else if (sendLogDomain.getType() == 5){
							sendLogVO.setManualUrpayStatus(2);
						}

						sendLogVO.setServiceStaff(sendLogDomain.getSendUser());
						sendLogVOs.add(sendLogVO);
					}
				}
				orderVO.setUrpayStatus1(sendLogVOs);

				//有过催付设置催付状态
				if (sendLogVOs.size() > 0) {
					orderVO.setUrpayStatus(1);
				} else {
					orderVO.setUrpayStatus(0);

					//用户交互信息统计
					for (BuyerInteractionStatisticDomain buyerStatisticDomain : buyerInteractionStatisticDomains) {
						if (orderDomain.getCustomerno().equals(buyerStatisticDomain.getCustomerno())) {
							BuyerStatisVO buyerStatisVO = new BuyerStatisVO();

							//有过付款或催付则不建议催付
							if (buyerStatisticDomain.getTradePayedCount() > 0 || buyerStatisticDomain.getUrpayCount() > 0) {
								buyerStatisVO.setAdvicesStatus(0);
							} else {
								buyerStatisVO.setAdvicesStatus(1);
							}

							//是否催付过
							if (buyerStatisticDomain.getUrpayCount() > 0) {
								buyerStatisVO.setMsg("3天内有进行过催付");
							} else {
								buyerStatisVO.setMsg("3天内未进行过催付");
							}
							buyerStatisVO.setCloseCount(buyerStatisticDomain.getTradeCloseCount());
							buyerStatisVO.setNoPayedCount(buyerStatisticDomain.getTradeNoPayedCount());
							buyerStatisVO.setPayedCount(buyerStatisticDomain.getTradePayedCount());
							buyerStatisVO.setTradeCount(buyerStatisticDomain.getTradeCount());
							orderVO.setUrpayStatus0(buyerStatisVO);
							break;
						}
					}
				}

				//隐藏信息查询
				boolean isHide = false;
                //boolean orderFollowup = false;
				for (CustomerOrdersShipDomain customerOrdersShipDomain : customerOrdersShipDomains) {
					if (orderVO.getTid().equals(customerOrdersShipDomain.getTid())) {
						isHide = customerOrdersShipDomain.getIsHide();
                        //orderFollowup = customerOrdersShipDomain.getOrderFollowup();
						break;
					}
				}
				orderVO.setIsHide(isHide);

                //跟进状态关联
                AffairDomain affairDomain = affairRepository.getAffairDomainByTid(orderDomain.getTid(), 2l);

                if (affairDomain != null) {
                    orderVO.setFollowupStatus(affairDomain.getStatus());
                    orderVO.setFollowupId(affairDomain.getPkid());
                }

                orderVO.setMemoVo(memoMap.get(orderDomain.getTid()));

			}
			long e7 = System.currentTimeMillis();
			logger.info("处理拼接逻辑耗时：{}", (e7-s7));
		}

		return orderVOs;
	}

	 @Override
	public PageVO findWorkOrderCount(OrderQuery orderQuery) {
		 iOrderRepository.findOrderCount(orderQuery);

		 //返回分页数据
		 PageVO pageVO = new PageVO();
		 pageVO.setPage(orderQuery.getCurrentPage());
		 pageVO.setTotal(orderQuery.getTotalItem());
		 pageVO.setTotalPage(orderQuery.getTotalPage());
         pageVO.setPageSize(orderQuery.getPageSize());
		 return pageVO;
	}

	/**
     * 获取预关闭的订单
     *
     * 订单的范围：
     *
     * 下单时间在：以当前时间为结束点，向前推3天的时间为开始点，所有未付款，未催付的订单,且不包括聚划算订单
     *
     **/
    @Override
    public List<OrderDomain> getPreclosedOrders(String dpId) {
        return urpayOrderRespository.getPreclosedOrders(dpId);
    }

    /**
    *
    * 获取聚划算的订单数据
    *
    * 订单的范围：
    *   只选择当日订单，未付款且未未催付
    *   且订单来源为聚划算催付
    *
    * @return
    */
    @Override
    public List<OrderDomain> getCheapOrders(String dpId) {
        return urpayOrderRespository.getCheapOrders(dpId);
    }

    /**
     *
     *查询待发货的订单数据
     * @see com.yunat.ccms.tradecenter.service.OrderService#querySendGoodsOrders(com.yunat.ccms.tradecenter.controller.vo.SendGoodsQueryRequest)
     */
    @Override
    public List<SendGoodsResultVO> querySendGoodsOrders(SendGoodsQueryRequest request) {
        List<SendGoodsResultVO> list = new ArrayList<SendGoodsResultVO>();
        long start = System.currentTimeMillis();
        List<OrderDomain> orderList = iOrderRepository.findWaitSendGoodsOrders(request);
        long end = System.currentTimeMillis();
        logger.info("查询订单耗时：{}", (end - start));
        if(orderList != null && !orderList.isEmpty()){
            //订单与子订单关系
            Map<String,List<OrderItemVO>> orderItemMap = getOrderItemMap(orderList);
            //买家昵称与会员等级关系
            Map<String,MemberDomain> memberMap = getMemberMap(orderList,request.getDpId());
            //订单与关怀的对应关系
            Map<String,List<SendLogDomain>> careMap = getCareLog(orderList);
            //获得订单隐藏记录
            Map<String,CustomerOrdersShipDomain> hideMap = getHideMap(orderList);

            Map<String,Integer> delayMap = getDelayTime(request.getDpId());

            //跟进事务map
            Map<String,AffairDomain> affairMap = getAffairMap(orderList);

            //买家留言
            List<String> tids = ListUtil.getPropertiesFromList(orderList, "tid");
            Map<String,TradeMemoVO> memoMap = taobaoMemoService.getTaobaoMemo(tids, orderList.get(0).getDpId());

            for(OrderDomain o : orderList){
                SendGoodsResultVO vo = new SendGoodsResultVO();
                vo.setBuyeyNick(o.getCustomerno());
                //发送历史
                vo.setCareLog(careMap.get(o.getTid()));
                //子订单
                vo.setGoodsItems(orderItemMap.get(o.getTid()));
                //会员等级处理
                MemberDomain memberDomain = memberMap.get(o.getCustomerno());
                String gradeDes = "新用户";
                if(memberDomain != null){
                    if ("0".equals(memberDomain.getGrade())) {
                        if (memberDomain.getTradeCount() != null && memberDomain.getTradeCount() > 0) {
                            gradeDes = "未分级";
                        } else {
                            gradeDes = "新用户";
                        }
                    } else {
                        gradeDes = MemberType.getMessage(memberDomain.getGrade());
                    }
                }
                //会员等级
                vo.setGrade(gradeDes);
                if(o.getStatus().equals(OrderStatus.WAIT_SELLER_SEND_GOODS.getStatus())){
                    vo.setOrderStatus(OrderStatus.WAIT_SELLER_SEND_GOODS.getMessage());
                }else{
                    vo.setOrderStatus(OrderStatus.SELLER_CONSIGNED_PART.getMessage());
                }
                //vo.setOrderStatus("等待卖家发货");
                vo.setPayment(o.getPayment());
                vo.setPayTime(DateUtils.getStringDate(o.getPayTime()));
                vo.setPostFee(o.getPostFee());
                vo.setReceiverAddress(o.getReceiverAddress());
                vo.setReceiverCity(o.getReceiverCity());
                vo.setReceiverDistrict(o.getReceiverDistrict());
                vo.setReceiverName(o.getReceiverName());
                vo.setReceiverPhone(o.getReceiverMobile());
                vo.setReceiverState(o.getReceiverState());
                vo.setReceiverZip(o.getReceiverZip());
                vo.setTid(o.getTid());
                vo.setTradeFrom(o.getTradeFrom());
                list.add(vo);
                //是否隐藏
                boolean isHide = false;
                boolean isFollowUp = false;
                if(hideMap.containsKey(o.getTid())){
                    isHide = hideMap.get(o.getTid()).getSendgoodsHide();
                    isFollowUp = hideMap.get(o.getTid()).getSendgoodsFollowUp();
                }
                vo.setIsHide(isHide);
                vo.setIsFollowUp(isFollowUp);
                //设置发送状态
                setSendStatus(vo,delayMap);

                //设置跟进状态
                if(affairMap.containsKey(o.getTid())){
                    vo.setFollowStatus(affairMap.get(o.getTid()).getStatus());
                    vo.setFollowId(affairMap.get(o.getTid()).getPkid().intValue());
                }else{
                    vo.setFollowStatus(-1);
                }
                //买家留言
                vo.setMemoVo(memoMap.get(o.getTid()));
            }
        }
        return list;
    }

    /**
     * 获取事务状态map
     *
     * @param orderList
     * @return
     */
    public Map<String,AffairDomain> getAffairMap(List<OrderDomain> orderList){
        Map<String,AffairDomain> affairMap = new HashMap<String,AffairDomain>();
        List<String> tids = ListUtil.getPropertiesFromList(orderList, "tid");
        List<AffairDomain> list = affairRepository.getAffairDomainByOids(tids, 3l);
        if(list != null){
            for (AffairDomain a : list) {
                affairMap.put(a.getTid(), a);
            }
        }
        return affairMap;
    }

    /**
     * 获取订单与订单是否隐藏对应
     *
     * @param orderList
     * @return
     */
    private Map<String,CustomerOrdersShipDomain> getHideMap(List<OrderDomain> orderList){
        Map<String,CustomerOrdersShipDomain> hideMap = new HashMap<String,CustomerOrdersShipDomain>();
        List<String> tids = ListUtil.getPropertiesFromList(orderList, "tid");
        List<CustomerOrdersShipDomain> customerOrdersShipDomains = customerOrdersShipRepository.getByTidIn(tids);
        if(customerOrdersShipDomains == null){
            return hideMap;
        }
        for (CustomerOrdersShipDomain customerOrdersShipDomain : customerOrdersShipDomains) {
            hideMap.put(customerOrdersShipDomain.getTid(), customerOrdersShipDomain);
        }
        return hideMap;
    }

    /**
     * 获取店铺延迟时间配置
     *
     * @param dpId
     * @return
     */
    private Map<String,Integer> getDelayTime(String dpId){
        //严重延迟
        int seriousDelay = ConstantTC.SERIOUS_DELAY;
        Integer seriousDelayObj = propertiesConfigManager.getInt(dpId, PropertiesNameEnum.SERIOUS_DELAY.getName());
        if(seriousDelayObj != null){
            seriousDelay = seriousDelayObj;
        }
        //一般延迟
        int commonDelay = ConstantTC.COMMON_DELAY;
        Integer commonDelayObj = propertiesConfigManager.getInt(dpId, PropertiesNameEnum.AVERAGE_DELAY.getName());
        if(commonDelayObj != null){
            commonDelay = commonDelayObj;
        }
        Map<String,Integer> result = new HashMap<String,Integer>();
        result.put("seriousDelay", seriousDelay);
        result.put("commonDelay", commonDelay);
        return result;
    }

    /**
     * 设置发送状态并计算延迟时间
     *
     * @param vo
     */
    private void setSendStatus(SendGoodsResultVO vo,Map<String,Integer> delayMap){
        String status = "";
        String depay = "";

        int seriousDelay = delayMap.get("seriousDelay");
        int commonDelay = delayMap.get("commonDelay");

        //付款时间
        Date payTime = DateUtils.getDateTime(vo.getPayTime());
        //当前时间
        Date curTime = new Date();
        //两个时间之差
        long time = Math.abs(curTime.getTime() - payTime.getTime());
        //转化为小时
        final int hour = (int) (time / (1000 * 3600));
        //分钟
        final int m = (int)((time - hour * 1000 * 3600) / (1000 * 60));
        if(hour >= seriousDelay){
            status = "严重延迟";
            depay = convertHour2Day(hour) + m + "分钟";
        }else if(hour >= commonDelay){
            status = "轻微延迟";
            depay = convertHour2Day(hour) + m + "分钟";
        }
        //发送状态
        vo.setSendStatus(status);
        //延迟时间
        vo.setDelayTime(depay);
    }

    private String convertHour2Day(int hour){
        int d = hour / 24;
        int h = hour % 24;
        return String.valueOf(d) + "天 " + String.valueOf(h) + "小时";
    }

    /**
     *获取订单与子订单关系
     *
     * @param orderList:订单列表
     * @return
     */
    private Map<String,List<OrderItemVO>> getOrderItemMap(List<OrderDomain> orderList){
        //订单与子订单关系
        Map<String,List<OrderItemVO>> orderItemMap = new HashMap<String,List<OrderItemVO>>();
        //获得子订单列表
        List<String> tids = ListUtil.getPropertiesFromList(orderList, "tid");
        long start = System.currentTimeMillis();
        List<OrderItemDomain> orderItemDomains = orderItemRepository.findOrderItems(tids);
        long end = System.currentTimeMillis();
        logger.info("查询子订单耗时：{}", (end - start));

        List<OrderItemVO> voList = new ArrayList<OrderItemVO>();
        for(OrderItemDomain o : orderItemDomains){
            OrderItemVO vo = new OrderItemVO();
            vo.setNum(o.getNum());
            vo.setPicPath(o.getPicPath());
            vo.setPrice(o.getPrice());
            vo.setDetailUrl(TradeCenterCons.ITEM_PREFIX + o.getNumIid());
            vo.setSkuPropertiesName(o.getSkuPropertiesName());
            vo.setTitle(o.getTitle());
            vo.setTid(o.getTid());
            voList.add(vo);
        }

        for(OrderItemVO o : voList){
            if(orderItemMap.containsKey(o.getTid())){
                orderItemMap.get(o.getTid()).add(o);
            }else{
                List<OrderItemVO> list = new ArrayList<OrderItemVO>();
                list.add(o);
                orderItemMap.put(o.getTid(), list);
            }
        }
        return orderItemMap;
    }

    /**
     * 买家昵称与会员等级关系
     *
     * @param orderList:订单列表
     * @param dpId：店铺id
     * @return
     */
    private Map<String,MemberDomain> getMemberMap(List<OrderDomain> orderList,String dpId){
        //买家昵称与会员等级关系
        Map<String,MemberDomain> memberMap = new HashMap<String,MemberDomain>();
        List<String> customernos = ListUtil.getPropertiesFromList(orderList, "customerno");
        long start = System.currentTimeMillis();
        List<MemberDomain> memberDomains = memberGradeRepository.getMembersByCustomers(dpId, customernos);
        if(memberDomains == null){
            return memberMap;
        }
        long end = System.currentTimeMillis();
        logger.info("查询客户等级耗时：{}", (end - start));
        for(MemberDomain m : memberDomains){
            memberMap.put(m.getId().getCustomerNo(), m);
        }
        return memberMap;
    }

    /**
     *
     * 获取订单对应的发送关怀的对应关系
     * @param orderList
     * @return
     */
    private Map<String,List<SendLogDomain>> getCareLog(List<OrderDomain> orderList){
        Map<String,List<SendLogDomain>> logMap = new HashMap<String,List<SendLogDomain>>();
        List<String> tids = ListUtil.getPropertiesFromList(orderList, "tid");
        long start = System.currentTimeMillis();
        List<Integer> types = new ArrayList<Integer>();
        types.add(UserInteractionType.MANUAL_SENDGOODS_SMS_CARE.getType());
        types.add(UserInteractionType.MANUAL_SENDGOODS_WANGWANG_CARE.getType());
        types.add(UserInteractionType.MANUAL_SENDGOODS_MOBILE_CARE.getType());
        List<SendLogDomain> sendLogDomains = sendLogRepository.getSendLogByTids(tids, types);
        if(sendLogDomains == null){
            return logMap;
        }
        long end = System.currentTimeMillis();
        logger.info("查询关怀历史耗时：{}", (end - start));
        for(SendLogDomain log : sendLogDomains){
            if(log.getType() == UserInteractionType.MANUAL_SENDGOODS_SMS_CARE.getType()){
                log.setSendType("短信");
            }else if(log.getType() == UserInteractionType.MANUAL_SENDGOODS_WANGWANG_CARE.getType()){
                log.setSendType("旺旺");
            }else if(log.getType() == UserInteractionType.MANUAL_SENDGOODS_MOBILE_CARE.getType()){
                log.setSendType("电话");
            }
            log.setSendTime(DateUtils.getStringDate(log.getCreated()));
            if(logMap.containsKey(log.getTid())){
                logMap.get(log.getTid()).add(log);
            }else{
                List<SendLogDomain> list = new ArrayList<SendLogDomain>();
                list.add(log);
                logMap.put(log.getTid(), list);
            }
        }
        return logMap;
    }

    @Override
    public long querySendGoodsOrdersCount(SendGoodsQueryRequest request) {
        return iOrderRepository.findWaitSendGoodsOrdersCount(request);
    }

}
