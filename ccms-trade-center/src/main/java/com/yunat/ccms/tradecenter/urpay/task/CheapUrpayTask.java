package com.yunat.ccms.tradecenter.urpay.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;
import com.yunat.ccms.tradecenter.urpay.filter.CheapUrpayTimeFilter;
import com.yunat.ccms.tradecenter.urpay.filter.IFilter;

/**
 *
 * 聚划算催付
 *
 * @author shaohui.li
 * @version $Id: CheapUrpayTask.java, v 0.1 2013-6-7 下午06:29:02 shaohui.li Exp $
 */
public class CheapUrpayTask extends UrpayBaseTask{

    @Autowired
    CheapUrpayTimeFilter cheapUrpayTimeFilter;

    @Override
    public List<UrpayConfigDomain> getOpenedUrapyConfig() {
        return urpayConfigService.getUrpayConfigListByType(getJobType().getTypeValue(), -1);
    }

    /**
     *
     *订单查询
     *
     * 只选择当日订单，未付款且未未催付
     * 且订单来源为聚划算催付
     *
     *
     * @see com.yunat.ccms.tradecenter.urpay.task.UrpayBaseTask#queryOrders(com.yunat.ccms.tradecenter.domain.UrpayConfigDomain)
     */
    @Override
    public List<OrderDomain> queryOrders(UrpayConfigDomain config) {
        return orderService.getCheapOrders(config.getDpId());
    }

    @Override
    public UrpayTypeEnum getJobType() {
        return UrpayTypeEnum.CHEAP_URPAY;
    }

    @Override
    public IFilter getUrpayTimeFilter() {
        return cheapUrpayTimeFilter;
    }

}
