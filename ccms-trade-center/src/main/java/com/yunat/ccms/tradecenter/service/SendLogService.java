package com.yunat.ccms.tradecenter.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;

/**
 *
 * 发送历史服务
 * @author shaohui.li
 * @version $Id: SendLogService.java, v 0.1 2013-6-4 下午01:49:55 shaohui.li Exp $
 */
public interface SendLogService {

    /**
     * 根据买家获取短信发送历史
     *
     * @param dpId:店铺Id
     * @param sendDate:发送日期
     * @param buyer：买家昵称
     * @return
     */
    List<SendLogDomain> getSmsLogByBuyer(String dpId,Date sendDate,String buyer,int urpayOrCareType);

    /**
     * 根据手机号码获取短信发送历史
     *
     * @param dpId:店铺Id
     * @param sendDate:发送日期
     * @param mobile：手机号码
     * @return
     */
    List<SendLogDomain> getSmsLogByMobile(String dpId,Date sendDate,String mobile,int urpayOrCareType);

    /**
     * 根据手机号或用户昵称获取下发记录
     * @param dpId
     * @param sendDate
     * @param mobile
     * @param buyerNick
     * @param urpayOrCareType
     * @return
     */
    List<SendLogDomain> getSmsLogByMobileOrBuyer(@Param("dpId")String dpId,@Param("sendDate") Date sendDate,@Param("mobile") String mobile, @Param("buyerNick") String buyerNick,@Param("urpayOrCareType") int urpayOrCareType);

	/**
	 * @param domain
	 */
	void saveSendLogDomain(SendLogDomain domain);

	/**
	 * 发送短信
	 * @param ListT
	 * @param gatewayId
	 * @param ccmsUser
	 * @param passWord
	 * @return
	 */
	BaseResponse<String> sendSMS(List<SmsQueueDomain> ListT, Long gatewayId,String ccmsUser, String passWord);
	
	
	/**
	 * 根据tid和oid  获取评价事务关怀历史发送记录
	 * @author tim.yin
	 */
	
	List<SendLogDomain> getTraderateRegardHistorySendRecord(String tid, String oid);
	
}
