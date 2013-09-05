package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 * 下单时间与付款时间过滤器
 *
 * @author shaohui.li
 * @version $Id: OrderTimeAndPayTimeFilter.java, v 0.1 2013-7-2 下午04:20:19 shaohui.li Exp $
 */
@Service("orderTimeAndPayTimeFilter")
public class OrderTimeAndPayTimeFilter implements IFilter{

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult retn = new FilterResult();
        CareConfigDomain care = (CareConfigDomain)config;
        //开始时间
        Date startDate = care.getStartDate();
        //结束时间
        Date endDate = null;
        Integer dateNumber = care.getDateNumber();
        if(dateNumber == null){
            endDate = care.getEndDate();
        }else{
            //总是开始，表示结束时间为100年后
            if(dateNumber == 0){
                endDate = DateUtils.addDay(startDate, 365 * 100);
            }else{
                endDate = care.getEndDate();
            }
        }
        //需要比较的时间
        Date compareDate = null;
        if(care.getCareMoment() == 0){
            if(order.getOrderStatus() != 1){
                logger.info("用户选择[下单后发送]，此时状态为非未付款，订单比较过滤！");
                retn.setFilter(true);
                return retn;
            }
            compareDate = order.getCreated();
        }else if(care.getCareMoment() == 1){
            compareDate = order.getPayTime();
        }
        if(compareDate == null){
            logger.info("下单时间或者付款时间为空，订单被过滤掉！");
            retn.setFilter(true);
            return retn;
        }
        //如果下单时间(或者付款时间)不在在开始时间和结束时间之间 ，则此订单被过滤
        if(compareDate.before(startDate)||compareDate.after(endDate)){
            retn.setFilter(true);
            return retn;
        }
        return retn;
    }

    @Override
    public String getFilterName() {
        return OrderTimeAndPayTimeFilter.class.getSimpleName();
    }
}
