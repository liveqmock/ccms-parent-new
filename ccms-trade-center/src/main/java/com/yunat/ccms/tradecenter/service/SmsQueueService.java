package com.yunat.ccms.tradecenter.service;

import java.util.Date;
import java.util.List;

import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;

/**
 *
 * 短信队列服务
 * @author shaohui.li
 * @version $Id: SmsQueueService.java, v 0.1 2013-5-31 下午08:05:45 shaohui.li Exp $
 */
public interface SmsQueueService {

    /**
     *
     * 批量保存待发送短信队列
     * @param SmsQueueDomain
     */
    public void saveSmsQueueByBatch(List<SmsQueueDomain> smsList);

    /**
	 * @return
	 */
	List<SmsQueueDomain> quarySmsQueueList();


	/**
	 * 将需要发送的短信，更新其thread
	 *
	 * @param thread
	 */
	public void updateQueue(String thread);

	/**
	 * 根据thread查询订单
	 *
	 * @param thread
	 * @return
	 */
	List<SmsQueueDomain> querySmsQueueByThread(String thread);

	/** 根据thread 删除**/
	public void deleteSmsByThread(String thread);

    /**
     *
     *批量保存待发送短信队列，并更新催付状态
     * @param smsList
     * @param list
     * @param jobType
     */
    public void saveSmsQueueByBatch(List<SmsQueueDomain> smsList,List<UrpayStatusDomain> list,String jobType);


    public void saveRefundSmsByBatch(List<SmsQueueDomain> smsList,List<OrderDomain> orderList);

	/**
	 * @param sms
	 */
	void deleteSmsQueue(List<SmsQueueDomain> smsList);

	/**
	 * 根据买家昵称从发送队列表里面 找数据
	 *
	 * @param buyer
	 * @return
	 */
	List<SmsQueueDomain> querySmsQueueByBuyer(String buyer,int urpayType,String dpId);

	/**
	 * 根据手机号码从发送队列表里面 找数据
	 *
	 * @param mobile
	 * @return
	 */
	List<SmsQueueDomain> querySmsQueueByMobile(String mobile,int urpayType,String dpId);

	/**
	 * 查找制定日期
	 * @param mobile
	 * @param buyerNick
	 * @param urpayType
	 * @param dpId
	 * @param date
	 * @return
	 */
	List<SmsQueueDomain> queryByMobileOrBuyer(String mobile, String buyerNick, int urpayType, String dpId, Date date);

}
