package com.yunat.ccms.tradecenter.controller.vo;

/**
 * 当日实时成交订单数据
 *
 * @author shaohui.li
 * @version $Id: TodayRealTimeOrderDataVO.java, v 0.1 2013-7-23 下午03:44:04 shaohui.li Exp $
 */
public class TodayRealTimeOrderDataVO {

    /** 当日成交订单数 **/
    private Long payTimeIsTodayOrderNum;

    /** 当日成交订单金额 **/
    private Double payTimeIsTodayOrderAmount;

    /** 当日成交均价 **/
    private Double orderAveragePrice;

    /** 关联销售订单占比 **/
    private Integer salePercent;

    /** 平均成交件数 **/
    private Double averageGoodsNum;

    /** 今日拍下订单数 **/
    private Long createdIsTodayOrderNum;

    /** 付款率 **/
    private Integer payedPercent;

    /** 今日拍下未付款订单数 **/
    private Long notPayedOrderNum;

    /** 催付比例 **/
    private Integer urpayPercent;

    public Long getPayTimeIsTodayOrderNum() {
        return payTimeIsTodayOrderNum;
    }

    public void setPayTimeIsTodayOrderNum(Long payTimeIsTodayOrderNum) {
        this.payTimeIsTodayOrderNum = payTimeIsTodayOrderNum;
    }

    public Double getPayTimeIsTodayOrderAmount() {
        return payTimeIsTodayOrderAmount;
    }

    public void setPayTimeIsTodayOrderAmount(Double payTimeIsTodayOrderAmount) {
        this.payTimeIsTodayOrderAmount = payTimeIsTodayOrderAmount;
    }

    public Double getOrderAveragePrice() {
        return orderAveragePrice;
    }

    public void setOrderAveragePrice(Double orderAveragePrice) {
        this.orderAveragePrice = orderAveragePrice;
    }

    public Integer getSalePercent() {
        return salePercent;
    }

    public void setSalePercent(Integer salePercent) {
        this.salePercent = salePercent;
    }

    public Double getAverageGoodsNum() {
        return averageGoodsNum;
    }

    public void setAverageGoodsNum(Double averageGoodsNum) {
        this.averageGoodsNum = averageGoodsNum;
    }

    public Long getCreatedIsTodayOrderNum() {
        return createdIsTodayOrderNum;
    }

    public void setCreatedIsTodayOrderNum(Long createdIsTodayOrderNum) {
        this.createdIsTodayOrderNum = createdIsTodayOrderNum;
    }

    public Long getNotPayedOrderNum() {
        return notPayedOrderNum;
    }

    public void setNotPayedOrderNum(Long notPayedOrderNum) {
        this.notPayedOrderNum = notPayedOrderNum;
    }

    public Integer getPayedPercent() {
        return payedPercent;
    }

    public void setPayedPercent(Integer payedPercent) {
        this.payedPercent = payedPercent;
    }

    public Integer getUrpayPercent() {
        return urpayPercent;
    }

    public void setUrpayPercent(Integer urpayPercent) {
        this.urpayPercent = urpayPercent;
    }
}
