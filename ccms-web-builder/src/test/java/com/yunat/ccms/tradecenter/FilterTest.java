package com.yunat.ccms.tradecenter;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;
import com.yunat.ccms.tradecenter.urpay.filter.BlacklistFilter;
import com.yunat.ccms.tradecenter.urpay.filter.CheapFilter;
import com.yunat.ccms.tradecenter.urpay.filter.FilterResult;
import com.yunat.ccms.tradecenter.urpay.filter.MemberGradeFilter;
import com.yunat.ccms.tradecenter.urpay.filter.MobileFilter;
import com.yunat.ccms.tradecenter.urpay.filter.OrderAmountFilter;
import com.yunat.ccms.tradecenter.urpay.filter.OrderDateFilter;
import com.yunat.ccms.tradecenter.urpay.filter.PayedOrderFilter;
import com.yunat.ccms.tradecenter.urpay.filter.PreCloseUrpayTimeFilter;
import com.yunat.ccms.tradecenter.urpay.filter.UrpayTimeFilter;
import com.yunat.ccms.tradecenter.urpay.filter.WhilePointUrpayTimeFilter;

public class FilterTest extends AbstractJunit4SpringContextBaseTests{

    private UrpayConfigDomain config = new UrpayConfigDomain();

    private OrderDomain order = new OrderDomain();

    @Autowired
    BlacklistFilter blacklistFilter;

    @Autowired
    CheapFilter cheapFilter;

    @Autowired
    MemberGradeFilter memberGradeFilter;

    @Autowired
    MobileFilter mobileFilter;

    @Autowired
    OrderAmountFilter orderAmountFilter;

    @Autowired
    OrderDateFilter orderDateFilter;

    @Autowired
    PayedOrderFilter payedOrderFilter;

    @Autowired
    PreCloseUrpayTimeFilter preCloseUrpayTimeFilter;

    @Autowired
    UrpayTimeFilter urpayTimeFilter;

    @Autowired
    WhilePointUrpayTimeFilter whilePointUrpayTimeFilter;

    @Before
    public void init(){
        //--------------------订单数据
        order.setTid("1");
        order.setDpId("123");
        order.setCustomerno("test");
        order.setCreated(DateUtils.getDateTime("2013-06-04 12:20:00"));
        order.setTradeFrom("JHS");
        order.setPayment(199.00);
        order.setReceiverMobile("13482882177");
        //-------------------- 配置数据
        config.setCreated(new Date());
        //1 自定义类型，0非自定义类型)
        config.setDateType(1);
        config.setDpId("123");
        config.setEndDate(DateUtils.getDate("2013-05-01"));
        //(1:是，0：否)
        config.setExcludeGoods(1);
        /**
         * 过滤选项：
         *1、多笔订单只发送一次
         *2、多个手机只发送一次
         *3、排除有支付过的客户
         *4、屏蔽短信黑名单用户
         *
         */
        config.setFilterCondition("1,2,3,4");

        config.setFixUrpayTime("9,10,19");
        config.setGatewayId(1);
        config.setGoods("");
        //(1:是，0：否)
        config.setIncludeCheap(1);

        /**
         * 会员等级：
         * -1:不限
         * 0:新客户
         * 1:普通会员
         * 2:高级会员
         * 3:VIP
         * 4:至尊VIP
         */
        config.setMemberGrade("-1");

        /**
         * 催付选项:
         * 0:不发送
         * 1:次日催付
         *
         */
        config.setNotifyOption(1);

        config.setOffset(30);

        config.setOrderMinAcount(1.00);
        config.setOrderMaxAcount(100.00);

        /**
         * 催付类型:
         *  1:自动催付
         *  2：预关闭催付
         *  3：聚划算催付
         *
         */
        config.setUrpayType(1);

        /**
         * 任务类型：
         * 1：实时催付
         * 2：定时催付
         */
        config.setTaskType(1);
    }

    @Test
    public void testBlackList(){
        blacklistFilter.doFiler(order, config);
    }

    @Test
    public void testCheap(){
        order.setTradeFrom("TT");
        cheapFilter.doFiler(order, config);
    }

    @Test
    public void testMemberGrade(){
        config.setMemberGrade("1");
        memberGradeFilter.doFiler(order, config);
    }

    @Test
    public void testMobileFilter(){
        order.setReceiverMobile("13482882177");
        FilterResult retn = mobileFilter.doFiler(order, config);
        System.out.println(retn.isFilter());
    }

    @Test
    public void testOrderAmountFilter(){
        order.setPayment(101.00);
        config.setOrderMinAcount(2.00);
        config.setOrderMaxAcount(100.00);
        FilterResult retn = orderAmountFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setPayment(66.00);
        retn = orderAmountFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setPayment(1.00);
        retn = orderAmountFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setPayment(2.00);
        retn = orderAmountFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setPayment(100.00);
        retn = orderAmountFilter.doFiler(order, config);
        System.out.println(retn.isFilter());
    }

    @Test
    public void testOrderDateFilter(){
        order.setCreated(DateUtils.getDateTime("2013-06-04 12:20:00"));

        config.setStartDate(DateUtils.getDate("2013-05-01"));
        config.setEndDate(DateUtils.getDate("2013-06-06"));

        FilterResult retn = orderDateFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setCreated(DateUtils.getDateTime("2013-04-04 12:20:00"));
        retn = orderDateFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setCreated(DateUtils.getDateTime("2013-06-07 00:00:01"));
        retn = orderDateFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setCreated(DateUtils.getDateTime("2013-05-01 12:20:00"));
        retn = orderDateFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setCreated(DateUtils.getDateTime("2013-06-06 23:59:59"));
        retn = orderDateFilter.doFiler(order, config);
        System.out.println(retn.isFilter());
    }

    @Test
    public void testPayedOrderFilter(){
        order.setDpId("123");
        order.setCustomerno("test");
        FilterResult retn = payedOrderFilter.doFiler(order, config);
        System.out.println(retn.isFilter());
    }

    @Test
    public void testPreCloseUrpayTimeFilter(){
        config.setUrpayStartTime(DateUtils.getDateTime("1970-01-01 09:00:00"));
        config.setUrpayEndTime(DateUtils.getDateTime("1970-01-01 22:00:00"));
        order.setCreated(DateUtils.getDateTime("2013-06-02 12:20:00"));
        config.setOffset(30);
        FilterResult retn = preCloseUrpayTimeFilter.doFiler(order, config);
        System.out.println(retn.isFilter());

        order.setCreated(DateUtils.getDateTime("2013-06-02 20:30:00"));
        retn = preCloseUrpayTimeFilter.doFiler(order, config);
        System.out.println(retn.isFilter());
    }

    @Test
    public void testUrpayTimeFilter(){
        config.setUrpayStartTime(DateUtils.getDateTime("1970-01-01 09:00:00"));
        config.setUrpayEndTime(DateUtils.getDateTime("1970-01-01 22:00:00"));

        //间隔分钟
        config.setOffset(30);
        order.setCreated(DateUtils.getDateTime("2013-06-04 12:20:00"));
        FilterResult retn = urpayTimeFilter.doFiler(order, config);
        System.out.println("=======处在催付时间范围内======需要催付=====false:" + retn.isFilter());

        config.setUrpayStartTime(DateUtils.getDateTime("1970-01-01 21:00:00"));
        retn = urpayTimeFilter.doFiler(order, config);
        System.out.println("========不处在催付时间范围内,且小于催付开始时间=====不需要催付=====true:" + retn.isFilter());

        config.setUrpayStartTime(DateUtils.getDateTime("1970-01-01 09:00:00"));
        config.setUrpayEndTime(DateUtils.getDateTime("1970-01-01 19:00:00"));
        config.setNotifyOption(0);
        retn = urpayTimeFilter.doFiler(order, config);

        System.out.println("========不处在催付时间范围内,且大于催付大于时间,不次日催付=====不需要催付=====true:" + retn.isFilter());


        config.setUrpayStartTime(DateUtils.getDateTime("1970-01-01 09:00:00"));
        config.setUrpayEndTime(DateUtils.getDateTime("1970-01-01 19:00:00"));
        config.setNotifyOption(1);
        retn = urpayTimeFilter.doFiler(order, config);
        //System.out.println("========不处在催付时间范围内,且大于催付大于时间==,次日催付===不需要催付=====true:" + retn.isFilter() + ",isNextDaySend = true:" + retn.isSendNextDay());
    }

    @Test
    public void testWhilePointUrpayTimeFilter(){
        order.setCreated(DateUtils.getDateTime("2013-06-06 09:20:00"));
        config.setFixUrpayTime("9,10,19");

        //间隔分钟
        config.setOffset(30);
        FilterResult retn = whilePointUrpayTimeFilter.doFiler(order, config);
        System.out.println("=======处在催付时间点======超过用户设定时间，需要催付=====不过滤，false:" + retn.isFilter());

        order.setCreated(DateUtils.getDateTime("2013-06-06 09:40:00"));
        retn = whilePointUrpayTimeFilter.doFiler(order, config);
        System.out.println("=======处在催付时间点======未超过用户设定时间，需要催付=====过滤，true:" + retn.isFilter());

        order.setCreated(DateUtils.getDateTime("2013-06-06 09:20:00"));
        config.setFixUrpayTime("9,19");
        config.setNotifyOption(0);
        retn = whilePointUrpayTimeFilter.doFiler(order, config);
        //System.out.println("=======不处在催付时间点,未超过用户设定的最后时点======不需要催付=====过滤，true:" + retn.isFilter() + ",isNextDaySend : false : " + retn.isSendNextDay());

        order.setCreated(DateUtils.getDateTime("2013-06-06 09:20:00"));
        config.setFixUrpayTime("8");
        config.setNotifyOption(0);
        retn = whilePointUrpayTimeFilter.doFiler(order, config);
        //System.out.println("=======不处在催付时间点，超过用户设定的最后时点======不需要催付=====过滤，true:" + retn.isFilter() + ",isNextDaySend : false : " + retn.isSendNextDay());

        order.setCreated(DateUtils.getDateTime("2013-06-06 09:20:00"));
        config.setFixUrpayTime("8");
        config.setNotifyOption(1);
        retn = whilePointUrpayTimeFilter.doFiler(order, config);
        //System.out.println("=======不处在催付时间点，超过用户设定的最后时点======不需要催付=====过滤，true:" + retn.isFilter() + ",isNextDaySend : true : " + retn.isSendNextDay());
    }
}
