package com.yunat.ccms.tradecenter.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.repository.OrderRepository;
import com.yunat.ccms.tradecenter.service.DictService;
import com.yunat.ccms.tradecenter.service.EtlService;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoTradeTimeManager;

/**
 *
 * 店铺相关服务
 *
 * @author shaohui.li
 * @version $Id: ShopService.java, v 0.1 2013-6-7 上午11:33:51 shaohui.li Exp $
 */
@Service("etlService")
public class EtlServiceImpl implements EtlService{

    /** 字典服务 **/
    @Autowired
    DictService dictService;

    //private static final String ETL_TIME_OUT_CODE = "ETL_TIME_OUT";

    @Autowired
    private TaobaoTradeTimeManager taobaoTradeTimeManager;

    @Autowired
    private OrderRepository orderRepository;

    /** 日志对象 **/
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * etl数据是否超时
     *
     * @return
     */
    public boolean isEtlTimeOut(String dpId,int offSet,String sessionKey){

        //淘宝在线查询订单最后修改时间
        Date orderLastModify = taobaoTradeTimeManager.getLastTradeDateByModified(dpId,sessionKey);

        logger.info("店铺:[" + dpId + "] 订单的淘宝最新修改时间:" + DateUtils.getStringDate(orderLastModify));

        //本地库店铺最大修改时间
        Date localMaxModify = orderRepository.getOrderMaxUpdateTime(dpId);

        logger.info("店铺:[" + dpId + "] 订单的本地库最新修改时间:" + DateUtils.getStringDate(localMaxModify));

        if(orderLastModify.getTime() == localMaxModify.getTime()){
            logger.info("店铺:[" + dpId + "] 订单的本地库最新修改时间 ==淘宝最新修改时间,未延迟");
            return false;
        }

        //本地库时间小于线上库时间
        if(localMaxModify.before(orderLastModify)){
            logger.info("店铺:[" + dpId + "] 本地最新时间 < 淘宝最新修改时间");
            long start = localMaxModify.getTime();
            long end = orderLastModify.getTime();
            int m = (int) ((end - start) / (1000 * 60));
            int thirty = offSet / 3;
            if(m >= thirty){
                return true;
            }else{
                return false;
            }
        }else{
            //本地库时间大于线上库时间，这种情况不太可能出现，默认是延迟
            logger.info("店铺:[" + dpId + "] 本地最新时间  >  淘宝最新修改时间");
            return true;
        }
    }

    /**
     * 获取etl默认延迟时间
     *
     * 默认情况不延迟0
     * @see com.yunat.ccms.tradecenter.service.EtlService#getEtlTimeOutMinute()
     */
    @Override
    public int getEtlTimeOutMinute() {
        return 0;
//        DictDomain d = dictService.getByTypeAndCode(ConstantTC.ETL_TIMEOUT_TYPE, ETL_TIME_OUT_CODE);
//        if(d == null){
//            return 0;
//        }else{
//            return Integer.parseInt(d.getName());
//        }
    }
}
