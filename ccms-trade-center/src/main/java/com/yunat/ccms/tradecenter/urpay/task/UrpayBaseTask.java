package com.yunat.ccms.tradecenter.urpay.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.SmsQueueDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.domain.UrpayStatusDomain;
import com.yunat.ccms.tradecenter.service.EtlService;
import com.yunat.ccms.tradecenter.service.OrderService;
import com.yunat.ccms.tradecenter.service.ShopService;
import com.yunat.ccms.tradecenter.service.SmsQueueService;
import com.yunat.ccms.tradecenter.service.UrpayConfigService;
import com.yunat.ccms.tradecenter.service.UrpayStatusService;
import com.yunat.ccms.tradecenter.service.VariableReplaceService;
import com.yunat.ccms.tradecenter.urpay.enums.UrpayTypeEnum;
import com.yunat.ccms.tradecenter.urpay.filter.CommonOrderFilter;
import com.yunat.ccms.tradecenter.urpay.filter.IFilter;
import com.yunat.ccms.tradecenter.urpay.filter.OrderFilterResult;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

/**
 *
 *所有催付任务的基类
 *
 * @author shaohui.li
 * @version $Id: UrpayBaseTask.java, v 0.1 2013-5-30 下午04:22:23 shaohui.li Exp $
 */
public abstract class UrpayBaseTask extends BaseJob{

    /** 日志对象 **/
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    /** 催付配置查询服务 **/
    protected UrpayConfigService urpayConfigService;

    @Autowired
    /** 待发送短信队列服务 **/
    private SmsQueueService smsQueueService;

    @Autowired
    /** 订单催付状态服务 **/
    private UrpayStatusService urpayStatusService;

    @Autowired
    /** 订单服务 **/
    protected OrderService orderService;

    /** 当前执行线程 **/
    private String thread ;

    @Autowired
    /** 店铺服务 **/
    private ShopService shopService;

    @Autowired
    /** Etl服务 **/
    private EtlService etlService;

    @Autowired
    /** 通用订单过滤器 **/
    CommonOrderFilter commonOrderFilter;

    @Autowired
    /** 变量替换服务 **/
    VariableReplaceService variableReplaceService;

    /** sessionKey访问服务 **/
    @Autowired
    private AccessTokenService accessTokenService;

    @Override
    public void handle(JobExecutionContext context) {
        doTask();
    }

    /**
     * 执行任务
     */
    public void doTask(){
        //当日执行线程
        thread = DateUtils.getStringDateName(new Date());
        //获取打开催付的配置列表
        List<UrpayConfigDomain> configList = getOpenedUrapyConfig();
        if(configList != null){
            for(UrpayConfigDomain config : configList){
                //店铺Id
                String dpId = config.getDpId();
                if(!isValidShop(dpId)){
                    logger.info("开始店铺:[" + dpId + "],状态无效，不发送任何短信");
                    continue;
                }
                logger.info("开始店铺:[" + dpId + "],开始查询订单...");
                long start = System.currentTimeMillis();
                List<OrderDomain> orderList = queryOrders(config);
                long end = System.currentTimeMillis() - start;
                logger.info("开始店铺:[" + dpId + "],查询订单完成,总共：" + end + " 毫秒");
                if(orderList != null && !orderList.isEmpty()){
                    logger.info("店铺:[" + dpId + "],查询到的订单数:" + orderList.size() + ",开始过滤订单...");

                    int offset = 0;
                    //如果是预关闭
                    if(getJobType() == UrpayTypeEnum.PRE_CLOSE_URPAY){
                        offset = 72 * 60 - config.getOffset();
                    }else{
                        offset = config.getOffset();
                    }
                    //获取sessionKey
                    AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, dpId);
                    String sessionKey = "";
                    boolean etlTimeout = false;
                    if(accessToken == null){
                        logger.info("获取店铺:[" + dpId + "] 的sessionKey失败,etl过期状态为：未延迟");
                    }else{
                        sessionKey = accessToken.getAccessToken();
                        etlTimeout = isEtlTimeout(dpId,offset,sessionKey);
                        logger.info("店铺:[" + dpId + "],的sessionkey:" + sessionKey);
                    }
                    logger.info("店铺:[" + dpId + "],ETL状态:" + etlTimeout);

                    //订单过滤
                    commonOrderFilter.setUrpayTimeFilter(getUrpayTimeFilter());
                    commonOrderFilter.setUrpayOrCareType(getJobType().getTypeValue());
                    OrderFilterResult filterResult = commonOrderFilter.filterOrder(orderList,config,etlTimeout,sessionKey);
                    //处理结果
                    dealResult(filterResult,config);
                }
            }
         }
    }

    /**
     * 处理过滤结果
     *
     * @param filterResult
     * @param config
     */
    void dealResult(OrderFilterResult filterResult,UrpayConfigDomain config){
        //店铺Id
        String dpId = config.getDpId();

        //去重的订单
        List<OrderDomain> repeatList = filterResult.getRepeatList();

        //第二天需要发送的订单
        List<OrderDomain> sendNextDayList = filterResult.getSendNextDayList();


        //保存第二天需要发送的订单 2: 次日催付
        List<UrpayStatusDomain> list = convertOrder2UrpayStatusDomain(sendNextDayList,2);
        updateUrpayStatus(list);

        //次日不需要发送的订单，永远不会被发送了
        List<OrderDomain> notSendNextDayList = filterResult.getNotSendList();
        list = convertOrder2UrpayStatusDomain(notSendNextDayList,3);
        updateUrpayStatus(list);

        //去重的订单，但表示为发送
        list = convertOrder2UrpayStatusDomain(repeatList,1);
        updateUrpayStatus(list);

        //待发送列表
        List<OrderDomain> smsList = filterResult.getSmsList();
        if(smsList == null || smsList.isEmpty()){
            return;
        }
        logger.info("店铺:[" + dpId + "],过滤之后订单数:" + smsList.size() + ",开始保存待发送队列...");
        //保存待发送的短信,并更新催付状态
        saveNeedSendOrders(smsList,config);
        logger.info("店铺:[" + dpId + "],保存待发送队列完成...");
    }

    /*
     *店铺是否有效
     *
     */
    private boolean isValidShop(String dpId) {
        return shopService.isValidShop(dpId);
    }

    /**
     * etl数据是否延迟，默认是不延迟
     *
     * @return
     */
    private boolean isEtlTimeout(String dpId,int offSet,String sessionKey){
        return etlService.isEtlTimeOut(dpId,offSet,sessionKey);
    }

    /**
     *
     * 将订单转化为待发送的短信列表
     * @param order
     * @param config
     * @return
     */
    private SmsQueueDomain convertOrder2Sms(OrderDomain order,UrpayConfigDomain config,String smsStr) {
        SmsQueueDomain sms = new SmsQueueDomain();
        sms.setBuyer_nick(order.getCustomerno());
        sms.setCreated(new Date());
        sms.setDpId(order.getDpId());
        sms.setMobile(order.getReceiverMobile());
        //自动发送设置为系统管理员
        sms.setSend_user("system");
        sms.setSms_content(smsStr);
        sms.setTid(order.getTid());
        sms.setTrade_created(order.getCreated());
        sms.setType(getJobType().getTypeValue());
        sms.setUpdated(new Date());
        sms.setGatewayId(config.getGatewayId().longValue());
        return sms;
    }

    /**
     *
     * 根据任务类型获取对应的催付配置
     * @return
     */
    public abstract List<UrpayConfigDomain> getOpenedUrapyConfig();

    /**
     * 获取所有未付款的订单
     *
     * @param config
     * @return
     */
    public abstract List<OrderDomain> queryOrders(UrpayConfigDomain config);


    /** 设置任务类型 **/
    public abstract UrpayTypeEnum getJobType();

    /** 获取催付时间过滤器 **/
    public abstract IFilter getUrpayTimeFilter();

    /**
     * 保存待发送的短信队列
     *
     * @param orderList
     */
    private void saveNeedSendOrders(List<OrderDomain> orderList,UrpayConfigDomain config){
        if(orderList == null || orderList.isEmpty()){
            return;
        }
        List<SmsQueueDomain> smsList = convertOrder2SmsQueue(orderList,config);
        List<UrpayStatusDomain> list = convertOrder2UrpayStatusDomain(orderList,1);
        //保存到队列表，并更新催付状态为：已催付
        smsQueueService.saveSmsQueueByBatch(smsList,list,String.valueOf(getJobType().getTypeValue()));
    }

    /**
     * 将订单对象转化为消息对象
     *
     * @param orderList
     * @param config
     * @return
     */
    private List<SmsQueueDomain> convertOrder2SmsQueue(List<OrderDomain> orderList,UrpayConfigDomain config){
        List<SmsQueueDomain> smsList = new ArrayList<SmsQueueDomain>();
        for(OrderDomain order : orderList){
            List<Object> list = new ArrayList<Object>();
            list.add(order);
            String result = variableReplaceService.replaceSmsContent(config.getSmsContent(),list);
            if(StringUtils.equalsIgnoreCase(result, "000")){
                logger.info("订单替换变量失败，订单:[" + order.getTid() + "],被过滤");
                continue;
            }else{
                SmsQueueDomain sms = convertOrder2Sms(order,config,result);
                smsList.add(sms);
            }
        }
        return smsList;
    }

    /**
     * 更新催付状态
     *
     * @param sendLNextDayist
     */
    private void updateUrpayStatus(List<UrpayStatusDomain> list) {
        if(list == null || list.isEmpty()){
            return;
        }
        urpayStatusService.insertUrpayStatusBatch(list, String.valueOf(getJobType().getTypeValue()));
    }

    /**
     * 将订单转化为催付状态对象
     *
     * @param orderList
     * @param urpayStatus
     * @return
     */
    private List<UrpayStatusDomain> convertOrder2UrpayStatusDomain(List<OrderDomain> orderList,int urpayStatus){
        List<UrpayStatusDomain> list = new ArrayList<UrpayStatusDomain>();
        for(OrderDomain order : orderList){
            UrpayStatusDomain d = new UrpayStatusDomain();
            d.setTid(order.getTid());
            if(getJobType() == UrpayTypeEnum.AUTO_URPAY){
                d.setAutoUrpayStatus(urpayStatus);
                d.setAutoUrpayThread(thread);
            }
            if(getJobType() == UrpayTypeEnum.CHEAP_URPAY){
                d.setCheapUrpayStatus(urpayStatus);
                d.setCheapUrpayThread(thread);
            }
            if(getJobType() == UrpayTypeEnum.PRE_CLOSE_URPAY){
                d.setCloseUrpayStatus(urpayStatus);
                d.setCloseUrpayThread(thread);
            }
            list.add(d);
        }
        return list;
    }
}
