package com.yunat.ccms.tradecenter.urpay.filter;

import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.RefundOrderDomain;

/**
 * 退款金额过滤器
 *
 * @author shaohui.li
 * @version $Id: RefundFeeFilter.java, v 0.1 2013-7-15 下午04:02:56 shaohui.li Exp $
 */
@Service("refundFeeFilter")
public class RefundFeeFilter implements IFilter{

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult retn = new FilterResult();

        RefundOrderDomain refundOrder = (RefundOrderDomain)order;

        //判断订单金额，金额不在范围之内的，下次不会被选择到
        if(config.getOrderMinAcount() != null){
            int result = refundOrder.getRefundFee().compareTo(config.getOrderMinAcount());
            if(result < 0){
                retn.setFilter(true);
                return retn;
            }
        }
        if(config.getOrderMaxAcount() != null){
            int result = refundOrder.getRefundFee().compareTo(config.getOrderMaxAcount());
            if(result > 0){
                retn.setFilter(true);
                return retn;
            }
        }
        return retn;
    }

    @Override
    public String getFilterName() {
        return RefundFeeFilter.class.getSimpleName();
    }

}
