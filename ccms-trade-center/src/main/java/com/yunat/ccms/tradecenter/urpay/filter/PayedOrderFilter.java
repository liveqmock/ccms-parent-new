package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.service.OrderService;

/**
 * 已经支付过的订单过滤器
 *  (某买家在这这个店铺中今天跟昨天中有支付过订单，
 *  判断是否近两天有支付通过pay_time付款时间判定
 * 条件：店铺id+买家id+付款时间在两天范围内
 *
 * @author shaohui.li
 * @version $Id: PayedOrderFilter.java, v 0.1 2013-6-3 下午08:09:29 shaohui.li Exp $
 */
@Component("payedOrderFilter")
public class PayedOrderFilter implements IFilter{

    /** 订单服务  **/
    @Autowired
    private OrderService orderService;

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {
        FilterResult retn = new FilterResult();

        //当前时间的前一天
        Date preDay = DateUtils.addDay(new Date(), -1);
        //转化为yyyy-MM-dd格式字符串
        String strDay = DateUtils.getString(preDay) + " 00:00:00";
        //转化为yyyy-MM-dd HH:mm:ss 格式日期
        Date startDate = DateUtils.getDateTime(strDay) ;
        //结束时间
        Date endDate = new Date();
        //查询从前一天的00:00:00 到当前时间所有付款的订单
        List<OrderDomain> list = orderService.findPayedOrder(order.getDpId(), order.getCustomerno(),startDate,endDate);
        if(list != null && !list.isEmpty()){
            retn.setFilter(true);
            return retn;
        }
        return retn;
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }

}
