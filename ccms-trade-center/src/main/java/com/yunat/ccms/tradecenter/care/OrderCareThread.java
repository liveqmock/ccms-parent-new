package com.yunat.ccms.tradecenter.care;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 * 下单关怀线程
 *
 * @author shaohui.li
 * @version $Id: OrderCareThread.java, v 0.1 2013-7-3 上午10:49:49 shaohui.li Exp $
 */
public class OrderCareThread implements Runnable{

    /** 订单Map key:店铺id value:订单列表 **/
    private Map<String,List<OrderDomain>> ordersMap;

    private OrderAndPayCare orderAndPayCare;

    public OrderCareThread(Map<String, List<OrderDomain>> ordersMap,
                            OrderAndPayCare orderAndPayCare) {
        super();
        this.ordersMap = ordersMap;
        this.orderAndPayCare = orderAndPayCare;
    }

    @Override
    public void run() {
        orderAndPayCare.setOrdersMap(ordersMap);
        orderAndPayCare.doCare();
    }
}
