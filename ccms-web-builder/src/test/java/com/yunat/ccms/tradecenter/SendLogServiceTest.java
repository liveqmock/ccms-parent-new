package com.yunat.ccms.tradecenter;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.domain.SendLogDomain;
import com.yunat.ccms.tradecenter.repository.UrpayStatusRepository;
import com.yunat.ccms.tradecenter.service.SendLogService;


public class SendLogServiceTest extends AbstractJunit4SpringContextBaseTests {

    @Autowired
    private SendLogService sendLogService;

    @Test
    public void query(){
        List<SendLogDomain> list = sendLogService.getSmsLogByBuyer("123", new Date(), "test",1);
        if(list != null){
            System.out.println(list.size());
        }
        list = sendLogService.getSmsLogByMobile("123", new Date(), "134",1);
        if(list != null){
            System.out.println(list.size());
        }
    }

    @Autowired
    public UrpayStatusRepository urpayStatusRepository;

    @Test
    public void insertBatchUrpayStatus(){
        UrpayStatusThread t1 = new UrpayStatusThread(urpayStatusRepository);
        UrpayStatusThread t2 = new UrpayStatusThread(urpayStatusRepository);
        t1.start();
        t2.start();
        try {
            Thread.sleep(60 * 10000 * 1000);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        //t2.start();
    }

    @Test
    public void testTime(){
        Date startDate = DateUtils.getDateTime("2013-06-04 12:20:00");
        Date orderDate = DateUtils.getDateTime("2013-06-04 12:20:00");
        System.out.println("---------------------" + orderDate.before(startDate));

        orderDate = DateUtils.getDateTime("2013-06-04 12:19:59");
        System.out.println("---------------------" + orderDate.before(startDate));

        Date endDate = DateUtils.getDateTime("2013-06-07 12:20:00");
        orderDate = DateUtils.getDateTime("2013-06-07 12:20:00");
        System.out.println("---------------------" + orderDate.after(endDate));

        orderDate = DateUtils.getDateTime("2013-06-07 12:20:01");
        System.out.println("---------------------" + orderDate.after(endDate));
    }

}

