package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.support.cons.OrderStatus;

/**
 * 自动确认收货过滤器
 *
 * @author 李卫林
 *
 */
@Component("autoConfirmFilter")
public class AutoConfirmFilter implements IFilter{

    /** 时间格式 **/
    protected static final String TIME_FORMAT = "HHmmss";

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {

		FilterResult retn = new FilterResult();

		//如果收货时间超出发货时间10天
		if (OrderStatus.TRADE_FINISHED.getStatus().equals(order.getStatus()) && getInterval(order.getEndtime(), order.getConsignTime()) > 10 * 24 * 3600 * 1000) {
			retn.setFilter(true);
			return retn;
		}

        return retn;
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }

    /**
     * 返回两个日期相差的毫秒数
     * date1 - date2
     * @param date1
     * @return
     */
    private long getInterval(Date date1, Date date2) {
    	return date1.getTime() - date2.getTime();
    }
}
