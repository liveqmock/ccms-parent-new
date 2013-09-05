package com.yunat.ccms.tradecenter.urpay.task;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.urpay.enums.AutoUrpayTypeEnum;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;

/**
 *
 *自动催付任务
 *
 * @author shaohui.li
 * @version $Id: RealTimeUrpayTask.java, v 0.1 2013-5-31 下午04:53:29 shaohui.li Exp $
 */
public abstract class AutoUrpayTask extends UrpayBaseTask{

    /**
     * 获取当日未付款的订单且未催付的 + 前一天未付款且需次日发送的订单
     *
     *
     *   *getReceiverMobile
         *getTradeFrom
         *getDpId()
         *getCustomerno
         *getMemberGrade
         *getOrderMinAcount
         *getOrderMaxAcount
         *getCreated
         *
     * @see com.yunat.ccms.tradecenter.urpay.task.UrpayBaseTask#getOrders(com.yunat.ccms.tradecenter.domain.UrpayConfigDomain)
     */
    @Override
    public List<OrderDomain> queryOrders(UrpayConfigDomain config) {
        return orderService.getNotPayedAndNotUrpayedOrders(config.getDpId());
    }

    @Override
    public UrpayTypeEnum getJobType() {
        return UrpayTypeEnum.AUTO_URPAY;
    }

    /**
    *
    * 根据任务类型获取对应的催付配置
    * @return
    */
   public List<UrpayConfigDomain> getOpenedUrapyConfig(){
       int jobType = getJobType().getTypeValue();
       int taskType = getTaskType().getTypeValue();
       return urpayConfigService.getUrpayConfigListByType(jobType,taskType);
   }

    /** 设置任务类型 **/
    public abstract AutoUrpayTypeEnum getTaskType();
}
