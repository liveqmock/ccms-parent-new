package com.yunat.ccms.tradecenter.service;

import java.util.Date;
import java.util.List;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.tradecenter.domain.RefundDomain;

public interface RefundWarnService {

	/**
	 * 获取当前所有的退款订单,created从上个执行时间开始
	 * @param nowTime
	 * @return
	 */
	List<RefundDomain> getRefundListByDpId(String dpId, Date lastDealTime, Date nowTime);

	/**
	 * 发送退款告警短信
	 * @param moblies 手机号，逗号分隔
	 * @param gatewayId 通道id若为空，则由系统自动获得一个通道
	 * @param dpId
	 * @param content
	 * @param dpNick
	 * @return
	 */
	BaseResponse<String> sendRefundWarnSms(String moblies, Long gatewayId, String dpId, String content, String dpNick);

	void record(List<String> oids, String thread);

	/**
	 * 找出先前没有处理过的退款订单
	 * @param oids
	 * @return
	 */
	List<String> findNotGoodUnDealEarly(List<String> oids);

}
