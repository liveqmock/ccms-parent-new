package com.yunat.ccms.tradecenter.urpay.filter;

import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 * 订单金额过滤器
 *
 * @author shaohui.li
 * @version $Id: OrderAmountFilter.java, v 0.1 2013-5-31 上午10:44:01 shaohui.li Exp $
 */
@Component("orderAmountFilter")
public class OrderAmountFilter implements IFilter{

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {

        FilterResult retn = new FilterResult();

        //判断订单金额，金额不在范围之内的，下次不会被选择到
        if(config.getOrderMinAcount() != null){
            int result = order.getPayment().compareTo(config.getOrderMinAcount());
            if(result < 0){
                retn.setFilter(true);
                return retn;
            }
        }
        if(config.getOrderMaxAcount() != null){
            int result = order.getPayment().compareTo(config.getOrderMaxAcount());
            if(result > 0){
                retn.setFilter(true);
                return retn;
            }
        }
        return retn;
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }

}
