package com.yunat.ccms.tradecenter.urpay.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;
import com.yunat.ccms.tradecenter.urpay.filter.IFilter;
import com.yunat.ccms.tradecenter.urpay.filter.PreCloseUrpayTimeFilter;

/**
 * 预关闭催付任务
 *
 * @author shaohui.li
 * @version $Id: PreClosedUrpayTask.java, v 0.1 2013-6-7 下午06:22:52 shaohui.li Exp $
 */
public class PreClosedUrpayTask extends UrpayBaseTask{

    @Autowired
    PreCloseUrpayTimeFilter preCloseUrpayTimeFilter;

    @Override
    public List<UrpayConfigDomain> getOpenedUrapyConfig() {
        return urpayConfigService.getUrpayConfigListByType(getJobType().getTypeValue(), -1);
    }

    /**
     * 查询需要的订单
     *
     * 选择订单的范围是：
     *
     * 下单时间在：以当前时间为结束点，向前推3天的时间为开始点，所有未付款，未催付的订单,且不包括聚划算订单
     *
     *
     *
     * @see com.yunat.ccms.tradecenter.urpay.task.UrpayBaseTask#queryOrders(com.yunat.ccms.tradecenter.domain.UrpayConfigDomain)
     */
    @Override
    public List<OrderDomain> queryOrders(UrpayConfigDomain config) {
        return orderService.getPreclosedOrders(config.getDpId());
    }

    @Override
    public UrpayTypeEnum getJobType() {
        return UrpayTypeEnum.PRE_CLOSE_URPAY;
    }

    @Override
    public IFilter getUrpayTimeFilter() {
        return preCloseUrpayTimeFilter;
    }
}
