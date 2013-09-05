package com.yunat.ccms.tradecenter.urpay.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.support.cons.FilterResultType;

/**
 * 关怀时间过滤器
 *
 * @author shaohui.li
 * @version $Id: UrpayTimeFilter.java, v 0.1 2013-5-30 下午06:33:54 shaohui.li Exp $
 */
@Component("careTimeFilter")
public class CareTimeFilter implements IFilter{

    /** 时间格式 **/
    protected static final String TIME_FORMAT = "HHmmss";

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {

		FilterResult retn = new FilterResult();

		//如果类型不对，过滤掉
		if (!(config instanceof CareConfigDomain)) {
			retn.setFilter(true);
			return retn;
		}

		CareConfigDomain careConfigDomain  = (CareConfigDomain)config;

        //过滤订单是否超出通知时间
        Long curTimeL = getDateLong(new Date(),TIME_FORMAT);
        Long endTimeL = getDateLong(careConfigDomain.getCareEndTime(),TIME_FORMAT);
        Long startTimeL = getDateLong(careConfigDomain.getCareStartTime(),TIME_FORMAT);

        //当前时间大于催付时间或当前时间小于催付时间
        if(curTimeL > endTimeL || curTimeL < startTimeL){
            retn.setFilter(true);

            //是否需要次日催付
            if(careConfigDomain.getNotifyOption() == 1){
                retn.setFilteredStatus(FilterResultType.NEXT_DAY.getType());
            }

            return retn;
        }

        return retn;
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }

    /**
     * 将日期转化为指定格式，并返回对应的Long值
     *
     * @param d
     * @param dateFormat
     * @return
     */
    protected Long getDateLong(Date d,String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        return new Long(format.format(d));
    }

}
