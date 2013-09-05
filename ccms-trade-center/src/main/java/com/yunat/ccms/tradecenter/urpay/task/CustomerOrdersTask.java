package com.yunat.ccms.tradecenter.urpay.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.tradecenter.service.CustomerOrdersService;

/**
 * 客服订单统计任务
 *
 * @author ming.peng
 * @date 2013-6-3
 * @since 4.1.0
 */
public class CustomerOrdersTask extends BaseJob {

	@Autowired
	private CustomerOrdersService shipService;

	@Override
	public void handle(JobExecutionContext context) {
		logger.info("客服订单关系统计任务 start");
		long start = System.currentTimeMillis();
		shipService.saveCustomerOrdersShipData();
		long end = System.currentTimeMillis();
		logger.info("客服订单关系统计任务 end, 耗时：[{}]", (end - start));
	}

	@Override
	public void handleBefore(JobExecutionContext context) {
	}


}
