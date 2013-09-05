package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.service.SendLogService;
import com.yunat.ccms.tradecenter.service.SmsQueueService;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeGetManger;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;

/**
 *
 * 通过订单过滤器
 *
 * @author shaohui.li
 * @version $Id: CommonOrderFilter.java, v 0.1 2013-6-7 下午07:36:07 shaohui.li Exp $
 */
@Component("commonOrderFilter")
@Scope("prototype")
public class CommonOrderFilter {

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    /** 发送历史服务 **/
    private SendLogService sendLogService;

    @Autowired
    /** 短信队列服务 **/
    SmsQueueService smsQueueService;

    @Autowired
    /** 过滤管理器 **/
    private FilterManager filterManager;

    /** 催付时间过滤 **/
    private IFilter urpayTimeFilter;

    /** 催付或者关怀类型 **/
    private int urpayOrCareType;

    /** 淘宝交易查询 **/
    @Autowired
    private TaobaoTradeGetManger taobaoTradeGetManger;


    public int getUrpayOrCareType() {
        return urpayOrCareType;
    }


    public void setUrpayOrCareType(int urpayOrCareType) {
        this.urpayOrCareType = urpayOrCareType;
    }


    public IFilter getUrpayTimeFilter() {
        return urpayTimeFilter;
    }


    public void setUrpayTimeFilter(IFilter urpayTimeFilter) {
        this.urpayTimeFilter = urpayTimeFilter;
    }


    /**
    *
    * 过滤订单数据
    * 以下方法可以抽取出来，需要结合关怀部分的功能
    * @param orderList
    * @return
    */
   public OrderFilterResult filterOrder(List<OrderDomain> orderList,BaseConfigDomain config,boolean etlTimeOut,String sessionKey){
       OrderFilterResult filterResult = new OrderFilterResult();
       //待发送列表
       List<OrderDomain> smsList = new ArrayList<OrderDomain>();
       //去重的订单
       List<OrderDomain> repeatList = new ArrayList<OrderDomain>();
       //第二天需要发送的订单
       List<OrderDomain> sendNextDayList = new ArrayList<OrderDomain>();
       //永远不会被发送
       List<OrderDomain> notSendList = new ArrayList<OrderDomain>();
       //所有手机
       Map<String,String> mobileMap = new HashMap<String,String>();
       //所有买家
       Map<String,String> buyerMap = new HashMap<String,String>();
       //根据配置获取通用过滤器
       List<IFilter> filters = filterManager.getCommonFilters(config);

       //店铺id
       String dpId = config.getDpId();

       for(OrderDomain order : orderList){
           try{
        	  String mobile = order.getReceiverMobile();
    	      if(StringUtils.isBlank(mobile)){
    	    	  logger.info("订单:[" + order.getTid() + "] 手机号码为空，直接被过滤!");
    	          continue;
    	      }
               boolean filtered = false;
               //日常过滤器过滤
               for(IFilter f : filters){
                   FilterResult result = f.doFiler(order,config);
                   //如果被过滤，不能放入待发送列表
                   if(result.isFilter()){
                       logger.info("订单:[" + order.getTid() + "] 被过滤器:" + f.getFilterName() + " 过滤！");
                       filtered = true;
                       break;
                   }
               }
               //如果被过滤掉，直接跳走
               if(filtered){
                   //如果被去重的手机或者卖家被过滤的话，需要删除map里面的值，否则后面都会被过滤掉
                   //recover(mobileMap,buyerMap,order,config);
                   continue;
               }
               //催付时间过滤器，主要是处理次日发送情况
               IFilter urpayTimeFilter = getUrpayTimeFilter();
               FilterResult result = urpayTimeFilter.doFiler(order, config);
               //如果被过滤掉，同时需要第二天发送
               if(!result.isFilter()){
                   //去重操作
                   if(StringUtils.isNotBlank(config.getFilterCondition())){
                       boolean ok = deDuplication(config,order,mobileMap,buyerMap);
                       if(ok){
                           logger.info("订单:[" + order.getTid() + "]" +  "本批订单有重复用户或者手机号码,被过滤！");
                           repeatList.add(order);
                           continue;
                       }
                   }
                   //对于需要发送出去的订单，必要情况下判断etl是否有延迟
                   if(etlTimeOut){
                       logger.info("店铺:[" + dpId + "] 订单有延迟，需要直接从淘宝检查订单状态");
                       boolean payed = taobaoTradeGetManger.isTradePayed(dpId, Long.parseLong(order.getTid()),sessionKey);
                       if(payed){
                           logger.info("订单:[" + order.getTid() + "] 从淘宝查询已经支付或者被关闭，直接过滤");
                           notSendList.add(order);
                           continue;
                       }
                   }
                   smsList.add(order);
               }else{
                   logger.info("订单:[" + order.getTid() + "] 被过滤器:" + urpayTimeFilter.getFilterName() + " 过滤！");
                   //如果被去重的手机或者卖家被过滤的话，需要删除map里面的值，否则后面都会被过滤掉
                   //recover(mobileMap,buyerMap,order,config);
                   if(this.getUrpayOrCareType() == UrpayTypeEnum.AUTO_URPAY.getTypeValue()){
                       //如果数据被过滤掉，同时需要次日发送
                       if(result.isFilter() && result.getFilteredStatus().equals("1")){
                           sendNextDayList.add(order);
                       }else if(result.isFilter() && result.getFilteredStatus().equals("2")){//如果不需要次日发送的话
                           //如果是自动催付的话，需要将将其状态改成3，不需要次日发送，下次也不会被订单选出
                           logger.info("订单:[" + order.getTid() + "] 次日不会被发送,催付状态变成3，永远不会被发送 !");
                           notSendList.add(order);
                       }
                   }
               }
           }catch(Exception ex){
               logger.error("过滤订单:[" + order.getTid() + "],出现错误",ex);
           }
       }

       filterResult.setSendNextDayList(sendNextDayList);
       filterResult.setNotSendList(notSendList);
       filterResult.setRepeatList(repeatList);
       filterResult.setSmsList(smsList);
       return filterResult;
   }

   /**
   *
   * 根据配置对指定订单进行去重，并返回不需要的订单
   * @param config
   * @param order
   * @param noSendList
   * @param mobileMap
   * @param buyerMap
   * @return
   */
  private boolean deDuplication(BaseConfigDomain config,OrderDomain order,Map<String,String> mobileMap, Map<String,String> buyerMap){
      //买家昵称
      String buyer = order.getCustomerno();
      /**
      *
      * 2、多笔订单只发送一次(判买家昵称)
      *       a、一次扫描对买家去重
      *       b、如果当日已经发过，不在发送
      */
      if(StringUtils.contains(config.getFilterCondition(), "1")){
          //如果当日有发送，则不需要发送
          if(buyerMap.containsKey(buyer)){
              return true;
          }else{
              buyerMap.put(buyer, buyer);
              //如果当日有发送，则不需要发送
              if(isSendToday(order,"1")){
                  logger.info("订单:[" + order.getTid() + "],客户:[" + order.getCustomerno() + "],今天有催付过，不再催付");
                  return true;
              }else{
                  logger.info("订单:[" + order.getTid() + "],客户:[" + order.getCustomerno() + "],今天没被催付过");
              }
          }
       }

      /**
       *  1、多个手机只发送一次(判手机)
              a、一次扫描对手机号码去重
              b、如果当日已经发过，不在发送
       */
      String mobile = order.getReceiverMobile();
      //手机号码不为空，同时用户选择了“多个手机只发送一次” 配置
      if(StringUtils.isNotBlank(mobile) && StringUtils.contains(config.getFilterCondition(), "2")){

          if(mobileMap.containsKey(mobile)){
              return true;
          }else{
              mobileMap.put(mobile, mobile);
              //如果当日有发送，则不需要发送
              if(isSendToday(order,"2")){
                  logger.info("订单:[" + order.getTid() + "],手机:[" + order.getReceiverMobile() + "],今天有催付过，不再催付");
                  return true;
              }else{
                  logger.info("订单:[" + order.getTid() + "],手机:[" + order.getReceiverMobile() + "],今天没被催付过");
              }
          }
      }
      return false;
  }

  /**
   * 判断当前订单当日是否发送过，防止短信没有即使发送，需要从队列表里面查一下
   *
   * 1、从发送历史里面检测
   * 2、从发送队列里面检测
   *
   * @param order
   * @param type:
   *              1：按买家昵称查询
   *              2：按手机号码查询
   * @return
   */
  private boolean isSendToday(OrderDomain order,String checkType) {
      List<SendLogDomain> list = null;
      List<SmsQueueDomain> smsList = null;
      if(StringUtils.equals(checkType, "1")){
          //检查发送历史
          list = sendLogService.getSmsLogByBuyer(order.getDpId(), new Date(), order.getCustomerno(),getUrpayOrCareType());
          if(list != null && !list.isEmpty()){
              return true;
          }else{
              //检查发送队列
              smsList = smsQueueService.querySmsQueueByBuyer(order.getCustomerno(), getUrpayOrCareType(),order.getDpId());
              if(smsList != null && !smsList.isEmpty()){
                  return true;
              }
          }
      }else if(StringUtils.equals(checkType, "2")){
          list = sendLogService.getSmsLogByMobile(order.getDpId(), new Date(), order.getReceiverMobile(),getUrpayOrCareType());
          if(list != null && !list.isEmpty()){
              return true;
          }else{
              //检查发送队列
              smsList = smsQueueService.querySmsQueueByMobile(order.getReceiverMobile(), getUrpayOrCareType(),order.getDpId());
              if(smsList != null && !smsList.isEmpty()){
                  return true;
              }
          }
      }
      return false;
  }

}
