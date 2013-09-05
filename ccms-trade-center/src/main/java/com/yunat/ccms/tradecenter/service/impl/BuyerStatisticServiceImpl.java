package com.yunat.ccms.tradecenter.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.BuyerInteractionStatisticDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.ShopStatisConfigDomain;
import com.yunat.ccms.tradecenter.repository.BatchRepository;
import com.yunat.ccms.tradecenter.repository.BuyerInteractionStatisticRepository;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.repository.SendLogRepository;
import com.yunat.ccms.tradecenter.repository.ShopStatisConfigRepository;
import com.yunat.ccms.tradecenter.service.BuyerStatisticService;
import com.yunat.ccms.tradecenter.support.cons.TradeCenterCons;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.util.DateUtil;
import com.yunat.ccms.tradecenter.support.util.ListUtil;
import com.yunat.ccms.tradecenter.support.util.TimeUtils;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;

@Component("buyerStatisticService")
public class BuyerStatisticServiceImpl implements BuyerStatisticService {

	private static Logger logger = LoggerFactory.getLogger(BuyerStatisticServiceImpl.class);

	@Autowired
	private BuyerInteractionStatisticRepository buyerInteractionStatisticRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private ShopStatisConfigRepository shopStatisConfigRepository;

	@Autowired
	private SendLogRepository sendLogRepository;

	@Override
	@Transactional
	public void staticBuyerInteractionIncre(List<String> dpIds, int days) {
		long start = System.currentTimeMillis();
		Date nowStaticTime = new Date();

		// 构造假日期
		try {
			nowStaticTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-02-17 23:59:59");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date nowStaticDate = DateUtils.truncate(nowStaticTime, Calendar.DAY_OF_MONTH);

		for (String dpId : dpIds) {

			// 确定增量统计日期
			Date incrementStatisticEndTime = nowStaticTime;
			Date incrementStatisticStartTime = null;
			List<OrderDomain> overDayOrderDomains = null;
			List<SendLogDomain> overSendLogDomains = null;

			// 最近的统计时间信息
			ShopStatisConfigDomain shopStatisConfigDomain = shopStatisConfigRepository.getByDpId(dpId);
			int statsInterval = TradeCenterCons.statisInterval;
			if (shopStatisConfigDomain != null) {
				statsInterval = shopStatisConfigDomain.getStatisInterval();
			}

			// 在统计间隔之内
			if (shopStatisConfigDomain == null || shopStatisConfigDomain.getRecentlyOrderStatisTime() == null
					|| TimeUtils.getIntervalSeconds(nowStaticTime) > statsInterval) {
				// 如果上次统计的时间跟现在不是同一天
				if (shopStatisConfigDomain != null && shopStatisConfigDomain.getRecentlyOrderStatisTime() != null) {
					incrementStatisticStartTime = shopStatisConfigDomain.getRecentlyOrderStatisTime();
					if (TimeUtils.getIntervalSeconds(incrementStatisticEndTime, incrementStatisticStartTime) > days * 3600 * 24) {
						incrementStatisticStartTime = DateUtils.addDays(incrementStatisticEndTime, -days);
					}

					if (!DateUtils.isSameDay(shopStatisConfigDomain.getRecentlyOrderStatisTime(), nowStaticTime)) {
						overDayOrderDomains = orderRepository.findByCreatedBetween(
								DateUtils.addDays(nowStaticDate, -days), DateUtils.addDays(nowStaticDate, -(days - 1)),
								incrementStatisticStartTime, dpId);

						List<Integer> types = Arrays.asList(UrpayTypeEnum.AUTO_URPAY.getTypeValue(),
								UrpayTypeEnum.CHEAP_URPAY.getTypeValue(), UrpayTypeEnum.PRE_CLOSE_URPAY.getTypeValue());
						overSendLogDomains = sendLogRepository.getByDpId(dpId, DateUtils.addDays(nowStaticDate, -days),
								DateUtils.addDays(nowStaticDate, -(days - 1)), types);
					}
				}

				else {
					// 获取
					incrementStatisticStartTime = DateUtils.addDays(nowStaticDate, -days);
				}

				// 获取今天的统计信息
				List<BuyerInteractionStatisticDomain> newBuyerStatisticDomains = new ArrayList<BuyerInteractionStatisticDomain>();
				List<BuyerInteractionStatisticDomain> modifyBuyerStatisticDomains = buyerInteractionStatisticRepository
						.getByDpId(dpId);
				Map<String, BuyerInteractionStatisticDomain> buyerStatisticDomainMap = ListUtil.toMapByProperty(
						modifyBuyerStatisticDomains, "customerno");

				Date lastTimeStartTime = null;

				// 获取需要增量分析的订单
				List<OrderDomain> orderDomains = orderRepository.findByModifiedBetween(incrementStatisticStartTime,
						incrementStatisticEndTime, dpId);

				// 开始统计
				for (int i = 0; i < orderDomains.size(); i++) {
					OrderDomain orderDomain = orderDomains.get(i);

					BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap.get(orderDomain
							.getCustomerno());

					boolean isNew = false;
					if (buyerStatisticDomain == null) {

						buyerStatisticDomain = new BuyerInteractionStatisticDomain();
						buyerStatisticDomain.setCreated(new Date());
						buyerStatisticDomain.setUpdated(new Date());
						buyerStatisticDomain.setCustomerno(orderDomain.getCustomerno());
						buyerStatisticDomain.setDealDate(nowStaticDate);
						buyerStatisticDomain.setTradeCloseCount(0);
						buyerStatisticDomain.setTradeCount(0);
						buyerStatisticDomain.setTradeNoPayedCount(0);
						buyerStatisticDomain.setTradePayedCount(0);
						buyerStatisticDomain.setUrpayCount(0);
						buyerStatisticDomain.setDpId(dpId);

						isNew = true;
					} else {
						buyerStatisticDomain.setUpdated(new Date());
						buyerStatisticDomain.setDealDate(nowStaticDate);
					}

					if (orderDomain.getCreated().compareTo(DateUtils.addDays(nowStaticDate, -days)) > 0) {

						// 新增时间在统计时段内
						if (isBetweenDate(orderDomain.getCreated(), incrementStatisticStartTime,
								incrementStatisticEndTime)) {
							buyerStatisticDomain.addTradeCount(1);
							buyerStatisticDomain.addTradeNoPayedCount(1);
						}

						// 付款时间段在统计时间内
						if (isBetweenDate(orderDomain.getPayTime(), incrementStatisticStartTime,
								incrementStatisticEndTime)) {
							buyerStatisticDomain.addTradePayedCount(1);
							buyerStatisticDomain.addTradeNoPayedCount(-1);
						}

						// 关闭时间在统计时间内
						if (isBetweenDate(orderDomain.getEndtime(), incrementStatisticStartTime,
								incrementStatisticEndTime) && "TRADE_CLOSED_BY_TAOBAO".equals(orderDomain.getStatus())) {
							buyerStatisticDomain.addTradeCloseCount(1);
							buyerStatisticDomain.addTradeNoPayedCount(-1);
						}

						// 将最后时间保存为最后一次更新时间
						if (i == orderDomains.size() - 1) {
							lastTimeStartTime = orderDomain.getModified();
						}
					}

					/**
					 * 如果是新增，且有值的保存
					 */
					if (isNew && buyerStatisticDomain.getTradeCount() > 0) {
						buyerStatisticDomainMap.put(orderDomain.getCustomerno(), buyerStatisticDomain);

						newBuyerStatisticDomains.add(buyerStatisticDomain);
					}
				}

				// 如果统计时间跨天，减去最前的一天的统计数据
				if (overDayOrderDomains != null && overDayOrderDomains.size() > 0) {
					for (int i = 0; i < overDayOrderDomains.size(); i++) {
						OrderDomain orderDomain = overDayOrderDomains.get(i);

						// 由前一个过程决定，绝不可能为null,但在初始数据不够的情况下
						BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap.get(orderDomain
								.getCustomerno());

						// 新增时间在统计时段内
						if (!isBetweenDate(orderDomain.getCreated(), incrementStatisticStartTime,
								incrementStatisticEndTime)) {
							buyerStatisticDomain.addTradeCount(-1);
						}

						boolean cutNoPayedCount = true;
						if (orderDomain.getPayTime() != null) {
							buyerStatisticDomain.addTradePayedCount(-1);
							cutNoPayedCount = false;
						}

						if (orderDomain.getEndtime() != null
								&& "TRADE_CLOSED_BY_TAOBAO".equals(orderDomain.getStatus())) {
							buyerStatisticDomain.addTradeCloseCount(-1);
							cutNoPayedCount = false;
						}

						if (cutNoPayedCount) {
							buyerStatisticDomain.addTradeNoPayedCount(-1);
						}
					}
				}
				// --- 统计结束 ---

				// --- 催付次数统计开始
				// 开始统计
				List<Integer> types = Arrays.asList(UrpayTypeEnum.AUTO_URPAY.getTypeValue(),
						UrpayTypeEnum.CHEAP_URPAY.getTypeValue(), UrpayTypeEnum.PRE_CLOSE_URPAY.getTypeValue());
				List<SendLogDomain> sendLogDomains = sendLogRepository.getByDpId(dpId, incrementStatisticStartTime,
						incrementStatisticEndTime, types);
				for (int i = 0; i < sendLogDomains.size(); i++) {
					SendLogDomain sendLogDomain = sendLogDomains.get(i);

					BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap.get(sendLogDomain
							.getBuyerNick());

					if (buyerStatisticDomain == null) {

						buyerStatisticDomain = new BuyerInteractionStatisticDomain();
						buyerStatisticDomain.setCreated(new Date());
						buyerStatisticDomain.setUpdated(new Date());
						buyerStatisticDomain.setCustomerno(sendLogDomain.getBuyerNick());
						buyerStatisticDomain.setDealDate(nowStaticDate);
						buyerStatisticDomain.setTradeCloseCount(0);
						buyerStatisticDomain.setTradeCount(0);
						buyerStatisticDomain.setTradeNoPayedCount(0);
						buyerStatisticDomain.setTradePayedCount(0);
						buyerStatisticDomain.setUrpayCount(0);
						buyerStatisticDomain.setDpId(dpId);
						buyerStatisticDomainMap.put(sendLogDomain.getBuyerNick(), buyerStatisticDomain);

						newBuyerStatisticDomains.add(buyerStatisticDomain);
					} else {
						buyerStatisticDomain.setUpdated(new Date());
						buyerStatisticDomain.setDealDate(nowStaticDate);
					}

					buyerStatisticDomain.addUrpayCount(1);
				}

				// 如果统计时间跨天，减去最前的一天的统计数据
				if (overSendLogDomains != null && overSendLogDomains.size() > 0) {
					for (int i = 0; i < overSendLogDomains.size(); i++) {
						SendLogDomain sendLogDomain = overSendLogDomains.get(i);
						BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap
								.get(sendLogDomain.getBuyerNick());
						buyerStatisticDomain.addUrpayCount(-1);
					}
				}

				// --- 催付次数统计结束

				// 保存统计结果
				batchRepository.batchInsert(newBuyerStatisticDomains);

				// 更新统计结果
				batchRepository.batchUpdate(modifyBuyerStatisticDomains);

				// 最后统计时间更新
				if (shopStatisConfigDomain == null) {
					shopStatisConfigDomain = new ShopStatisConfigDomain();
					shopStatisConfigDomain.setDpId(dpId);
					shopStatisConfigDomain.setRecentlyOrderStatisTime(lastTimeStartTime);
					shopStatisConfigDomain.setRecentlyUrpayStatisTime(nowStaticTime);
					shopStatisConfigDomain.setStatisInterval(statsInterval);
					shopStatisConfigDomain.setCreated(new Date());
					shopStatisConfigDomain.setUpdated(new Date());

					shopStatisConfigRepository.save(shopStatisConfigDomain);
				} else {
					shopStatisConfigRepository.updateTime(dpId, lastTimeStartTime, nowStaticTime);
				}

				long end = System.currentTimeMillis();

				System.out.println("##############################################");
				System.out.println(end - start);
				System.out.println("##############################################");
			}
		}
	}


	@Override
	@Transactional
	public void staticBuyerInteractionByCus(String dpId, int days, Date date, List<String> customernos) {
		Date nowStaticDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
		// 确定增量统计日期
		Date incrementStatisticEndTime = date;
		Date incrementStatisticStartTime = DateUtils.addDays(nowStaticDate, -days);

		// 最近的统计时间信息
		ShopStatisConfigDomain shopStatisConfigDomain = shopStatisConfigRepository.getByDpId(dpId);
		int statsInterval = TradeCenterCons.statisInterval;
		if (shopStatisConfigDomain != null) {
			statsInterval = shopStatisConfigDomain.getStatisInterval();
		}

		// 在统计间隔之内
		if (shopStatisConfigDomain == null || shopStatisConfigDomain.getRecentlyOrderStatisTime() == null || DateUtil.getSeconds(date, shopStatisConfigDomain.getRecentlyOrderStatisTime()) > statsInterval) {
			// 获取需要增量分析的订单
			List<OrderDomain> orderDomains = orderRepository.findByCreatedBetween(incrementStatisticStartTime, incrementStatisticEndTime, dpId, customernos);

			// 开始
			List<Integer> types = new ArrayList<Integer>();
			types.add(UserInteractionType.AUTO_URPAY.getType());
			types.add(UserInteractionType.CHEAP_URPAY.getType());
			types.add(UserInteractionType.MANUAL_URPAY.getType());
			types.add(UserInteractionType.MANUAL_WANGWANG_URPAY.getType());
			types.add(UserInteractionType.PRE_CLOSE_URPAY.getType());
			List<SendLogDomain> sendLogDomains = sendLogRepository.getByDpIdAndCus(dpId, incrementStatisticStartTime, incrementStatisticEndTime, types, customernos);

			staticBuyerInteraction(orderDomains, sendLogDomains, dpId, incrementStatisticEndTime, incrementStatisticStartTime);
		}
	}

	@Override
	@Transactional
	public void staticBuyerInteraction(String dpId, int days, Date date) {
		logger.info("static buyer interaction start");

		long start = System.currentTimeMillis();


		Date nowStaticDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
		// 确定增量统计日期
		Date incrementStatisticEndTime = date;
		Date incrementStatisticStartTime = DateUtils.addDays(nowStaticDate, -days);



		// 最近的统计时间信息
		ShopStatisConfigDomain shopStatisConfigDomain = shopStatisConfigRepository.getByDpId(dpId);
		int statsInterval = TradeCenterCons.statisInterval;
		if (shopStatisConfigDomain != null) {
			statsInterval = shopStatisConfigDomain.getStatisInterval();
		}

		// 在统计间隔之内
		if (shopStatisConfigDomain == null || shopStatisConfigDomain.getRecentlyOrderStatisTime() == null || DateUtil.getSeconds(date, shopStatisConfigDomain.getRecentlyOrderStatisTime()) > statsInterval) {
			// 获取需要增量分析的订单
			List<OrderDomain> orderDomains = orderRepository.findByCreatedBetween(incrementStatisticStartTime, incrementStatisticEndTime, dpId);
			// 开始
			List<Integer> types = new ArrayList<Integer>();
			types.add(UserInteractionType.AUTO_URPAY.getType());
			types.add(UserInteractionType.CHEAP_URPAY.getType());
			types.add(UserInteractionType.MANUAL_URPAY.getType());
			types.add(UserInteractionType.MANUAL_WANGWANG_URPAY.getType());
			types.add(UserInteractionType.PRE_CLOSE_URPAY.getType());
			List<SendLogDomain> sendLogDomains = sendLogRepository.getByDpId(dpId, incrementStatisticStartTime,
					incrementStatisticEndTime, types);

			staticBuyerInteraction(orderDomains, sendLogDomains, dpId, incrementStatisticEndTime, incrementStatisticStartTime);


			// 最后统计时间更新
			if (shopStatisConfigDomain == null) {
				shopStatisConfigDomain = new ShopStatisConfigDomain();
				shopStatisConfigDomain.setDpId(dpId);
				shopStatisConfigDomain.setRecentlyOrderStatisTime(incrementStatisticStartTime);
				shopStatisConfigDomain.setRecentlyUrpayStatisTime(incrementStatisticEndTime);
				shopStatisConfigDomain.setStatisInterval(statsInterval);
				shopStatisConfigDomain.setCreated(new Date());
				shopStatisConfigDomain.setUpdated(new Date());

				shopStatisConfigRepository.save(shopStatisConfigDomain);
			} else {
				shopStatisConfigRepository.updateTime(dpId, date, date);
			}
		}

		long end = System.currentTimeMillis();
		logger.info("static buyer interaction end, costTime:{}",  (end - start));
	}

	private void staticBuyerInteraction(List<OrderDomain> orderDomains, List<SendLogDomain> sendLogDomains, String dpId, Date incrementStatisticEndTime, Date incrementStatisticStartTime) {

			// 获取今天的统计信息
			List<BuyerInteractionStatisticDomain> newBuyerStatisticDomains = new ArrayList<BuyerInteractionStatisticDomain>();
			List<BuyerInteractionStatisticDomain> modifyBuyerStatisticDomains = new ArrayList<BuyerInteractionStatisticDomain>();

			long s00 = System.currentTimeMillis();

			List<BuyerInteractionStatisticDomain> haveBuyerStatisticDomains = buyerInteractionStatisticRepository.getByDpId(dpId);

			long e00 = System.currentTimeMillis();
			logger.info("buyerInteractionStatisticRepository.getByDpId, size:{}, costTime:{}", haveBuyerStatisticDomains.size(), (e00 - s00));

			long s01 = System.currentTimeMillis();
			Map<String, BuyerInteractionStatisticDomain> haveBuyerStatisticDomainMap = ListUtil.toMapByProperty(haveBuyerStatisticDomains, "customerno");
			long e01 = System.currentTimeMillis();

			logger.debug("ListUtil.toMapByProperty costTime:{}", (e01 - s01));
			Map<String, BuyerInteractionStatisticDomain> buyerStatisticDomainMap = new HashMap<String, BuyerInteractionStatisticDomain>();

			long s02 = System.currentTimeMillis();
			// 获取需要增量分析的订单
			long e02 = System.currentTimeMillis();
			logger.info("orderRepository.findByCreatedBetween, size:{}, costTime:{}", orderDomains.size(), (e02 - s02));

			long s0 = System.currentTimeMillis();
			// 开始统计
			for (int i = 0; i < orderDomains.size(); i++) {
				OrderDomain orderDomain = orderDomains.get(i);

				BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap.get(orderDomain.getCustomerno());

				boolean isNew = false;
				if (buyerStatisticDomain == null) {
					isNew = true;

					buyerStatisticDomain = new BuyerInteractionStatisticDomain();
					buyerStatisticDomain.setCreated(new Date());
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setCustomerno(orderDomain.getCustomerno());
					buyerStatisticDomain.setDealDate(incrementStatisticEndTime);
					buyerStatisticDomain.setTradeCloseCount(0);
					buyerStatisticDomain.setTradeCount(0);
					buyerStatisticDomain.setTradeNoPayedCount(0);
					buyerStatisticDomain.setTradePayedCount(0);
					buyerStatisticDomain.setUrpayCount(0);
					buyerStatisticDomain.setDpId(orderDomain.getDpId());

				} else {
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setDealDate(incrementStatisticEndTime);
				}

				if (orderDomain.getCreated().compareTo(incrementStatisticStartTime) > 0) {

					// 新增时间在统计时段内
					if (isBetweenDate(orderDomain.getCreated(), incrementStatisticStartTime, incrementStatisticEndTime)) {
						buyerStatisticDomain.addTradeCount(1);
						buyerStatisticDomain.addTradeNoPayedCount(1);
					}

					// 付款时间段在统计时间内
					if (isBetweenDate(orderDomain.getPayTime(), incrementStatisticStartTime, incrementStatisticEndTime)) {
						buyerStatisticDomain.addTradePayedCount(1);
						buyerStatisticDomain.addTradeNoPayedCount(-1);
					}

					// 关闭时间在统计时间内
					if (isBetweenDate(orderDomain.getEndtime(), incrementStatisticStartTime, incrementStatisticEndTime)) {

						//未付款关闭
						if ("TRADE_CLOSED_BY_TAOBAO".equals(orderDomain.getStatus())) {
							buyerStatisticDomain.addTradeCloseCount(1);
							buyerStatisticDomain.addTradeNoPayedCount(-1);
						}
						//申请退款关闭
						else if ("TRADE_CLOSED".equals(orderDomain.getStatus())) {
							buyerStatisticDomain.addTradeCloseCount(1);
							buyerStatisticDomain.addTradePayedCount(-1);
						}

					}
				}

				if (isNew && buyerStatisticDomain.getTradeCount() > 0) {
					buyerStatisticDomainMap.put(orderDomain.getCustomerno(), buyerStatisticDomain);

					BuyerInteractionStatisticDomain buyerInteractionStatisticDomain = haveBuyerStatisticDomainMap.get(orderDomain.getCustomerno());
					if (buyerInteractionStatisticDomain != null) {
						haveBuyerStatisticDomains.remove(buyerInteractionStatisticDomain);

						buyerStatisticDomain.setPkid(buyerInteractionStatisticDomain.getPkid());
						modifyBuyerStatisticDomains.add(buyerStatisticDomain);
					} else {
						newBuyerStatisticDomains.add(buyerStatisticDomain);
					}
				}
			}

			long e0 = System.currentTimeMillis();
			logger.info("statistic target, costTime:{}", (e0 - s0));

			// --- 统计结束 ---

			long s2 = System.currentTimeMillis();
			// --- 催付次数统计开始
			// 开始统计
			List<Integer> types = new ArrayList<Integer>();
			types.add(UserInteractionType.AUTO_URPAY.getType());
			types.add(UserInteractionType.CHEAP_URPAY.getType());
			types.add(UserInteractionType.MANUAL_URPAY.getType());
			types.add(UserInteractionType.MANUAL_WANGWANG_URPAY.getType());
			types.add(UserInteractionType.PRE_CLOSE_URPAY.getType());
			for (int i = 0; i < sendLogDomains.size(); i++) {
				SendLogDomain sendLogDomain = sendLogDomains.get(i);

				BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap.get(sendLogDomain.getBuyerNick());

				boolean isNew = false;
				if (buyerStatisticDomain == null) {
					isNew = true;

					buyerStatisticDomain = new BuyerInteractionStatisticDomain();
					buyerStatisticDomain.setCreated(new Date());
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setCustomerno(sendLogDomain.getBuyerNick());
					buyerStatisticDomain.setDealDate(incrementStatisticEndTime);
					buyerStatisticDomain.setTradeCloseCount(0);
					buyerStatisticDomain.setTradeCount(0);
					buyerStatisticDomain.setTradeNoPayedCount(0);
					buyerStatisticDomain.setTradePayedCount(0);
					buyerStatisticDomain.setUrpayCount(0);
					buyerStatisticDomain.setDpId(dpId);

				} else {
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setDealDate(incrementStatisticEndTime);
				}

				buyerStatisticDomain.addUrpayCount(1);

				if (isNew) {
					buyerStatisticDomainMap.put(sendLogDomain.getBuyerNick(), buyerStatisticDomain);

					if (haveBuyerStatisticDomainMap.containsKey(sendLogDomain.getBuyerNick())) {
						//去掉最近有值的数据
						haveBuyerStatisticDomains.remove(haveBuyerStatisticDomainMap.get(sendLogDomain.getBuyerNick()));
						modifyBuyerStatisticDomains.add(buyerStatisticDomain);
					} else {
						newBuyerStatisticDomains.add(buyerStatisticDomain);
					}
				}
			}

			// --- 催付次数统计结束
			long e2 = System.currentTimeMillis();
			logger.info("statis urpay times, costTimes:{}", (e2 - s2));

			long s3 = System.currentTimeMillis();

			/**
			 * 批量操作的性能问题
			 */
				// 保存统计结果
			batchRepository.batchInsert(newBuyerStatisticDomains);


			// 更新统计结果
			//过滤出真实变化的数据，有些数据结果实际上没有变化
			List<BuyerInteractionStatisticDomain> yesModifyBuyerStatisticDomains = new ArrayList<BuyerInteractionStatisticDomain>();
			for (BuyerInteractionStatisticDomain modifyBuyerStatisticDomain : modifyBuyerStatisticDomains) {
				if (!modifyBuyerStatisticDomain.equals(haveBuyerStatisticDomainMap.get(modifyBuyerStatisticDomain.getCustomerno()))) {
					yesModifyBuyerStatisticDomains.add(modifyBuyerStatisticDomain);
				}
			}

			//去除没有变化的数据，做更新
			batchRepository.batchUpdate(yesModifyBuyerStatisticDomains);

			//删除以往数据，不在最近3天中的数据
			buyerInteractionStatisticRepository.delete(haveBuyerStatisticDomains);

			long e3 = System.currentTimeMillis();

			logger.info("insert or update buyer interaction, insertSize:{}, updateSize:{}, deleteSize:{}, costTimes:{}", new Object[]{newBuyerStatisticDomains.size(), yesModifyBuyerStatisticDomains.size(), haveBuyerStatisticDomains.size(), (e3 - s3)});
	}



	/*public void staticBuyerInteraction(String dpId, int days, Date date) {
		logger.info("static buyer interaction start");

		long start = System.currentTimeMillis();


		Date nowStaticDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

		// 确定增量统计日期
		Date incrementStatisticEndTime = date;
		Date incrementStatisticStartTime = DateUtils.addDays(nowStaticDate, -days);

		// 最近的统计时间信息
		ShopStatisConfigDomain shopStatisConfigDomain = shopStatisConfigRepository.getByDpId(dpId);
		int statsInterval = TradeCenterCons.statisInterval;
		if (shopStatisConfigDomain != null) {
			statsInterval = shopStatisConfigDomain.getStatisInterval();
		}

		// 在统计间隔之内
		if (shopStatisConfigDomain == null || shopStatisConfigDomain.getRecentlyOrderStatisTime() == null || DateUtil.getSeconds(date, shopStatisConfigDomain.getRecentlyOrderStatisTime()) > statsInterval) {


			// 获取今天的统计信息
			List<BuyerInteractionStatisticDomain> newBuyerStatisticDomains = new ArrayList<BuyerInteractionStatisticDomain>();
			List<BuyerInteractionStatisticDomain> modifyBuyerStatisticDomains = new ArrayList<BuyerInteractionStatisticDomain>();

			long s00 = System.currentTimeMillis();

			List<BuyerInteractionStatisticDomain> haveBuyerStatisticDomains = buyerInteractionStatisticRepository.getByDpId(dpId);

			long e00 = System.currentTimeMillis();
			logger.info("buyerInteractionStatisticRepository.getByDpId, size:{}, costTime:{}", haveBuyerStatisticDomains.size(), (e00 - s00));

			long s01 = System.currentTimeMillis();
			Map<String, BuyerInteractionStatisticDomain> haveBuyerStatisticDomainMap = ListUtil.toMapByProperty(haveBuyerStatisticDomains, "customerno");
			long e01 = System.currentTimeMillis();

			logger.debug("ListUtil.toMapByProperty costTime:{}", (e01 - s01));
			Map<String, BuyerInteractionStatisticDomain> buyerStatisticDomainMap = new HashMap<String, BuyerInteractionStatisticDomain>();

			long s02 = System.currentTimeMillis();
			// 获取需要增量分析的订单
			List<OrderDomain> orderDomains = orderRepository.findByCreatedBetween(incrementStatisticStartTime, incrementStatisticEndTime, dpId);
			long e02 = System.currentTimeMillis();
			logger.info("orderRepository.findByCreatedBetween, size:{}, costTime:{}", orderDomains.size(), (e02 - s02));

			long s0 = System.currentTimeMillis();
			// 开始统计
			for (int i = 0; i < orderDomains.size(); i++) {
				OrderDomain orderDomain = orderDomains.get(i);

				BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap.get(orderDomain.getCustomerno());

				boolean isNew = false;
				if (buyerStatisticDomain == null) {
					isNew = true;

					buyerStatisticDomain = new BuyerInteractionStatisticDomain();
					buyerStatisticDomain.setCreated(new Date());
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setCustomerno(orderDomain.getCustomerno());
					buyerStatisticDomain.setDealDate(nowStaticDate);
					buyerStatisticDomain.setTradeCloseCount(0);
					buyerStatisticDomain.setTradeCount(0);
					buyerStatisticDomain.setTradeNoPayedCount(0);
					buyerStatisticDomain.setTradePayedCount(0);
					buyerStatisticDomain.setUrpayCount(0);
					buyerStatisticDomain.setDpId(dpId);

				} else {
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setDealDate(nowStaticDate);
				}

				if (orderDomain.getCreated().compareTo(DateUtils.addDays(nowStaticDate, -days)) > 0) {

					// 新增时间在统计时段内
					if (isBetweenDate(orderDomain.getCreated(), incrementStatisticStartTime, incrementStatisticEndTime)) {
						buyerStatisticDomain.addTradeCount(1);
						buyerStatisticDomain.addTradeNoPayedCount(1);
					}

					// 付款时间段在统计时间内
					if (isBetweenDate(orderDomain.getPayTime(), incrementStatisticStartTime, incrementStatisticEndTime)) {
						buyerStatisticDomain.addTradePayedCount(1);
						buyerStatisticDomain.addTradeNoPayedCount(-1);
					}

					// 关闭时间在统计时间内
					if (isBetweenDate(orderDomain.getEndtime(), incrementStatisticStartTime, incrementStatisticEndTime)) {

						//未付款关闭
						if ("TRADE_CLOSED_BY_TAOBAO".equals(orderDomain.getStatus())) {
							buyerStatisticDomain.addTradeCloseCount(1);
							buyerStatisticDomain.addTradeNoPayedCount(-1);
						}
						//申请退款关闭
						else if ("TRADE_CLOSED".equals(orderDomain.getStatus())) {
							buyerStatisticDomain.addTradeCloseCount(1);
							buyerStatisticDomain.addTradePayedCount(-1);
						}

					}
				}

				if (isNew && buyerStatisticDomain.getTradeCount() > 0) {
					buyerStatisticDomainMap.put(orderDomain.getCustomerno(), buyerStatisticDomain);

					BuyerInteractionStatisticDomain buyerInteractionStatisticDomain = haveBuyerStatisticDomainMap.get(orderDomain.getCustomerno());
					if (buyerInteractionStatisticDomain != null) {
						haveBuyerStatisticDomains.remove(buyerInteractionStatisticDomain);

						buyerStatisticDomain.setPkid(buyerInteractionStatisticDomain.getPkid());
						modifyBuyerStatisticDomains.add(buyerStatisticDomain);
					} else {
						newBuyerStatisticDomains.add(buyerStatisticDomain);
					}
				}
			}

			long e0 = System.currentTimeMillis();
			logger.info("statistic target, costTime:{}", (e0 - s0));

			// --- 统计结束 ---

			long s2 = System.currentTimeMillis();
			// --- 催付次数统计开始
			// 开始统计
			List<Integer> types = new ArrayList<Integer>();
			types.add(UserInteractionType.AUTO_URPAY.getName());
			types.add(UserInteractionType.CHEAP_URPAY.getName());
			types.add(UserInteractionType.MANUAL_URPAY.getName());
			types.add(UserInteractionType.MANUAL_WANGWANG_URPAY.getName());
			types.add(UserInteractionType.PRE_CLOSE_URPAY.getName());
			List<SendLogDomain> sendLogDomains = sendLogRepository.getByDpId(dpId, incrementStatisticStartTime,
					incrementStatisticEndTime, types);
			for (int i = 0; i < sendLogDomains.size(); i++) {
				SendLogDomain sendLogDomain = sendLogDomains.get(i);

				BuyerInteractionStatisticDomain buyerStatisticDomain = buyerStatisticDomainMap.get(sendLogDomain.getBuyerNick());

				boolean isNew = false;
				if (buyerStatisticDomain == null) {
					isNew = true;

					buyerStatisticDomain = new BuyerInteractionStatisticDomain();
					buyerStatisticDomain.setCreated(new Date());
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setCustomerno(sendLogDomain.getBuyerNick());
					buyerStatisticDomain.setDealDate(nowStaticDate);
					buyerStatisticDomain.setTradeCloseCount(0);
					buyerStatisticDomain.setTradeCount(0);
					buyerStatisticDomain.setTradeNoPayedCount(0);
					buyerStatisticDomain.setTradePayedCount(0);
					buyerStatisticDomain.setUrpayCount(0);
					buyerStatisticDomain.setDpId(dpId);

				} else {
					buyerStatisticDomain.setUpdated(new Date());
					buyerStatisticDomain.setDealDate(nowStaticDate);
				}

				buyerStatisticDomain.addUrpayCount(1);

				if (isNew) {
					buyerStatisticDomainMap.put(sendLogDomain.getBuyerNick(), buyerStatisticDomain);

					if (haveBuyerStatisticDomainMap.containsKey(sendLogDomain.getBuyerNick())) {
						//去掉最近有值的数据
						haveBuyerStatisticDomains.remove(haveBuyerStatisticDomainMap.get(sendLogDomain.getBuyerNick()));
						modifyBuyerStatisticDomains.add(buyerStatisticDomain);
					} else {
						newBuyerStatisticDomains.add(buyerStatisticDomain);
					}
				}
			}

			// --- 催付次数统计结束
			long e2 = System.currentTimeMillis();
			logger.info("statis urpay times, costTimes:{}", (e2 - s2));

			long s3 = System.currentTimeMillis();

			*//**
			 * 批量操作的性能问题
			 *//*
			// 保存统计结果
			batchRepository.batchInsert(newBuyerStatisticDomains);

			// 更新统计结果
			//过滤出实际没有变化的数据
			List<BuyerInteractionStatisticDomain> noModifyBuyerStatisticDomains = new ArrayList<BuyerInteractionStatisticDomain>();
			for (BuyerInteractionStatisticDomain modifyBuyerStatisticDomain : modifyBuyerStatisticDomains) {
				if (modifyBuyerStatisticDomain.equals(haveBuyerStatisticDomainMap.get(modifyBuyerStatisticDomain.getCustomerno()))) {
					noModifyBuyerStatisticDomains.add(modifyBuyerStatisticDomain);
				}
			}

			//去除没有变化的数据，做更新
			modifyBuyerStatisticDomains.removeAll(noModifyBuyerStatisticDomains);
			batchRepository.batchUpdate(modifyBuyerStatisticDomains);

			//删除以往数据，不在最近3天中的数据
			buyerInteractionStatisticRepository.delete(haveBuyerStatisticDomains);

			long e3 = System.currentTimeMillis();

			logger.info("insert or update buyer interaction, insertSize:{}, updateSize:{}, deleteSize:{}, costTimes:{}", new Object[]{newBuyerStatisticDomains.size(), modifyBuyerStatisticDomains.size(), haveBuyerStatisticDomains.size(), (e3 - s3)});

			// 最后统计时间更新
			if (shopStatisConfigDomain == null) {
				shopStatisConfigDomain = new ShopStatisConfigDomain();
				shopStatisConfigDomain.setDpId(dpId);
				shopStatisConfigDomain.setRecentlyOrderStatisTime(incrementStatisticStartTime);
				shopStatisConfigDomain.setRecentlyUrpayStatisTime(incrementStatisticEndTime);
				shopStatisConfigDomain.setStatisInterval(statsInterval);
				shopStatisConfigDomain.setCreated(new Date());
				shopStatisConfigDomain.setUpdated(new Date());

				shopStatisConfigRepository.save(shopStatisConfigDomain);
			} else {
				shopStatisConfigRepository.updateTime(dpId, incrementStatisticEndTime, incrementStatisticEndTime);
			}
		}

		long end = System.currentTimeMillis();
		logger.info("static buyer interaction end, costTime:{}",  (end - start));
	}
*/
	private boolean isBetweenDate(Date date, Date minDate, Date maxDate) {
		if (date == null) {
			return false;
		}

		return date.compareTo(minDate) >= 0 && date.compareTo(maxDate) < 0;
	}

}
