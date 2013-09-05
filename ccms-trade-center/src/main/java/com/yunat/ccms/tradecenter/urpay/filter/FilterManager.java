package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.CareConfigDomain;
import com.yunat.ccms.tradecenter.support.cons.CareFilterConditionType;
import com.yunat.ccms.tradecenter.support.cons.TradeCenterCons;

/**
 *
 * 过滤管理器
 * @author shaohui.li
 * @version $Id: FilterManager.java, v 0.1 2013-6-7 下午03:18:24 shaohui.li Exp $
 */
@Service("filterManager")
public class FilterManager implements ApplicationContextAware{

    private ApplicationContext applicationConext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationConext = context;
    }

    /**
    *
    * 根据催付配置，获取其所有的过滤器
    * 每一种催付，其催付条件过滤器不同
    * @param
    * @return
    */
   public List<IFilter> getCommonFilters(BaseConfigDomain config){
       List<IFilter> filterList = new ArrayList<IFilter>();
       //订单日期过滤器
       filterList.add((IFilter)applicationConext.getBean("orderDateFilter"));

       //手机号码
       filterList.add((IFilter)applicationConext.getBean("mobileFilter"));

       //过滤条件
       if(StringUtils.isNotBlank(config.getFilterCondition())){
           //已经支付的用户
           if(StringUtils.contains(config.getFilterCondition(), "3")){
               filterList.add((IFilter)applicationConext.getBean("payedOrderFilter"));
           }
           //屏蔽短信黑名单用户
           if(StringUtils.contains(config.getFilterCondition(), "4")){
               filterList.add((IFilter)applicationConext.getBean("blacklistFilter"));
           }
       }

       //会员等级
       if(!StringUtils.equals(config.getMemberGrade(), "-1")){
           filterList.add((IFilter)applicationConext.getBean("memberGradeFilter"));
       }

       //订单金额
       if((config.getOrderMinAcount() == null || config.getOrderMinAcount().intValue() != -1) && (config.getOrderMaxAcount() == null || config.getOrderMaxAcount().intValue() != -1)){
           filterList.add((IFilter)applicationConext.getBean("orderAmountFilter"));
       }

       //商品选择
       //TODO

       //聚划算
       if(config.getIncludeCheap() == 0){
           filterList.add((IFilter)applicationConext.getBean("cheapFilter"));
       }
       return filterList;
   }

   /**
   *
   * 根据催付配置，获取其所有的过滤器
   * 每一种催付，其催付条件过滤器不同
   * @param
   * @return
   */
  public List<IFilter> getCareFilters(CareConfigDomain config){
      List<IFilter> filterList = new ArrayList<IFilter>();

      //关怀订单时间过滤器
      filterList.add((IFilter)applicationConext.getBean("careOrderTimeFilter"));

      //手机号码
      filterList.add((IFilter)applicationConext.getBean("mobileFilter"));

      //会员等级
      if(!StringUtils.equals(config.getMemberGrade(), "-1")){
          filterList.add((IFilter)applicationConext.getBean("memberGradeFilter"));
      }

      //订单金额
      if((config.getOrderMinAcount() == null || config.getOrderMinAcount().intValue() != -1) && (config.getOrderMaxAcount() == null || config.getOrderMaxAcount().intValue() != -1)){
          filterList.add((IFilter)applicationConext.getBean("orderAmountFilter"));
      }

      //商品选择
      //TODO

      //三值聚划算过滤
       filterList.add((IFilter)applicationConext.getBean("cheapThreeValueFilter"));

      //过滤条件过滤器
      String filterConditions = config.getFilterCondition();
      if (filterConditions != null) {
          String[] fileterConditionArr = filterConditions.split(TradeCenterCons.FILTER_CONDITION_SEPARATOR);

          for (String filterCondition : fileterConditionArr) {

        	  //如果选中过滤今日已发送
        	  if (CareFilterConditionType.TODAY_HAS_SEND.getType().equals(filterCondition)) {
        		  filterList.add((IFilter)applicationConext.getBean("careTodayHasSendFilter"));
        	  }

        	  //如果选中过滤短信黑名单
        	  if (CareFilterConditionType.BLACKLIST.getType().equals(filterCondition)) {
        		  filterList.add((IFilter)applicationConext.getBean("blacklistFilter"));
        	  }

        	  //如果选择自动确认收货
        	  if (CareFilterConditionType.AUTO_CONFIRM.getType().equals(filterCondition)) {
        		  filterList.add((IFilter)applicationConext.getBean("autoConfirmFilter"));
        	  }
          }
      }

      //关怀时间过滤
      filterList.add((IFilter)applicationConext.getBean("careTimeFilter"));


      return filterList;
  }


  public List<IFilter> getRefundFilters(CareConfigDomain config){
      List<IFilter> filterList = new ArrayList<IFilter>();

      //退款时间过滤器
      filterList.add((IFilter)applicationConext.getBean("refundTimeFilter"));

      //手机号码
      filterList.add((IFilter)applicationConext.getBean("mobileFilter"));

      //会员等级
      if(!StringUtils.equals(config.getMemberGrade(), "-1")){
          filterList.add((IFilter)applicationConext.getBean("memberGradeFilter"));
      }

      //退款金额
      if((config.getOrderMinAcount() == null || config.getOrderMinAcount().intValue() != -1) && (config.getOrderMaxAcount() == null || config.getOrderMaxAcount().intValue() != -1)){
          filterList.add((IFilter)applicationConext.getBean("refundFeeFilter"));
      }

      //商品选择
      //TODO

      //三值聚划算过滤
       filterList.add((IFilter)applicationConext.getBean("cheapThreeValueFilter"));

      //过滤条件过滤器
      String filterConditions = config.getFilterCondition();
      if (filterConditions != null) {
          String[] fileterConditionArr = filterConditions.split(TradeCenterCons.FILTER_CONDITION_SEPARATOR);

          for (String filterCondition : fileterConditionArr) {

              //如果选中过滤今日已发送
              if (CareFilterConditionType.TODAY_HAS_SEND.getType().equals(filterCondition)) {
                  filterList.add((IFilter)applicationConext.getBean("careTodayHasSendFilter"));
              }

              //如果选中过滤短信黑名单
              if (CareFilterConditionType.BLACKLIST.getType().equals(filterCondition)) {
                  filterList.add((IFilter)applicationConext.getBean("blacklistFilter"));
              }

              //如果选择自动确认收货
              if (CareFilterConditionType.AUTO_CONFIRM.getType().equals(filterCondition)) {
                  filterList.add((IFilter)applicationConext.getBean("autoConfirmFilter"));
              }
          }
      }

      //关怀时间过滤
      filterList.add((IFilter)applicationConext.getBean("refundCareTimeFilter"));

      return filterList;
  }

}
