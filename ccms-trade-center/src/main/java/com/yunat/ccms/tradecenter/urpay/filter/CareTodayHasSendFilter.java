package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.service.SmsQueueService;
import com.yunat.ccms.tradecenter.support.cons.FilterResultType;

/**
 * 排除今天已经发过的用户（关怀类型）
 * @author 李卫林
 *
 */
@Component("careTodayHasSendFilter")
public class CareTodayHasSendFilter implements IFilter {

	@Autowired
	private SendLogService sendLogService;

	@Autowired
	private SmsQueueService smsQueueService;


	@Override
	public String getFilterName() {
		return getClass().getSimpleName();
	}

	@Override
	public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
		FilterResult retn = new FilterResult();

		//如果类型不对，过滤掉
		if (!(config instanceof CareConfigDomain)) {
			retn.setFilter(true);
			return retn;
		}

		CareConfigDomain careConfigDomain  = (CareConfigDomain)config;

		//查看今日关怀记录 -- 下发表
		List<SendLogDomain> sendLogDomainList = sendLogService.getSmsLogByMobileOrBuyer(order.getDpId(), new Date(), order.getReceiverMobile(), order.getCustomerno(), careConfigDomain.getCareType());

		if (sendLogDomainList.size() > 0) {
			retn.setFilteredStatus(FilterResultType.REPEAT.getType());
			retn.setFilter(true);
			return retn;
		}

		//短信队列表
		List<SmsQueueDomain> smsQueueDomainList = smsQueueService.queryByMobileOrBuyer(order.getReceiverMobile(), order.getCustomerno(), careConfigDomain.getCareType(), order.getDpId(), new Date());
		if (smsQueueDomainList.size() > 0) {
			retn.setFilteredStatus(FilterResultType.REPEAT.getType());
			retn.setFilter(true);
			return retn;
		}



		return retn;
	}
}
