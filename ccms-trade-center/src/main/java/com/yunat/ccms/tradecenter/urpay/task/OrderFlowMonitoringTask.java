package com.yunat.ccms.tradecenter.urpay.task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomain;
import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomainPK;
import com.yunat.ccms.tradecenter.repository.IOrderRepository;
import com.yunat.ccms.tradecenter.repository.OrderFlowMonitoringRepository;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.support.cons.OrderFlowMonitoringStatusEnum;
import com.yunat.ccms.tradecenter.support.cons.OrderStatusEnum;
import com.yunat.ccms.tradecenter.support.cons.PropertiesGroupEnum;
import com.yunat.ccms.tradecenter.support.cons.PropertiesNameEnum;
import com.yunat.ccms.tradecenter.support.cons.ShippingStatus;

/**
 * 
 * @Description: 订单监控-订单流转监控
 * @author fanhong.meng
 * @date 2013-7-25 下午2:36:33
 * 
 */
@Component("orderFlowMonitoringTask")
public class OrderFlowMonitoringTask extends BaseJob {

	@Autowired
	private IOrderRepository iOrderRepository;

	@Autowired
	private OrderFlowMonitoringRepository orderFlowMonitoringRepository;

	@Autowired
	private TaobaoShopService taobaoShopService;

	@Autowired
	private PropertiesConfigManager propertiesConfigManager;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("订单流转监控-近30天订单流转监控，开始!");
		long s0 = System.currentTimeMillis();
		String nowDate = DateUtils.getStringDate(new Date());
		List<TaobaoShop> shops = taobaoShopService.taoBaoList();
		for (TaobaoShop taobaoShop : shops) {
			List<OrderFlowMonitoringDomain> orderFlowMonitoringList = new ArrayList<OrderFlowMonitoringDomain>();
			String dpId = taobaoShop.getShopId();
			String created = DateUtils.getString(DateUtils.addDay(new Date(), -29)); // 获取30天前日期
			// 统计下单数
			Integer cretaeOrderNum = iOrderRepository.countOrderCreateTime(dpId, created);

			// 统计付款数
			Integer buyerPayOrderNum = iOrderRepository.countOrderPayTime(dpId, created);
			// 计算付款间隔
			Double buyerPayIntervalTime = iOrderRepository.countOrderPaymentIntervalTime(dpId, created);

			// 统计发货数
			Integer sendGoodsOrderNum = iOrderRepository.countOrderConsignTime(dpId, created);
			// 计算发货间隔
			Double sendGoodsIntervalTime = iOrderRepository.countOrderSendGoodsIntervalTime(dpId, created);

			// 统计签收数
			Integer signedOrderNum = iOrderRepository.countOrderAndTransitstepinfo(dpId, created,
					OrderStatusEnum.TRADE_FINISHED.getOrderStatusCode(), ShippingStatus.signed.getStatus());
			// 计算签收间隔
			Double signedIntervalTime = iOrderRepository.countOrderSignedIntervalTime(dpId, created,
					ShippingStatus.signed.getStatus());

			// 统计确认数
			Integer finishedOrderNum = iOrderRepository.countOrderFinished(dpId, created,
					OrderStatusEnum.TRADE_FINISHED.getOrderStatusCode());
			// 计算确认收货间隔
			Double finishedIntervalTime = iOrderRepository.countOrderFinishedIntervalTime(dpId, created,
					OrderStatusEnum.TRADE_FINISHED.getOrderStatusCode(), ShippingStatus.signed.getStatus());

			// 统计评价数
			Integer traderateOrderNum = iOrderRepository.countOrderAndTraderate(dpId, created);
			// 计算平均间隔
			Double traderateIntervalTime = iOrderRepository.countOrderTraderateIntervalTime(dpId, created);

			orderFlowMonitoringList.add(orderFlowMonitoring(dpId, OrderFlowMonitoringStatusEnum.CREATE_ORDER, 0.0,
					cretaeOrderNum, 0));
			logger.info("订单流转监控-店铺:{},计算下单数结束,{}", dpId, "订单数:" + cretaeOrderNum);
			orderFlowMonitoringList.add(orderFlowMonitoring(dpId, OrderFlowMonitoringStatusEnum.PAY_ORDER,
					buyerPayIntervalTime, buyerPayOrderNum, cretaeOrderNum));
			logger.info("订单流转监控-店铺:{},计算付款数结束,{}", dpId, "订单数:" + buyerPayOrderNum);
			orderFlowMonitoringList.add(orderFlowMonitoring(dpId, OrderFlowMonitoringStatusEnum.SEND_GOODS_ORDER,
					sendGoodsIntervalTime, sendGoodsOrderNum, buyerPayOrderNum));
			logger.info("订单流转监控-店铺:{},计算发货数结束,{}", dpId, "订单数:" + sendGoodsOrderNum);
			orderFlowMonitoringList.add(orderFlowMonitoring(dpId, OrderFlowMonitoringStatusEnum.SIGNED_ORDER,
					signedIntervalTime, signedOrderNum, sendGoodsOrderNum));
			logger.info("订单流转监控-店铺:{},计算签收数结束,{}", dpId, "订单数:" + signedOrderNum);
			orderFlowMonitoringList.add(orderFlowMonitoring(dpId, OrderFlowMonitoringStatusEnum.FINISHED_ORDER,
					finishedIntervalTime, finishedOrderNum, signedOrderNum));
			logger.info("订单流转监控-店铺:{},计算确认数结束,{}", dpId, "订单数:" + finishedOrderNum);
			orderFlowMonitoringList.add(orderFlowMonitoring(dpId, OrderFlowMonitoringStatusEnum.TRADERATE_ORDER,
					traderateIntervalTime, traderateOrderNum, finishedOrderNum));
			logger.info("订单流转监控-店铺:{},计算评价数结束,{}", dpId, "订单数:" + traderateOrderNum);

			orderFlowMonitoringRepository.save(orderFlowMonitoringList);

			// 更新最新一次执行时间
			propertiesConfigManager.saveProperties(dpId, PropertiesNameEnum.NEARLY_30_DAYS_ORDER_FLOW_MONITORING.getName(),
					nowDate, PropertiesGroupEnum.ORDER_FLOW_MONITORING.getName());
		}

		long e0 = System.currentTimeMillis();
		logger.info("订单流转监控-近30天订单流转监控，结束!耗时:{}", (e0 - s0));
	}

	private OrderFlowMonitoringDomain orderFlowMonitoring(String dpId, OrderFlowMonitoringStatusEnum statusEnum,
			Double intervalTime, Integer currentOrderNum, Integer lastOrderNum) {
		OrderFlowMonitoringDomain orderFlowMonitoring = new OrderFlowMonitoringDomain();
		OrderFlowMonitoringDomainPK orderFlowMonitoringPK = new OrderFlowMonitoringDomainPK();
		orderFlowMonitoringPK.setDpId(dpId);
		orderFlowMonitoringPK.setOrderStatus(statusEnum.getStatusCod());
		orderFlowMonitoring.setOrderFlowMonitoringPK(orderFlowMonitoringPK);

		orderFlowMonitoring.setOrderCount(currentOrderNum);
		orderFlowMonitoring.setFlowRate(calculateflowRate(Double.parseDouble(currentOrderNum.toString()),
				Double.parseDouble(lastOrderNum.toString())));
		orderFlowMonitoring.setFlowIntervalTime(decimalFormat(intervalTime));
		orderFlowMonitoring.setOrderId(statusEnum.getOrderId());
		orderFlowMonitoring.setStatusName(statusEnum.getStatusName());
		orderFlowMonitoring.setStatusDesc(statusEnum.getStatusDesc());
		return orderFlowMonitoring;
	}

	/**
	 * 计算流转率
	 * 
	 * @param divisor
	 *            终止数
	 * @param dividend
	 *            起始数
	 * @return
	 */
	private String calculateflowRate(Double divisor, Double dividend) {
		Double flowRate = 0.0;
		if (dividend > 0) {
			flowRate = (divisor / dividend);
		}
		flowRate = flowRate * 100; // 计算百分比
		return decimalFormat(flowRate);
	}

	/**
	 * 格式化Double 小数点最大1位最小0位
	 * 
	 * @param arg
	 * @return
	 */
	private String decimalFormat(Double arg) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(1);
		decimalFormat.setMinimumFractionDigits(0);
		return decimalFormat.format(arg).toString();
	}

}
