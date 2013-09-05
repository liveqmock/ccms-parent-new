package com.yunat.ccms.tradecenter.support.taobaoapi.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;
import com.yunat.ccms.tradecenter.support.taobaoapi.BaseApiManager;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeTimeManager;
import com.yunat.utility.DateUtilities;

/**
 * 获取淘宝最近交易时间实现类
 *
 * @author shaohui.li
 * @version $Id: TradesSoldGetterImpl.java, v 0.1 2013-8-14 下午06:26:59 shaohui.li Exp $
 */
@Service("taobaoTradeTimeManager")
public class TaobaoTradeTimeManagerImpl extends BaseApiManager implements TaobaoTradeTimeManager{

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    /** 查询字段 **/
	private String fields = "tid,created,modified";

    @Override
    public Date getLastTradeDateByCreated(String shopId,String sessionKey) {
        Date curTime = new Date();
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setFields(fields);
        req.setPageNo(1L);
        req.setPageSize(2L);
        Date endDate = DateUtilities.getCurrentDate();
        Date startDate = DateUtils.addDays(endDate, -1);

        for (int i = 0; i < 3; i++) {
            req.setStartCreated(startDate);
            req.setEndCreated(endDate);
            TradesSoldGetResponse res = null;
            try{
                res = callTaobao(sessionKey, req);
            }catch(Exception ex){
                logger.error("获取店铺:[" + shopId + "] 最近下单时间出错",ex);
            }
            if(res == null){
                return DateUtils.addMinutes(curTime, -5);
            }
            if(!res.isSuccess()){
                logger.info("获取店铺:[" + shopId + "]最近下单时间出错,错误代码:" + res.getErrorCode() + ",错误原因:" + res.getMsg());
                return DateUtils.addMinutes(curTime, -5);
            }
            List<Trade> tradeList = res.getTrades();
            if (CollectionUtils.isNotEmpty(tradeList)) {
                return tradeList.get(0).getCreated();
            }
            endDate = startDate;
            startDate = DateUtils.addDays(endDate, -1);
        }
        return startDate;
    }

    @Override
    public Date getLastTradeDateByModified(String shopId,String sessionKey){
        Date curTime = new Date();
        TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
        req.setFields(fields);
        req.setPageNo(1L);
        req.setPageSize(2L);
        Date endDate = DateUtilities.getCurrentDate();
        Date startDate = DateUtils.addDays(endDate, -1);
        for(int i = 0; i < 3; i++){
            req.setStartModified(startDate);
            req.setEndModified(endDate);
            TradesSoldIncrementGetResponse res = null;
            try{
                res = callTaobao(sessionKey, req);
            }catch(Exception ex){
                logger.error("获取店铺:[" + shopId + "]订单最后修改时间出错",ex);
            }
            if(res == null){
                return DateUtils.addMinutes(curTime, -5);
            }
            if(!res.isSuccess()){
                logger.info("获取店铺:[" + shopId + "]订单最后修改时间出错,错误代码:" + res.getErrorCode() + ",错误原因:" + res.getMsg());
                return DateUtils.addMinutes(curTime, -5);
            }
            List<Trade> tradeList = res.getTrades();
            if (CollectionUtils.isNotEmpty(tradeList)) {
                return tradeList.get(0).getModified();
            }
            endDate = startDate;
            startDate = DateUtils.addDays(endDate, -1);
        }
        return startDate;
    }

}
