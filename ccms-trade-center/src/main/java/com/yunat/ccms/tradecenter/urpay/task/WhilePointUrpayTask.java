package com.yunat.ccms.tradecenter.urpay.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.tradecenter.urpay.enums.AutoUrpayTypeEnum;
import com.yunat.ccms.tradecenter.urpay.filter.IFilter;
import com.yunat.ccms.tradecenter.urpay.filter.WhilePointUrpayTimeFilter;

/**
 * 自动催付 -- 整点催付任务
 *
 * 不做任务实际动作，所有操作，均在父类执行
 *
 * 增加本类，只因为quartz调度的时候，实时与定时器催付的执行点不同
 *
 * 便于quartz好配置
 *
 * 执行时间(每小时执行一次)
 *
 * @author shaohui.li
 * @version $Id: RealTimeUrpayTask.java, v 0.1 2013-6-6 上午11:31:02 shaohui.li Exp $
 */
public class WhilePointUrpayTask extends AutoUrpayTask{

    @Autowired
    WhilePointUrpayTimeFilter whilePointUrpayTimeFilter;

    @Override
    public AutoUrpayTypeEnum getTaskType() {
        return AutoUrpayTypeEnum.WHILE_POINT_URPAY;
    }

    @Override
    public IFilter getUrpayTimeFilter() {
        return whilePointUrpayTimeFilter;
    }

}
