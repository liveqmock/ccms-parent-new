package com.yunat.ccms.tradecenter.controller.vo;

/**
 * 当前24小时订单数据
 *
 * @author shaohui.li
 * @version $Id: Today24HourOrderDataVO.java, v 0.1 2013-7-23 下午04:06:33 shaohui.li Exp $
 */
public class Today24HourOrderDataVO {

    /** 当前拍下订单数 **/
    private Long orderNum;

    /** 下单时点 **/
    private int orderHour;

    /** 当前拍下订单金额 **/
    private Double orderAmount;

    /** 当前付款订单金额 **/
    private Double payedOrderAmount;


    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getPayedOrderAmount() {
        return payedOrderAmount;
    }

    public void setPayedOrderAmount(Double payedOrderAmount) {
        this.payedOrderAmount = payedOrderAmount;
    }

    public int getOrderHour() {
        return orderHour;
    }

    public void setOrderHour(int orderHour) {
        this.orderHour = orderHour;
    }
}
