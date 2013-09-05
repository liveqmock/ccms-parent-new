package com.yunat.ccms.tradecenter.urpay.task;

import com.yunat.ccms.tradecenter.service.RefundService;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: weilin.li
 * Date: 13-8-8
 * Time: 上午10:54
 */
public class RefundResonStaticsTask extends BaseJob{

    @Autowired
    private RefundService refundService;

    @Override
    public void handle(JobExecutionContext context) {
        refundService.refundResonStaticsTask();
    }
}
