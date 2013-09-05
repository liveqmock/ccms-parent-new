package com.yunat.ccms.tradecenter.service;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;


/**
 * 用户交互服务
 * 关怀、催付、旺旺
 * @author 李卫林
 *
 */
public interface OrderInteractionService {

	/**
	 * 退款旺旺，手机关怀
	 * @param tids
	 * @param content
	 * @param type
	 */
	void refundOrdersCare(CaringRequest req, Integer type);

	/**
	 * 退款短信关怀
	 * @param tids
	 * @param oids
	 * @param content
	 * @param gatewayId
	 * @param type
	 * @param filterBlacklist
	 */
	BaseResponse<String> refundOrdersSMSCare(CaringRequest req, Integer type);

	/**
	 * 催付功能
	 * @param tid
	 * @param smsContent
	 * @param getwayId
	 * @param filterBlacklist TODO
	 * @return TODO
	 */
	BaseResponse<String> urpayOrder(String tid, String smsContent, Long getwayId, Boolean filterBlacklist);

	/**
	 * 批量催付
	 * @param tids
	 * @param smsContent
	 * @param getwayId
	 * @param filterBlacklist TODO
	 * @return TODO
	 */
	BaseResponse<String> urpayOrders(String[] tids, String smsContent, Long getwayId, Boolean filterBlacklist);

	/**
	 * 旺旺催付功能
	 * @param tid
	 * @param note TODO
	 */
	void wwUrpayOrder(String tid, String note);

	/**
	 * 旺旺批量催付
	 * @param tids
	 * @param note TODO
	 */
	void wwUrpayOrders(String[] tids, String note);

	/**
	 * 发货短信 关怀 或 批量关怀
	 * @param tids
	 * @param smsContent
	 * @param getwayId
	 * @param filterBlacklist
	 * @return
	 */
	BaseResponse<String> careOrders(String[] tids, String smsContent, Long gatewayId, Integer type, Boolean filterBlacklist);

	/**
	 * 发货 旺旺，电话 关怀或批量关怀
	 * @param tids
	 * @param content
	 * @return
	 */
	void careOrders(String[] tids, String content, Integer type);

}
