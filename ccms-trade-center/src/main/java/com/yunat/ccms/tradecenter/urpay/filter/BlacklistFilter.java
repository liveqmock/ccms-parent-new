package com.yunat.ccms.tradecenter.urpay.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.channel.support.domain.MobileBlackList;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.MobileBlackListService;

/**
 * 黑名单过滤器
 *
 * @author shaohui.li
 * @version $Id: BlacklistFilter.java, v 0.1 2013-5-31 上午10:46:32 shaohui.li Exp $
 */
@Component("blacklistFilter")
public class BlacklistFilter implements IFilter {

	@Autowired
	MobileBlackListService mobileBlackListService;

	@Override
	public FilterResult doFiler(final OrderDomain order, final BaseConfigDomain config) {
	    FilterResult result = new FilterResult();
		if (StringUtils.isBlank(order.getReceiverMobile())) {
		    result.setFilter(true);
			return result;
		}
		final MobileBlackList mobileBlackListDomain = mobileBlackListService.getByMobile(order.getReceiverMobile());
		if (null != mobileBlackListDomain) {
		    result.setFilter(true);
            return result;
		}
		return result;
	}

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }
}
