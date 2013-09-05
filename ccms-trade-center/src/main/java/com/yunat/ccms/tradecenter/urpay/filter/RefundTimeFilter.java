package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.RefundOrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;

/**
 * 退款时间过滤器
 *
 * @author shaohui.li
 * @version $Id: RefundTimeFilter.java, v 0.1 2013-7-15 下午03:59:26 shaohui.li Exp $
 */
@Service("refundTimeFilter")
public class RefundTimeFilter implements IFilter{

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult retn = new FilterResult();
        //开始时间
        Date startDate = config.getStartDate();
        //判断是否为催付
        if(config instanceof UrpayConfigDomain){
            //如果是预先关闭催付，订单时间为开始时间的前3天
            if(((UrpayConfigDomain)config).getUrpayType() == UrpayTypeEnum.PRE_CLOSE_URPAY.getTypeValue()){
                startDate = DateUtils.addDay(startDate,-3);
            }
        }
        //结束时间
        Date endDate = null;
        Integer dateNumber = config.getDateNumber();
        if(dateNumber == null){
            endDate = config.getEndDate();
        }else{
            //总是开始，表示结束时间为100年后
            if(dateNumber == 0){
                endDate = DateUtils.addDay(startDate, 365 * 100);
            }else{
                endDate = config.getEndDate();
            }
        }
        RefundOrderDomain refundOrder = (RefundOrderDomain)order;
        //如果退款时间在开始时间之间，或者在结束时间 + 1 之后,则此订单被过滤
        if(refundOrder.getRefundTime().before(startDate)||refundOrder.getRefundTime().after(endDate)){
            retn.setFilter(true);
            return retn;
        }
        return retn;
    }

    @Override
    public String getFilterName() {
        return RefundTimeFilter.class.getSimpleName();
    }

}
