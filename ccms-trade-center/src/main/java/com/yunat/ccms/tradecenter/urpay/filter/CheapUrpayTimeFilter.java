package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;

/**
 *
 * 聚划算时间过滤器
 *
 * @author shaohui.li
 * @version $Id: CheapUrpayTimeFilter.java, v 0.1 2013-6-8 下午08:17:55 shaohui.li Exp $
 */
@Component("cheapUrpayTimeFilter")
public class CheapUrpayTimeFilter extends UrpayTimeFilter{

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult retn = new FilterResult();

        //当前订单
        String tid = order.getTid();

        //当前时间
        Date curTime = new Date();

        //实际催付时间
        Date urpayTime = DateUtils.addMinute(order.getCreated(), ((UrpayConfigDomain)config).getOffset());

        //下单时间 + 用户设置的分钟  > 当前时间，说明还没有到催付时间，则过滤掉
        if(urpayTime.after(curTime)){
            logger.info("订单:[" + tid + "],还未到催付时间，实际催付时间:" + DateUtils.getStringDate(urpayTime));
            retn.setFilter(true);
            return retn;
        }

        //下单后半个小时
        Date createdAfter30m = DateUtils.addMinute(order.getCreated(), 30);
        //当前时间 > 下单时间超过半个小时,直接过滤掉
        if(createdAfter30m.before(curTime)){
            logger.info("订单:[" + tid + "],下单超过半个小时，被过滤,下单后半个小时时间:" + DateUtils.getStringDate(createdAfter30m));
            retn.setFilter(true);
            return retn;
        }
        return filterUrpayTime(curTime,config,false);
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }
}