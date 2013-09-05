package com.yunat.ccms.tradecenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.controller.vo.AutoUrpayAndCareMonitoringVO;
import com.yunat.ccms.tradecenter.controller.vo.Today24HourOrderDataVO;
import com.yunat.ccms.tradecenter.controller.vo.TodayOrderChangeDataVO;
import com.yunat.ccms.tradecenter.controller.vo.TodayRealTimeOrderDataVO;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.domain.WarnConfigDomain;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.OrderFlowMonitoringRepository;
import com.yunat.ccms.tradecenter.repository.OrderMonitorRepository;
import com.yunat.ccms.tradecenter.repository.UrpayConfigRepository;
import com.yunat.ccms.tradecenter.repository.WarnConfigRepository;
import com.yunat.ccms.tradecenter.service.OrderMonitorService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 *
 * 订单监控服务实现类
 *
 * @author shaohui.li
 * @version $Id: OrderMonitorServiceImpl.java, v 0.1 2013-7-23 下午04:12:19 shaohui.li Exp $
 */
@Service("orderMonitorService")
public class OrderMonitorServiceImpl implements OrderMonitorService{

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(OrderMonitorServiceImpl.class);

    /** 订单监控dao **/
    @Autowired
    private OrderMonitorRepository orderMonitorRepository;

    @Autowired
    private OrderFlowMonitoringRepository orderFlowMonitoringRepository;

    @Autowired
    private UrpayConfigRepository urpayConfigRepository;

    @Autowired
    private CareConfigRepository careConfigRepository;

    @Autowired
    private WarnConfigRepository warnConfigRepository;

    @Override
    public TodayRealTimeOrderDataVO getTodayRealTimeOrderData(String dpId) {
        TodayRealTimeOrderDataVO vo = new TodayRealTimeOrderDataVO();
        Map<String,Object> map = orderMonitorRepository.getPayTimeIsTodayOrderNumAndAmount(dpId);
        //今日付款总订单数
        Long todayPayedOrderNum = (Long)map.get("todayPayedOrderNum");
        logger.info("店铺[" + dpId + "],今日付款总订单数:" + todayPayedOrderNum);
        vo.setPayTimeIsTodayOrderNum(todayPayedOrderNum);

        //今日付款总订单金额
        BigDecimal todayPayedOrderAmount = ((BigDecimal)map.get("todayPayedOrderAmount"));
        Double todayPayedAmount = 0.00;
        if(todayPayedOrderAmount != null){
            todayPayedAmount = todayPayedOrderAmount.doubleValue();;
        }
        logger.info("店铺[" + dpId + "],今日付款总订单金额:" + todayPayedAmount);
        vo.setPayTimeIsTodayOrderAmount(todayPayedAmount);

        //订单均价 = 今日付款总订单金额 / 今日付款总订单数
        Double orderAveragePrice = divide(todayPayedOrderAmount,todayPayedOrderNum,2);
        logger.info("店铺[" + dpId + "],订单均价:" + orderAveragePrice);
        vo.setOrderAveragePrice(orderAveragePrice);

        //关联销售订单占比 = (今日付款且子订单大于1的主订单数) / 今日付款总订单数
        //今日付款，子订单大于1的主订单数
        Long more1OrderNum = orderMonitorRepository.getPayTimeIsTodayItemNumMoreOneOrderNum(dpId);
        logger.info("店铺[" + dpId + "],今日付款，子订单大于1的主订单数:" + more1OrderNum);
        Double   salePercent = divide(more1OrderNum,todayPayedOrderNum,2);
        vo.setSalePercent(muti(salePercent));

        //平均成交件数 = 今天付款的订单的商品数 / 今日付款总订单数
        Long goodsNum = orderMonitorRepository.getPayTimeIsTodayItemGoodsNums(dpId);
        logger.info("店铺[" + dpId + "],今日付款，今天付款的订单的商品数:" + goodsNum);
        Double averageGoodsNum = divide(goodsNum,todayPayedOrderNum,2);
        vo.setAverageGoodsNum(averageGoodsNum);

        //今日拍下订单总数
        Long createdIsTodayOrderNum = orderMonitorRepository.getCreatedIsTodayOrderNum(dpId);
        logger.info("店铺[" + dpId + "],今日付款，今日拍下订单总数:" + createdIsTodayOrderNum);
        vo.setCreatedIsTodayOrderNum(createdIsTodayOrderNum);

        //付款率 = 拍下且付款的订单 / 今日拍下订单总数
        Long payedOrderNum = orderMonitorRepository.getCreatedIsTodayPayedOrderNum(dpId);
        logger.info("店铺[" + dpId + "],拍下且付款的订单:" + payedOrderNum);
        Double payedPercent = divide(payedOrderNum,createdIsTodayOrderNum,2);
        vo.setPayedPercent(muti(payedPercent));

        //未付款的订单
        Long notPayedOrderNum = orderMonitorRepository.getCreatedIsTodayNotPayedOrderNum(dpId);
        logger.info("店铺[" + dpId + "],未付款的订单:" + notPayedOrderNum);
        vo.setNotPayedOrderNum(notPayedOrderNum);

        //催付比列 = 未付款且催付的订单数 / 未付款的订单
        Long notPayAndNotifyOrderNum = orderMonitorRepository.getNotPayAndNotifyOrderNum(dpId);
        logger.info("店铺[" + dpId + "],未付款且催付的订单数:" + notPayAndNotifyOrderNum);
        Double urpayPercent = divide(notPayAndNotifyOrderNum,notPayedOrderNum,2);
        vo.setUrpayPercent(muti(urpayPercent));
        return vo;
    }

    /**
     * 获取当日24小时订单数据
     * @see com.yunat.ccms.tradecenter.service.OrderMonitorService#getToday24HourOrderData(java.lang.String)
     */
    @Override
    public Map<String, Today24HourOrderDataVO> getToday24HourOrderData(String dpId) {
        Map<String,Today24HourOrderDataVO> map = new TreeMap<String,Today24HourOrderDataVO>(
          new Comparator<String>(){
              /*
               * int compare(Object o1, Object o2) 返回一个基本类型的整型，
               * 返回负数表示：o1 小于o2，
               * 返回0 表示：o1和o2相等，
               * 返回正数表示：o1大于o2。
               */
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        });
        for(int i=0;i<24;i++){
            Today24HourOrderDataVO vo = new Today24HourOrderDataVO();
            vo.setOrderHour(i);
            vo.setOrderNum(0l);
            vo.setOrderAmount(0.00);
            vo.setPayedOrderAmount(0.00);
            map.put(String.valueOf(i), vo);
        }
        //24小时下单数据
        List<Today24HourOrderDataVO> list = orderMonitorRepository.getToday24HourOrderData(dpId, false);
        for(Today24HourOrderDataVO vo : list){
            String hour = String.valueOf(vo.getOrderHour());
            Today24HourOrderDataVO temp = map.get(hour);
            temp.setOrderAmount(vo.getOrderAmount());
        }
        //24小时付款数据
        List<Today24HourOrderDataVO> payedList = orderMonitorRepository.getToday24HourOrderData(dpId, true);
        for(Today24HourOrderDataVO vo : payedList){
            String hour = String.valueOf(vo.getOrderHour());
            Today24HourOrderDataVO temp = map.get(hour);
            temp.setPayedOrderAmount(vo.getOrderAmount());
        }
        return map;
    }

    /**
     * 除法
     *
     * @param src 除数
     * @param des 被除数
     * @param length 保留位数
     * @return
     */
    private Double divide(Number src,Number des,int length){
        if(des.doubleValue() == 0){
            return 0.00;
        }
        BigDecimal result = new BigDecimal(src.doubleValue()).divide(new BigDecimal(des.doubleValue()),length,BigDecimal.ROUND_HALF_UP);
        return result.doubleValue();
    }

    /**
     *
     * 乘 * 100
     * @param src
     * @param des
     * @return
     */
    public Integer muti(Number src){
        BigDecimal result = new BigDecimal(src.doubleValue()).movePointRight(2);
        return result.intValue();
    }

    /**
    *
    * 获取今日订单变更数据
    * @param dpId
    * @return
    */
    @Override
    public TodayOrderChangeDataVO getTodayOrderChangeData(String dpId) {
        TodayOrderChangeDataVO vo = new TodayOrderChangeDataVO();
        String tableName = "plt_taobao_order_tc";
        //今日拍下订单数据
        String fieldName = "created";
        Long createdTodayOrderNum = orderMonitorRepository.getOrderNumByTableAndFieldName(dpId, tableName, fieldName);
        vo.setCreatedTodayOrderNum(createdTodayOrderNum);

        //今日付款订单数
        fieldName = "pay_time";
        Long payedTodayOrderNum = orderMonitorRepository.getOrderNumByTableAndFieldName(dpId, tableName, fieldName);
        vo.setPayedTodayOrderNum(payedTodayOrderNum);

        //今日发货订单数
        fieldName = "consign_time";
        Long consignedTodayOrderNum = orderMonitorRepository.getOrderNumByTableAndFieldName(dpId, tableName, fieldName);
        vo.setConsignedTodayOrderNum(consignedTodayOrderNum);

        //同城到达订单数
        fieldName = "arrived_time";
        Long arrivedTodayOrderNum = orderMonitorRepository.getLogisNumByFieldName(dpId, fieldName);
        vo.setArrivedTodayOrderNum(arrivedTodayOrderNum);

        //派件
        fieldName = "delivery_time";
        Long deliveryTodayOrderNum = orderMonitorRepository.getLogisNumByFieldName(dpId, fieldName);
        vo.setDeliveryTodayOrderNum(deliveryTodayOrderNum);

        //签收
        fieldName = "signed_time";
        Long signedTodayOrderNum = orderMonitorRepository.getLogisNumByFieldName(dpId, fieldName);
        vo.setSignedTodayOrderNum(signedTodayOrderNum);

        //今日确认收货的订单数
        Long confirmTodayOrderNum = orderMonitorRepository.getConfirmTodayOrderNum(dpId);
        vo.setConfirmTodayOrderNum(confirmTodayOrderNum);

        //今日退款的子订单数
        tableName = "plt_taobao_refund";
        fieldName = "created";
        Long refundTodayOrderNum = orderMonitorRepository.getOrderNumByTableAndFieldName(dpId, tableName, fieldName);
        vo.setRefundTodayOrderNum(refundTodayOrderNum);

        //今日评价子订单数
        tableName = "plt_taobao_traderate";
        Long traderateTodayOrderNum = orderMonitorRepository.getOrderNumByTableAndFieldName(dpId, tableName, fieldName);
        vo.setTraderateTodayOrderNum(traderateTodayOrderNum);
        return vo;
    }

	@Override
	public List<OrderFlowMonitoringDomain> findNearly30DaysOrderData(String dpId) {
		return orderFlowMonitoringRepository.findOrderFlowMonitoring(dpId);
	}

	@Override
	public List<AutoUrpayAndCareMonitoringVO> findAutoUrpayAndCareMonitoringVO(String dpId) {
		List<AutoUrpayAndCareMonitoringVO> autoMonitoring = new ArrayList<AutoUrpayAndCareMonitoringVO>();
		// 催付
		autoMonitoring.add(getAutoUrpayMonitoringVO(dpId, UserInteractionType.AUTO_URPAY));
		autoMonitoring.add(getAutoUrpayMonitoringVO(dpId, UserInteractionType.PRE_CLOSE_URPAY));
		autoMonitoring.add(getAutoUrpayMonitoringVO(dpId, UserInteractionType.CHEAP_URPAY));

		// 关怀
		autoMonitoring.add(getAutoCareMonitoringVO(dpId, UserInteractionType.ORDER_CARE));
		autoMonitoring.add(getAutoCareMonitoringVO(dpId, UserInteractionType.SHIPMENT_CARE));
		autoMonitoring.add(getAutoCareMonitoringVO(dpId, UserInteractionType.ARRIVED_CARE));
		autoMonitoring.add(getAutoCareMonitoringVO(dpId, UserInteractionType.DELIVERY_CARE));
		autoMonitoring.add(getAutoCareMonitoringVO(dpId, UserInteractionType.SIGNED_CARE));
		autoMonitoring.add(getAutoCareMonitoringVO(dpId, UserInteractionType.REFUND_CARE));
		autoMonitoring.add(getAutoCareMonitoringVO(dpId, UserInteractionType.CONFIRM_CARE));

		//警告
		autoMonitoring.add(getAutoWarnMonitoringVO(dpId,UserInteractionType.NOT_GOOD_TRADERATE_WARN));
		autoMonitoring.add(getAutoWarnMonitoringVO(dpId,UserInteractionType.REFUND_WARN));
		return autoMonitoring;
	}

	/**
	 * 查询 催付信息
	 * @param dpId
	 * @param name
	 * @param type 催付类型
	 * @return
	 */
	private AutoUrpayAndCareMonitoringVO getAutoUrpayMonitoringVO(String dpId, UserInteractionType userInteractionType){
		UrpayConfigDomain urpayConfigDomain = urpayConfigRepository.getByUrpayTypeAndDpId(userInteractionType.getType(), dpId);

		AutoUrpayAndCareMonitoringVO autoUrpayAndCareMonitoringVO = new AutoUrpayAndCareMonitoringVO();
		autoUrpayAndCareMonitoringVO.setName(userInteractionType.getMessage());
		autoUrpayAndCareMonitoringVO.setType(userInteractionType.getType());
		if (null != urpayConfigDomain) {
			autoUrpayAndCareMonitoringVO.setIsOpen(urpayConfigDomain.getIsOpen());
		} else {
			autoUrpayAndCareMonitoringVO.setIsOpen(0);
		}
		return autoUrpayAndCareMonitoringVO;
	}

	/**
	 * 查询 关怀信息
	 * @param dpId
	 * @param name
	 * @param type 关怀类型
	 * @return
	 */
	private AutoUrpayAndCareMonitoringVO getAutoCareMonitoringVO(String dpId, UserInteractionType userInteractionType){
		CareConfigDomain careConfigDomain = careConfigRepository.getByCareTypeAndDpId(userInteractionType.getType(), dpId);

		AutoUrpayAndCareMonitoringVO autoUrpayAndCareMonitoringVO = new AutoUrpayAndCareMonitoringVO();
		autoUrpayAndCareMonitoringVO.setName(userInteractionType.getMessage());
		autoUrpayAndCareMonitoringVO.setType(userInteractionType.getType());
		if (null != careConfigDomain) {
			autoUrpayAndCareMonitoringVO.setIsOpen(careConfigDomain.getIsOpen());
		} else {
			autoUrpayAndCareMonitoringVO.setIsOpen(0);
		}
		return autoUrpayAndCareMonitoringVO;
	}


	/**
	 * 查询警告信息
	 *
	 * @param dpId
	 * @param userInteractionType
	 * @return
	 */
	private AutoUrpayAndCareMonitoringVO getAutoWarnMonitoringVO(String dpId, UserInteractionType userInteractionType){
	    AutoUrpayAndCareMonitoringVO autoUrpayAndCareMonitoringVO = new AutoUrpayAndCareMonitoringVO();
	    WarnConfigDomain config = warnConfigRepository.findByDpIdAndWarnType(dpId, userInteractionType.getType());
	    autoUrpayAndCareMonitoringVO.setName(userInteractionType.getMessage());
        autoUrpayAndCareMonitoringVO.setType(userInteractionType.getType());
        if(null != config){
            autoUrpayAndCareMonitoringVO.setIsOpen(config.getIsOpen());
        }else{
            autoUrpayAndCareMonitoringVO.setIsOpen(0);
        }
	    return autoUrpayAndCareMonitoringVO;
	}
}
