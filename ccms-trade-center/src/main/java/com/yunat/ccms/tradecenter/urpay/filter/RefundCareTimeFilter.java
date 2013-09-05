package com.yunat.ccms.tradecenter.urpay.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.support.cons.FilterResultType;

/**
 *
 * 退款关怀时间过滤器
 * @author shaohui.li
 * @version $Id: RefundCareTimeFilter.java, v 0.1 2013-7-15 下午03:50:21 shaohui.li Exp $
 */
@Service("refundCareTimeFilter")
public class RefundCareTimeFilter implements IFilter{

    /** 时间格式 **/
    private static final String TIME_FORMAT = "HHmmss";

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult retn = new FilterResult();
        String tid = order.getTid();
        CareConfigDomain careConfigDomain  = (CareConfigDomain)config;
        //过滤订单是否超出通知时间
        Long curTimeL = getDateLong(new Date(),TIME_FORMAT);
        Long endTimeL = getDateLong(careConfigDomain.getCareEndTime(),TIME_FORMAT);
        Long startTimeL = getDateLong(careConfigDomain.getCareStartTime(),TIME_FORMAT);
        //当前时间小于催付时间
        if(curTimeL < startTimeL){
            logger.info("订单[" + tid + "] 当前时间小于催付时间，被过滤");
            retn.setFilter(true);
            return retn;
        }
        //当前时间大于催付时间
        if(curTimeL > endTimeL){
            logger.info("订单[" + tid + "] 当前时间大于催付时间，被过滤");
            retn.setFilter(true);
            //是否需要次日催付
            if(config.getNotifyOption() == 1){
                retn.setFilteredStatus(FilterResultType.NEXT_DAY.getType());
            }else{
                retn.setFilteredStatus(FilterResultType.NOT_SEND.getType());
            }
            return retn;
        }
        return retn;
    }

    @Override
    public String getFilterName() {
        return RefundCareTimeFilter.class.getSimpleName();
    }

    private Long getDateLong(Date d,String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        return new Long(format.format(d));
    }
}
