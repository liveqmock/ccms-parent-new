package com.yunat.ccms.tradecenter.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.RefundOrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;
import com.yunat.ccms.tradecenter.repository.BatchRepository;
import com.yunat.ccms.tradecenter.repository.RefundRepository;
import com.yunat.ccms.tradecenter.repository.SendLogRepository;
import com.yunat.ccms.tradecenter.repository.SmsQueueRepository;
import com.yunat.ccms.tradecenter.service.SmsQueueService;
import com.yunat.ccms.tradecenter.service.UrpayStatusService;

/**
 *
 *短信队列服务 接口实现类
 *
 * @author shaohui.li
 * @version $Id: SmsQueueServiceImp.java, v 0.1 2013-5-31 下午08:17:13 shaohui.li Exp $
 */
@Service("smsQueueService")
public class SmsQueueServiceImp implements SmsQueueService{

    /** 批量保存Repository **/
    @Autowired
    BatchRepository batchRepository;

    @Autowired
    UrpayStatusService urpayStatusService;

    @Autowired
    SendLogRepository sendLogRepository;

    @Autowired
    SmsQueueRepository smsQueueRepository;

    @Autowired
    /** 退款Dao **/
    RefundRepository refundRepository;

    @Override
    public void saveSmsQueueByBatch(List<SmsQueueDomain> smsList) {
        batchRepository.batchInsert(smsList);
    }

    @Override
    @Transactional
    public void saveSmsQueueByBatch(List<SmsQueueDomain> smsList, List<UrpayStatusDomain> list,String jobType) {
        batchRepository.batchInsert(smsList);
        urpayStatusService.insertUrpayStatusBatch(list, jobType);
    }

    @Override
    public List<SmsQueueDomain> quarySmsQueueList(){
    	return smsQueueRepository.querySmsQueueBySendTime(new Date());
    }

	@Override
	public void deleteSmsQueue(List<SmsQueueDomain> smsList) {
		smsQueueRepository.deleteInBatch(smsList);
	}

    @Override
    public List<SmsQueueDomain> querySmsQueueByBuyer(String buyer,int urpayType,String dpId) {
        return smsQueueRepository.querySmsQueueByBuyer(buyer, urpayType,dpId);
    }

    @Override
    public List<SmsQueueDomain> querySmsQueueByMobile(String mobile,int urpayType,String dpId) {
        return smsQueueRepository.querySmsQueueByMobile(mobile, urpayType,dpId);
    }

	@Override
	public List<SmsQueueDomain> queryByMobileOrBuyer(String mobile, String buyerNick, int urpayType, String dpId,
			Date date) {
		// TODO Auto-generated method stub
		Date startTime = DateUtils.dateStart(date);
		Date endTime = DateUtils.dateEnd(date);

		return smsQueueRepository.queryByMobileOrBuyer(mobile, buyerNick, urpayType, dpId, startTime, endTime);
	}

    @Override
    @Transactional
    public void saveRefundSmsByBatch(List<SmsQueueDomain> smsList, List<OrderDomain> orderList) {
        int statusValue = 1;
        String nextSendDate = "";
        batchRepository.batchInsert(smsList);
        for(OrderDomain order : orderList){
            refundRepository.updateRefundCareState((RefundOrderDomain)order, statusValue, nextSendDate);
        }
    }

    @Override
    public void updateQueue(String thread) {
        smsQueueRepository.updateSmsQueueByThread(thread, new Date());
    }

    @Override
    public List<SmsQueueDomain> querySmsQueueByThread(String thread) {
        return smsQueueRepository.querySmsQueueByThead(thread);
    }

    @Override
    public void deleteSmsByThread(String thread) {
        smsQueueRepository.deleteSmsQueueByThread(thread);
    }
}
