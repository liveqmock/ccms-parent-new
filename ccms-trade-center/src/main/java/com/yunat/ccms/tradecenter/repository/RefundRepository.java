package com.yunat.ccms.tradecenter.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.RefundVO;
import com.yunat.ccms.tradecenter.domain.RefundDomain;
import com.yunat.ccms.tradecenter.domain.RefundOrderDomain;
import com.yunat.ccms.tradecenter.service.queryobject.RefundQuery;

/**
 * 退款订单查询
 *
 * @author shaohui.li
 * @version $Id: RefundRepository.java, v 0.1 2013-7-15 下午04:36:16 shaohui.li Exp $
 */
public interface RefundRepository {

	/**
	 * 得到凭证信息
	 * @return
	 */
	public List<Map<String, Object>> findProofDetail(String dpId);

	/**
	 * 得到所有的常用话术
	 * @param dpId
	 * @return
	 */
	public List<Map<String, Object>> findTopContentList(String dpId);

	/**
	 * 修改退款关怀状态
	 * @param array
	 * @return
	 */
	public int[] refundOrdersCare(Map<String, ?>[] array);

    /**
     *查询当日没有关怀的退款订单
     *以及前一日需要次日催付的退款订单
     *
     * @param dpId
     * @return
     */
    public List<RefundOrderDomain> getNotCaredRefundOrders(String dpId);


    /**
     * 插入或者更新退款关怀状态
     *
     * @param refundOrder
     */
    public void updateRefundCareState(RefundOrderDomain refundOrder,int status,String nextSendDate);

    List<RefundVO> fidRefunds(RefundQuery refundQuery);

    /**
     * 统计状态数量
     * @return
     */
    List<Map<String, Object>> statisticsDealWithStatus(String dpId);

    /**
     * 获取所有退款警告处理的订单,对象中只有oid 和 refund_fee
     * @param dpId
     * @param lastDealTime 上次查询（处理）的时间，对应created时间
     * @return
     */
    List<RefundDomain> getRefundWarnListByDpId(String dpId, Date lastDealTime, Date nowTime);

    /**
     * 统计店铺的退款原因列表
     * @param shopIdList
     * @param recentStatisticsTime
     * @param statisticsEndTime
     */
    List<Map<String, Object>> refundResonStaticsTask(List<String> shopIdList, String recentStatisticsTime, String statisticsEndTime);
}
