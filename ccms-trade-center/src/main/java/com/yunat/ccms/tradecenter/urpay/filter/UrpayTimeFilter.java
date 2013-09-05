package com.yunat.ccms.tradecenter.urpay.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * 实时催付时间过滤器
 *
 * @author shaohui.li
 * @version $Id: UrpayTimeFilter.java, v 0.1 2013-5-30 下午06:33:54 shaohui.li Exp $
 */
@Component("urpayTimeFilter")
public class UrpayTimeFilter implements IFilter{

    /** 时间格式 **/
    protected static final String TIME_FORMAT = "HHmmss";

    /** 日志对象 **/
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /** etl服务 **/
    @Autowired
    EtlService etlService;

    /** 订单号 **/
    private String tid = "";

    @Override
    public FilterResult doFiler(OrderDomain order, BaseConfigDomain config) {

        String tid = order.getTid();

        FilterResult retn = new FilterResult();

        //当前时间
        Date curTime = new Date();

        //实际催付时间
        Date urpayTime = DateUtils.addMinute(order.getCreated(), ((UrpayConfigDomain)config).getOffset() + etlService.getEtlTimeOutMinute());

        logger.info("时间催付时间:" + DateUtils.getStringDate(urpayTime));

        //下单时间 + 用户设置的分钟  > 当前时间，说明还没有到催付时间，则过滤掉
        if(urpayTime.after(curTime)){
            logger.info("订单:[" + tid + "] 实际催付时间:[" + DateUtils.getStringDate(urpayTime) + "]暂时没到催付时间，被过滤掉...");
            retn.setFilter(true);
            return retn;
        }
        //最大发送时间
        int maxTime = 60;
        //催付状态
        int urpayStatus = order.getAutoUrpayStatus();
        //最大发送时间
        Date maxSendTime = null;

        //当日最早催付时间
        Date startTime = ((UrpayConfigDomain)config).getUrpayStartTime();

        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        String firstUrpayTime = format.format(startTime);
        Date todayFirstUrpayTime = getFirstUrpayTime(firstUrpayTime);
        logger.info("订单:[" + tid + "],当日最早发送时间:" + DateUtils.getStringDate(todayFirstUrpayTime));

        if(urpayStatus == 2){//次日催付
            logger.info("订单:[" + tid + "] 属于次日催付 ");
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
        return filterUrpayTime(curTime,config,true);
    }

    /** 获取今日最早催付时间 **/
    private Date getFirstUrpayTime(String firstUrpayTime){
        String today = DateUtils.getString(new Date());
        return DateUtils.getDateTime(today + " " + firstUrpayTime);
    }

    @Override
    public String getFilterName() {
        return getClass().getSimpleName();
    }

    /**
     *
     *过滤催付时间
     *
     * @param curTime：当前时间
     * @param config：催付配置
     * @return
     */

    protected FilterResult filterUrpayTime(Date curTime,BaseConfigDomain config,boolean sendNextDay){

        FilterResult retn = new FilterResult();

        //过滤订单是否超出通知时间
        Long curTimeL = getDateLong(curTime,TIME_FORMAT);
        Long endTimeL = getDateLong(((UrpayConfigDomain)config).getUrpayEndTime(),TIME_FORMAT);
        Long startTimeL = getDateLong(((UrpayConfigDomain)config).getUrpayStartTime(),TIME_FORMAT);

        //当前时间小于催付时间
        if(curTimeL < startTimeL){
            logger.info("订单[" + tid + "] 当前时间小于催付时间，被过滤");
            retn.setFilter(true);
            return retn;
        }
        //当前时间大于催付时间
        if(curTimeL > endTimeL){
            logger.info("订单[" + tid + "] 当前时间大于催付时间，被过滤");
            retn.setFilter(true);
            //是否需要判断次日催付
            if(sendNextDay){
                //是否需要次日催付
                if(config.getNotifyOption() == 1){
                    retn.setFilteredStatus("1");
                }else{
                    retn.setFilteredStatus("2");
                }
            }
            return retn;
        }
        return retn;
    }

    /**
     * 将日期转化为指定格式，并返回对应的Long值
     *
     * @param d
     * @param dateFormat
     * @return
     */
    protected Long getDateLong(Date d,String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        return new Long(format.format(d));
    }

}
