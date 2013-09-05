package com.yunat.ccms.tradecenter.urpay.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.RefundOrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.repository.RefundRepository;
import com.yunat.ccms.tradecenter.service.CareConfigService;
import com.yunat.ccms.tradecenter.service.SmsQueueService;
import com.yunat.ccms.tradecenter.service.VariableReplaceService;
import com.yunat.ccms.tradecenter.support.cons.CareFilterConditionType;
import com.yunat.ccms.tradecenter.support.cons.FilterResultType;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.urpay.filter.FilterManager;
import com.yunat.ccms.tradecenter.urpay.filter.FilterResult;
import com.yunat.ccms.tradecenter.urpay.filter.IFilter;
import com.yunat.ccms.tradecenter.urpay.filter.OrderFilterResult;

/**
 * 退款关怀任务
 *
 * @author shaohui.li
 * @version $Id: RefundCareTask.java, v 0.1 2013-7-15 上午11:29:07 shaohui.li Exp $
 */
public class RefundCareTask extends BaseJob{

    /**  关怀配置服务 **/
    @Autowired
    private CareConfigService careConfigService;

    @Autowired
    /** 退款Dao **/
    RefundRepository refundRepository;

    @Autowired
    /** 过滤 管理器 **/
    FilterManager filterManager;

    @Autowired
    /** 变量替换服务 **/
    VariableReplaceService variableReplaceService;

    @Autowired
    /** 短信队列服务 **/
    SmsQueueService smsQueueService;

    @Override
    public void handle(JobExecutionContext context) {
        List<CareConfigDomain> configList = careConfigService.getByCareType(UserInteractionType.REFUND_CARE.getType());
        if(configList != null && !configList.isEmpty()){
            for(CareConfigDomain config : configList){
                String dpId = config.getDpId();
                long start = System.currentTimeMillis();
                List<RefundOrderDomain> refundOrderList = queryOrders(config);
                long end = System.currentTimeMillis();
                logger.info("查询店铺:[" + dpId + "]退款成功订单所需要的时间为:" + (end - start) + " 毫秒");
                if(refundOrderList != null && !refundOrderList.isEmpty()){
                    OrderFilterResult result = filterOrder(refundOrderList,config);
                    dealResult(result,config);
                }else{
                    logger.info("店铺[" + dpId + "] 本次查询没有满足条件的退款");
                }
            }
        }
    }

    /**
     * 查询订单
     *
     * @param config
     * @return
     */
    List<RefundOrderDomain> queryOrders(CareConfigDomain config){
        return refundRepository.getNotCaredRefundOrders(config.getDpId());
    }

    /**
     * 过滤订单
     *
     * @param orderList
     * @param config
     * @return
     */
    private OrderFilterResult filterOrder(List<RefundOrderDomain> orderList, CareConfigDomain config) {

        OrderFilterResult filterResult = new OrderFilterResult();

        // 待发送列表
        List<OrderDomain> smsList = new ArrayList<OrderDomain>();

        // 去重的订单
        List<OrderDomain> repeatList = new ArrayList<OrderDomain>();

        // 第二天需要发送的订单
        List<OrderDomain> sendNextDayList = new ArrayList<OrderDomain>();

        //永远不会发送的订单
        List<OrderDomain> notSendList = new ArrayList<OrderDomain>();

        // 根据配置获取通用过滤器
        List<IFilter> filters = filterManager.getRefundFilters(config);

        for (OrderDomain order : orderList) {
            try {
                FilterResult result = null;
                // 日常过滤器过滤
                for (IFilter f : filters) {
                    result = f.doFiler(order, config);
                    // 如果被过滤，不能放入待发送列表
                    if (result.isFilter()) {
                        logger.info("订单:[" + order.getTid() + "] 被过滤器:" + f.getFilterName() + " 过滤！");
                        break;
                    }
                }
                //需要发送
                if (!result.isFilter()) {
                    smsList.add(order);
                }
                //第二天发送
                else if (result.isFilter() && FilterResultType.NEXT_DAY.getType().equals(result.getFilteredStatus())) {
                    sendNextDayList.add(order);
                }
                //去重的订单
                else if (result.isFilter() && FilterResultType.REPEAT.getType().equals(result.getFilteredStatus())) {
                    repeatList.add(order);
                }
                //不需要发送的
                else if (result.isFilter() && FilterResultType.NOT_SEND.getType().equals(result.getFilteredStatus())) {
                    notSendList.add(order);
                }
            } catch (Exception ex) {
                logger.error("过滤订单:[" + order.getTid() + "],出现错误", ex);
            }
        }
        filterResult.setSendNextDayList(sendNextDayList);
        filterResult.setRepeatList(repeatList);
        filterResult.setNotSendList(notSendList);
        filterResult.setSmsList(smsList);
        return filterResult;
    }

    /**
     *处理结果
     *
     * @param result
     */
    @Transactional
    void dealResult(OrderFilterResult result,CareConfigDomain config){
        //店铺Id
        String dpId = config.getDpId();

        //去重的订单，但表示为发送
        List<OrderDomain> repeatList = result.getRepeatList();
        updateCareStatus(repeatList,1);

        //第二天需要发送的订单 2: 次日催付
        List<OrderDomain> sendNextDayList = result.getSendNextDayList();
        updateCareStatus(sendNextDayList,2);

        //次日不需要发送的订单，永远不会被发送了
        List<OrderDomain> notSendNextDayList = result.getNotSendList();
        updateCareStatus(notSendNextDayList,3);

        //待发送列表
        List<OrderDomain> smsList = result.getSmsList();
        if(smsList == null || smsList.isEmpty()){
            return;
        }
        //先更新为催付状态，然后去重，对于去重之后的访问队列表
        updateCareStatus(smsList,1);
        //去重操作
        if (StringUtils.contains(config.getFilterCondition(), CareFilterConditionType.TODAY_HAS_SEND.getType())) {
            smsList = toRepeat(smsList);
        }
        logger.info("店铺:[" + dpId + "],过滤之后订单数:" + smsList.size() + ",开始保存待发送队列...");
        //保存待发送的短信,并更新催付状态
        saveSms(smsList,config);
        logger.info("店铺:[" + dpId + "],保存待发送队列完成...");
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

    /**
     * 保存订单到队列表
     *
     * @param smsList
     */
    private void saveSms(List<OrderDomain> smsList,CareConfigDomain config){
        List<SmsQueueDomain> queueList = convertOrder2SmsQueue(smsList,config);
        smsQueueService.saveRefundSmsByBatch(queueList, smsList);
    }

    /**
     *
     *更新关怀状态
     *
     * @param orderList
     * @param statusValue
     */
    private void updateCareStatus(List<OrderDomain> orderList,int statusValue) {
        if(orderList == null || orderList.isEmpty()){
            return;
        }
        String nextSendDate = "";
        if(statusValue == 2){
            nextSendDate = DateUtils.getStringYMD(new Date());
        }
        for(OrderDomain order : orderList){
            refundRepository.updateRefundCareState((RefundOrderDomain)order, statusValue, nextSendDate);
        }
    }

    /**
     * 将订单对象转化为消息对象
     *
     * @param orderList
     * @param config
     * @return
     */
    private List<SmsQueueDomain> convertOrder2SmsQueue(List<OrderDomain> orderList,CareConfigDomain config){
        List<SmsQueueDomain> smsList = new ArrayList<SmsQueueDomain>();
        for(OrderDomain order : orderList){
            List<Object> list = new ArrayList<Object>();
            list.add(order);
            String result = variableReplaceService.replaceSmsContent(config.getSmsContent(),list);
            if(StringUtils.equalsIgnoreCase(result, "000")){
                logger.info("订单替换变量失败，订单:[" + order.getTid() + "],被过滤");
                continue;
            }else{
                SmsQueueDomain sms = convert(order,config,result);
                smsList.add(sms);
            }
        }
        return smsList;
    }

    /**
    *
    * 将订单转化为待发送的短信列表
    * @param order
    * @param config
    * @return
    */
   private SmsQueueDomain convert(OrderDomain order,CareConfigDomain config,String smsStr) {
       SmsQueueDomain sms = new SmsQueueDomain();
       sms.setBuyer_nick(order.getCustomerno());
       sms.setCreated(new Date());
       sms.setDpId(order.getDpId());
       sms.setMobile(order.getReceiverMobile());
       //自动发送设置为系统管理员
       sms.setSend_user("system");
       sms.setSms_content(smsStr);
       sms.setTid(order.getTid());
       sms.setOid(((RefundOrderDomain)order).getOid());
       sms.setTrade_created(order.getCreated());
       sms.setType(UserInteractionType.REFUND_CARE.getType());
       sms.setUpdated(new Date());
       sms.setGatewayId(config.getGatewayId().longValue());
       return sms;
   }
}
