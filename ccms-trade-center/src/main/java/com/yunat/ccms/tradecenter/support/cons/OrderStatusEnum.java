package com.yunat.ccms.tradecenter.support.cons;

/**
 *
 *订单状态枚举
 * @author shaohui.li
 * @version $Id: OrderStatusEnum.java, v 0.1 2012-9-26 下午02:33:33 shaohui.li Exp $
 */
public enum OrderStatusEnum {
    /**
                可选值: * TRADE_NO_CREATE_PAY(没有创建支付宝交易) *
                WAIT_BUYER_PAY(等待买家付款) *
                WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款) *
                WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) *
                TRADE_BUYER_SIGNED(买家已签收,货到付款专用) *
                TRADE_FINISHED(交易成功) *
                TRADE_CLOSED(付款以后用户退款成功，交易自动关闭) *
                TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)
    **/
    TRADE_NO_CREATE_PAY("TRADE_NO_CREATE_PAY","没有创建支付宝交易",0),

    WAIT_BUYER_PAY("WAIT_BUYER_PAY","等待买家付款",1),

    WAIT_SELLER_SEND_GOODS("WAIT_SELLER_SEND_GOODS","等待卖家发货,即:买家已付款",2),

    WAIT_BUYER_CONFIRM_GOODS("WAIT_BUYER_CONFIRM_GOODS","等待买家确认收货,即:卖家已发货",3),

    TRADE_BUYER_SIGNED("TRADE_BUYER_SIGNED","买家已签收,货到付款专用",4),

    TRADE_FINISHED("TRADE_FINISHED","交易成功",5),

    TRADE_CLOSED("TRADE_CLOSED","付款以后用户退款成功，交易自动关闭",6),

    TRADE_CLOSED_BY_TAOBAO("TRADE_CLOSED_BY_TAOBAO","付款以前，卖家或买家主动关闭交易",7),

    SELLER_CONSIGNED_PART("SELLER_CONSIGNED_PART", "卖家部分发货",8)
    ;

    /** 状态代码**/
    private String orderStatusCode;

    /** 状态描述**/
    private String orderStatusDesc;

    /** 状态值**/
    private int statusValue;

    private OrderStatusEnum(String orderStatusCode,String orderStatusDesc,int statusValue) {
        this.orderStatusCode = orderStatusCode;
        this.orderStatusDesc = orderStatusDesc;
        this.statusValue = statusValue;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public int getStatusValue() {
        return statusValue;
    }

}
