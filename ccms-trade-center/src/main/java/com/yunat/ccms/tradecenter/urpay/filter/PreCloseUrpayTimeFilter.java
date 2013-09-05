package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;


/**
 *
 * 预关闭催付时间过滤器
 *
 * @author shaohui.li
 * @version $Id: PreCloseUrpayTimeFilter.java, v 0.1 2013-5-31 上午11:09:34 shaohui.li Exp $
 */
@Component("preCloseUrpayTimeFilter")
public class PreCloseUrpayTimeFilter extends UrpayTimeFilter{

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {

        FilterResult retn = new FilterResult();

        //订单id
        String tid = order.getTid();

        //下单时间
        Date createdTime = order.getCreated();

        //当前时间
        Date curTime = new Date();

        //实际催付时间 : 为 用户3天时间 - 用户设置的提醒时间

        logger.info("订单:[" + tid + "]下单时间：[" + DateUtils.getStringDate(createdTime) + "], 用户配置:" + ((UrpayConfigDomain)config).getOffset());

        Date urpayTime = DateUtils.addMinute(createdTime, 3 * 24 * 60 - ((UrpayConfigDomain)config).getOffset());

        //下单时间 + 用户设置的分钟  > 当前时间，说明还没有到催付时间，则过滤掉
        if(urpayTime.after(curTime)){
            logger.info("订单:[" + tid + "] 实际催付时间:[" + DateUtils.getStringDate(urpayTime) + "]暂时没到催付时间，被过滤掉...");
            retn.setFilter(true);
            return retn;
        }

        //下单后72小时
        Date createdAfter72h = DateUtils.addMinute(order.getCreated(), 60 * 72);
        //当前时间 > 下单时间超过半个小时,直接过滤掉
        if(createdAfter72h.before(curTime)){
            logger.info("订单:[" + tid + "],下单超过72小时，被过滤,下单后72个小时时间:" + DateUtils.getStringDate(createdAfter72h));
            retn.setFilter(true);
            return retn;
        }
        return filterUrpayTime(curTime,config,false);
    }
}
