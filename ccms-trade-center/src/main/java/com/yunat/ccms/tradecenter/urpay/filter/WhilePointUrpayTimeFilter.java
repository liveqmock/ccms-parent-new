package com.yunat.ccms.tradecenter.urpay.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.BaseConfigDomain;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.service.EtlService;


/**
 *
 * 整点催付时间过滤器
 *
 * @author shaohui.li
 * @version $Id: WhilePointUrpayTimeFilter.java, v 0.1 2013-6-3 下午01:21:22 shaohui.li Exp $
 */
@Component("whilePointUrpayTimeFilter")
public class WhilePointUrpayTimeFilter implements IFilter{

    /** 时间格式 **/
    private static final String TIME_FORMAT = "HH-mm-ss";

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    /** etl服务 **/
    @Autowired
    EtlService etlService;

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {

        FilterResult retn = new FilterResult();

        //定点催付时间
        String fixUrpayTime = ((UrpayConfigDomain)config).getFixUrpayTime();

        //获取当前催付时间
        String curUrpayTime = curTimeInNotifyTime(fixUrpayTime);

        String tid = order.getTid();

        //催付状态
        int urpayStatus = order.getAutoUrpayStatus();

        //如果当前催付时间为空,说明不用催付
        if(curUrpayTime == null){

            logger.info("订单:[" + tid + "] 当前时点不是用户配置的催付时间");

            //当前催付时间
            Date curTime = new Date();

            //实际可执行催付时间
            Date urpayTime = DateUtils.addMinute(order.getCreated(), ((UrpayConfigDomain)config).getOffset() + etlService.getEtlTimeOutMinute());

            logger.info("订单:[" + tid + "]当前时间:" + DateUtils.getStringDate(curTime) + "实际催付时间:[" + DateUtils.getStringDate(urpayTime) + "]");

            //下单时间 + 用户设置的分钟  > 当前时间，说明还没有到催付时间，则过滤掉
            if(urpayTime.after(curTime)){
                logger.info("订单:[" + tid + "] 未到催付时间，被过滤");
                retn.setFilter(true);
                return retn;
            }

            //当前时点大于用户设置的最后催付时点，判断是否隔日催付
            if(greaterLastPoint(fixUrpayTime)){
                logger.info("订单:[" + tid + "] 当前时点 大于用户设置的最后催付时点,需要判断次日催付");
                //是否需要次日催付
                if(config.getNotifyOption() == 1){
                    logger.info("订单:[" + tid + "] 用户选择次日催付");
                    retn.setFilteredStatus("1");
                }else{
                    retn.setFilteredStatus("2");
                    logger.info("订单:[" + tid + "] 用户选择次日不催付");
                }
            }
            retn.setFilter(true);
            return retn;
         }else{ //当前时点在用户催付时间点，需要催付

             logger.info("订单:[" + tid + "] 当前时点是用户配置的催付时间");

              //催付时间
              String notifyTime =  getDate(new Date(),"yyyy-MM-dd") + " " + curUrpayTime + ":00:00";

              //当前催付时间
              Date curTime = DateUtils.getDateTime(notifyTime);

              //实际可执行催付时间
              Date urpayTime = DateUtils.addMinute(order.getCreated(), ((UrpayConfigDomain)config).getOffset() + etlService.getEtlTimeOutMinute());

              logger.info("订单:[" + tid + "] 实际可执行催付时间:" + DateUtils.getStringDate(urpayTime));

              //下单时间 + 用户设置的分钟  > 当前时间，说明还没有到催付时间，则过滤掉
              if(urpayTime.after(curTime)){
                  logger.info("当前时间:" + DateUtils.getStringDate(curTime) + "实际催付时间:[" + DateUtils.getStringDate(urpayTime) + "]暂时没到催付时间，被过滤掉...");
                  retn.setFilter(true);
                  return retn;
              }
              //最大发送时间
              int maxTime = 60;
              //最大发送时间
              Date maxSendTime = null;
              //如果是次日催付
              if(urpayStatus == 2){
                  logger.info("订单:[" + tid + "] 属于次日催付 ");
                  //当日最早催付
                  Date todayFirstUrpayTime = getFirstUrpayTime(((UrpayConfigDomain)config).getFixUrpayTime());
                  //最大时间 = 当日最早催付 + 60
                  maxSendTime = DateUtils.addMinute(todayFirstUrpayTime,maxTime);
                  if(maxSendTime.before(curTime)){
                      logger.info("订单:[" + tid + "],当前时间超过其最大发送时间:" + DateUtils.getStringDate(maxSendTime) + ",被过滤永远不发送");
                      retn.setFilteredStatus("2");
                      retn.setFilter(true);
                      return retn;
                  }
              }else{
                  String today = DateUtils.getString(new Date()) + " 00:00:00";
                  Date dateOfToday = DateUtils.getDateTime(today);
                  //订单下单时间小于今天
                  if(order.getCreated().before(dateOfToday)){
                      maxSendTime = DateUtils.addMinute(urpayTime,maxTime);
                      //当前时间超过其最大发送时间，则永远不会发送此短信
                      if(maxSendTime.before(curTime)){
                          logger.info("订单:[" + tid + "],当前时间超过其最大发送时间:" + DateUtils.getStringDate(maxSendTime) + ",被过滤永远不发送");
                          retn.setFilteredStatus("2");
                          retn.setFilter(true);
                          return retn;
                      }
                  }
              }
        }
        return retn;
    }

    /** 获取今日最早催付时间 **/
    private Date getFirstUrpayTime(String firstUrpayTime){
        String today = DateUtils.getString(new Date());
        String firstTime = firstUrpayTime.split(",")[0];
        if(firstTime.length() == 1){
            firstTime = "0" + firstTime;
        }
        firstTime = firstTime + ":00:00";
        return DateUtils.getDateTime(today + " " + firstTime);
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }

    /**
     * 当前时点是否在催付时点内
     *
     * @param notifyTime
     * @return
     */
    private String curTimeInNotifyTime(String notifyTime){
        String[] allTime = notifyTime.split(",");
        String curHorse = getDate(new Date(),TIME_FORMAT).split("-")[0];
        for(int i=0;i<allTime.length;i++){
            String h = allTime[i];
            if(h.length() == 1){
                h = "0" + h;
            }
            if(StringUtils.equals(curHorse, h)){
                return allTime[i];
            }
        }
        return null;
    }

    /**
     * 当前时点是否大于用户设置的最后催付时点
     *
     * @param notifyTime
     * @return
     */
    private boolean greaterLastPoint(String notifyTime){
        String[] allTime = notifyTime.split(",");
        String curHorse = getDate(new Date(),TIME_FORMAT).split("-")[0];
        String h = allTime[allTime.length - 1];
        if(h.length() == 1){
            h = "0" + h;
        }
        int curH = Integer.parseInt(curHorse);
        int scanH = Integer.parseInt(h);
        if(curH > scanH){
            return true;
        }
        return false;
    }

    /**
     * 格式化日期
     *
     * @param d
     * @param dateFormat
     * @return
     */
    private String getDate(Date d,String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(d);
    }
}
