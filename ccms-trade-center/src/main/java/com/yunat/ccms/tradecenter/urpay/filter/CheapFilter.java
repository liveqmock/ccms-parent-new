package com.yunat.ccms.tradecenter.urpay.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 *
 * 聚划算过滤器
 *
 * @author shaohui.li
 * @version $Id: CheapFilter.java, v 0.1 2013-6-4 下午01:16:19 shaohui.li Exp $
 */
@Component("cheapFilter")
public class CheapFilter implements IFilter{

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult result = new FilterResult();
        if(StringUtils.contains(order.getTradeFrom(), "JHS")){
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
