package com.yunat.ccms.tradecenter.care;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.repository.CareConfigRepository;
import com.yunat.ccms.tradecenter.repository.CareStatusRepository;
import com.yunat.ccms.tradecenter.repository.SmsQueueRepository;
import com.yunat.ccms.tradecenter.service.VariableReplaceService;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.urpay.filter.CareFilterManager;
import com.yunat.ccms.tradecenter.urpay.filter.OrderFilterResult;

/**
 * 订单关怀基类
 *
 * @author shaohui.li
 * @version $Id: OrderBaseCare.java, v 0.1 2013-7-1 下午03:16:21 shaohui.li Exp $
 */
public abstract class OrderBaseCare{

    /** 日志对象 **/
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    /** 过滤管理器 **/
    private CareFilterManager filterManager;

    /** 订单Map key:店铺id value:订单列表 **/
    private Map<String,List<OrderDomain>> ordersMap;

    /** 关怀配置服务 **/
    @Autowired
    private CareConfigRepository careConfigRepository;

    @Autowired
    private CareStatusRepository careStatusRepository;

    @Autowired
    /** 短信队列服务 **/
    protected SmsQueueRepository smsQueueRepository;

    @Autowired
    /** 变量服务 **/
    private VariableReplaceService variableReplaceService;

    /** 设置关怀类型 **/
    public abstract UserInteractionType getCareType();

    /** 设置关怀类型 **/
    public abstract String getCareStatusFieldName();

    /** 处理过滤结果 **/
    public abstract void dealResult(OrderFilterResult filterResult,CareConfigDomain config);

    public Map<String, List<OrderDomain>> getOrdersMap() {
        return ordersMap;
    }

    public void setOrdersMap(Map<String, List<OrderDomain>> ordersMap) {
        this.ordersMap = ordersMap;
    }

    /**
     * 执行下单关怀
     */
    public void doCare() {
        if(ordersMap == null || ordersMap.isEmpty()){
            return;
        }
        for(Map.Entry<String, List<OrderDomain>> entry : ordersMap.entrySet()){
            String dpId = entry.getKey();
            List<OrderDomain> orderList = entry.getValue();
            CareConfigDomain care = careConfigRepository.getByCareTypeAndDpIdAndIsOpen(getCareType().getType(), dpId, 1);
            if(care != null){
                OrderFilterResult result = filterManager.filterOrder(orderList, care);
                dealResult(result,care);
            }else{
                logger.info("店铺:[" + dpId + "] 未开启[" + getCareType().getMessage() + "] 关怀！");
            }
        }
    }



    /**
     * 更新关怀状态
     *
     * @param orderList
     * @param careStatus
     */
    public void updateCareStatus(List<OrderDomain> orderList,int careStatus){
        if(orderList == null){
            return;
        }
        for (OrderDomain orderDomain : orderList) {
            careStatusRepository.inOrUpCareStatus(orderDomain.getTid(),getCareStatusFieldName(), careStatus);
        }
    }

    /**
     * 将订单转化为短信队列
     *
     * @param orderList
     * @param config
     * @param flag
     * @return
     */
    public List<SmsQueueDomain> convert2Sms(List<OrderDomain> orderList, CareConfigDomain config, boolean flag) {
        Calendar ca = null;
        if (flag) {
            ca = Calendar.getInstance();
            ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
            Calendar hours =  Calendar.getInstance();
            hours.setTime(config.getCareStartTime());
            ca.set(Calendar.HOUR_OF_DAY, hours.get(Calendar.HOUR_OF_DAY));
            ca.set(Calendar.MINUTE, 0);
            ca.set(Calendar.SECOND, 0);
        }
        List<SmsQueueDomain> smsQueueList = new ArrayList<SmsQueueDomain>(orderList.size());
        for (OrderDomain orderDomain : orderList) {
            List<Object> list = new ArrayList<Object>(1);
            list.add(orderDomain);
            String smsContent = variableReplaceService.replaceSmsContent(config.getSmsContent(), list);
            if(StringUtils.equalsIgnoreCase(smsContent, "000")){
                logger.info("订单替换变量失败，订单:[" + orderDomain.getTid() + "],被过滤");
                continue;
            }else{
                SmsQueueDomain sms = new SmsQueueDomain();
                sms.setBuyer_nick(orderDomain.getCustomerno());
                sms.setCreated(new Date());
                sms.setDpId(orderDomain.getDpId());
                sms.setMobile(orderDomain.getReceiverMobile());
                //自动发送设置为系统管理员
                sms.setSend_user("system");
                sms.setTid(orderDomain.getTid());
                sms.setTrade_created(orderDomain.getCreated());
                sms.setType(config.getCareType());
                sms.setUpdated(new Date());
                sms.setGatewayId(config.getGatewayId().longValue());
                sms.setSend_time(flag ? ca.getTime() : null);
                sms.setSms_content(smsContent);
                smsQueueList.add(sms);
            }
        }
        return smsQueueList;
    }

    /**
     * 去重
     * @param orderDomainList
     * @return
     */
    public List<OrderDomain> toRepeat(List<OrderDomain> orderDomainList) {
        List<OrderDomain> repeatOrderDomainList = new ArrayList<OrderDomain>();

        List<String> mobiles = new ArrayList<String>();
        List<String> customernos = new ArrayList<String>();
        for (OrderDomain orderDomain : orderDomainList) {
            if (mobiles.contains(orderDomain.getReceiverMobile())) {
                logger.info("订单被去重， 订单id：{}", orderDomain.getTid());
                continue;
            }

            if (customernos.contains(orderDomain.getCustomerno())) {
                logger.info("订单被去重， 订单id：{}", orderDomain.getTid());
                continue;
            }

            mobiles.add(orderDomain.getReceiverMobile());
            customernos.add(orderDomain.getCustomerno());
            repeatOrderDomainList.add(orderDomain);
        }

        return repeatOrderDomainList;
    }
}
