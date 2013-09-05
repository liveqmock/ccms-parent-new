package com.yunat.ccms.tradecenter.urpay.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 * 聚划算过滤器(3值)
 *  1:仅聚划算，0：排除聚划算,-1：不限
 * @author 李卫林
 *
 */
@Component("cheapThreeValueFilter")
public class CheapThreeValueFilter implements IFilter{

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
    	FilterResult result = new FilterResult();

    	//如果为仅聚划算
    	if (config.getIncludeCheap() == 1) {
    		if (!StringUtils.contains(order.getTradeFrom(), "JHS")) {
    			 result.setFilter(true);
    	         return result;
    		}
    	}
    	//如果为排除聚划算
    	else if (config.getIncludeCheap() == 0) {
    		if (StringUtils.contains(order.getTradeFrom(), "JHS")) {
    			 result.setFilter(true);
    	         return result;
    		}
    	}

        return result;
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }
}
