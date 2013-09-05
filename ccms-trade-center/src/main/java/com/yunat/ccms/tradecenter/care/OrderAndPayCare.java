package com.yunat.ccms.tradecenter.care;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.support.cons.CareFilterConditionType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.urpay.filter.OrderFilterResult;
import com.yunat.ccms.tradecenter.urpay.filter.OrderTimeAndPayTimeFilter;

/**
 * 下单（付款关怀）
 *
 * @author shaohui.li
 * @version $Id: OrderAndPayCare.java, v 0.1 2013-7-2 下午04:01:25 shaohui.li Exp $
 */
@Service("orderAndPayCare")
public class OrderAndPayCare extends OrderBaseCare {

    @Autowired
    OrderTimeAndPayTimeFilter orderTimeAndPayTimeFilter;

    @Override
    public UserInteractionType getCareType() {
        return UserInteractionType.ORDER_CARE;
    }

    @Override
    public String getCareStatusFieldName() {
        return "order_care_status";
    }

    @Override
    public void dealResult(OrderFilterResult filterResult, CareConfigDomain config) {
      //即将被发送的队列
        List<SmsQueueDomain> queueList = new ArrayList<SmsQueueDomain>();
        //店铺Id
        String dpId = config.getDpId();
        //次日不需要发送的订单，永远不会被发送了,直接 更新状态
        List<OrderDomain> notSendList = filterResult.getNotSendList();
        //去重的订单
        List<OrderDomain> repeatList = filterResult.getRepeatList();
        //被过滤的订单
        List<OrderDomain> filteredList = filterResult.getFilteredList();
        //次日催付订单
        List<OrderDomain> sendNextDayList = filterResult.getSendNextDayList();
        //待发送列表
        List<OrderDomain> smsList = filterResult.getSmsList();

        //一、对于去重的订单直接设置为已经发送即可
        updateCareStatus(repeatList,1);

        //二、对于永远不发送不处理
        if(notSendList !=null && !notSendList.isEmpty()){
            logger.info("不处理的订单:" + notSendList.size());
        }
        //三、被过滤掉的，直接修改关怀状态为不发送：3
        if(filteredList != null && !filteredList.isEmpty()){
            updateCareStatus(filteredList,3);
        }
        //四、对于需要第二天发送的和及时需要发送的，标志关怀状态为已经发送：1，并写入队列表
        if(sendNextDayList != null && !sendNextDayList.isEmpty()){
            //更新为催付状态
            updateCareStatus(sendNextDayList,1);
            queueList.addAll(convert2Sms(sendNextDayList,config,true));
        }
        //先更新为催付状态，然后去重，对于去重之后的访问队列表
        updateCareStatus(smsList,1);
        //去重操作
        if (StringUtils.contains(config.getFilterCondition(), CareFilterConditionType.TODAY_HAS_SEND.getType())) {
            smsList = toRepeat(smsList);
        }
        if(smsList != null && !smsList.isEmpty()){
            queueList.addAll(convert2Sms(smsList,config,false));
        }
        logger.info("店铺:[" + dpId + "],过滤之后订单数:" + smsList.size() + ",开始保存待发送队列...");
        //保存到队列
        if (!CollectionUtils.isEmpty(queueList)){
            smsQueueRepository.save(queueList);
            logger.info("店铺:[" + dpId + "],保存待发送队列完成...");
        }
    }
}
