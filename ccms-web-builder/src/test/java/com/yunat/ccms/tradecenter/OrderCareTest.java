package com.yunat.ccms.tradecenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.tradecenter.care.OrderAndPayCare;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.repository.BatchRepository;

public class OrderCareTest extends AbstractJunit4SpringContextBaseTests{

    /** 订单Map key:店铺id value:订单列表 **/
    private Map<String,List<OrderDomain>> ordersMap = new HashMap<String,List<OrderDomain>>();

    @Autowired
    private OrderAndPayCare orderAndPayCare;

    @Autowired
    BatchRepository batchRepository;

    @Test
    public void doOrderCare(){
        List<OrderDomain> list = new ArrayList<OrderDomain>();
        for(int i=0;i<10;i++){
            OrderDomain d = new OrderDomain();
            d.setBuyerMessage("");
            d.setConsignTime(new Date());
            d.setCreated(DateUtils.getDateTime("2013-06-18 10:23:33"));
            d.setCustomerno("c" + i);
            d.setDpId("65927472");
            d.setEndtime(null);
            d.setModified(DateUtils.getDateTime("2013-06-18 10:23:33"));
            d.setNum(1);
            d.setOrderStatus(1);
            d.setPayment(199.00);
            d.setPayTime(null);
            d.setPostFee(10.00);
            d.setReceiverAddress("ReceiverAddress");
            d.setReceiverCity("ReceiverCity");
            d.setReceiverDistrict("ReceiverDistrict");
            d.setReceiverMobile("13482882177");//手机号码
            d.setReceiverName("test");
            d.setReceiverPhone("ReceiverPhone");
            d.setReceiverState("ReceiverState");
            d.setReceiverZip("12243");
            d.setSellerFlag(1);
            d.setSellerMemo("");
            d.setShippingType("");
            d.setStatus("WAIT_BUYER_PAY");
            d.setOrderStatus(1);
            d.setTid(String.valueOf(i));
            d.setTradeFrom("TAOBAO");
            d.setOrderCreated(new Date());
            d.setOrderUpdated(new Date());
            list.add(d);
        }
        //保存
        batchRepository.batchInsert(list);
        ordersMap.put("65927472", list);
        orderAndPayCare.setOrdersMap(ordersMap);
        orderAndPayCare.doCare();
    }
}
