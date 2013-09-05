package com.yunat.ccms.tradecenter.controller.vo;

/**
 * 今日订单变化数据
 *
 * @author shaohui.li
 * @version $Id: TodayOrderChangeDataVO.java, v 0.1 2013-7-30 上午10:58:58 shaohui.li Exp $
 */
public class TodayOrderChangeDataVO {

    /** 今日拍下订单数 **/
    private Long createdTodayOrderNum;

    /** 今日付款订单数 **/
    private Long payedTodayOrderNum;

    /** 今日发货订单数 **/
    private Long consignedTodayOrderNum;

    /** 今日同城订单数 **/
    private Long arrivedTodayOrderNum;

    /** 今日派件订单数 **/
    private Long deliveryTodayOrderNum;

    /** 今日签收订单数**/
    private Long signedTodayOrderNum;

    /** 今日确认收货的订单数 **/
    private Long confirmTodayOrderNum;

    /** 今日退款的子订单数 **/
    private Long refundTodayOrderNum;

    /** 今日评价子订单数 **/
    private Long traderateTodayOrderNum;

    public Long getCreatedTodayOrderNum() {
        return createdTodayOrderNum;
    }

    public void setCreatedTodayOrderNum(Long createdTodayOrderNum) {
        this.createdTodayOrderNum = createdTodayOrderNum;
    }

    public Long getPayedTodayOrderNum() {
        return payedTodayOrderNum;
    }

    public void setPayedTodayOrderNum(Long payedTodayOrderNum) {
        this.payedTodayOrderNum = payedTodayOrderNum;
    }

    public Long getConsignedTodayOrderNum() {
        return consignedTodayOrderNum;
    }

    public void setConsignedTodayOrderNum(Long consignedTodayOrderNum) {
        this.consignedTodayOrderNum = consignedTodayOrderNum;
    }

    public Long getArrivedTodayOrderNum() {
        return arrivedTodayOrderNum;
    }

    public void setArrivedTodayOrderNum(Long arrivedTodayOrderNum) {
        this.arrivedTodayOrderNum = arrivedTodayOrderNum;
    }

    public Long getDeliveryTodayOrderNum() {
        return deliveryTodayOrderNum;
    }

    public void setDeliveryTodayOrderNum(Long deliveryTodayOrderNum) {
        this.deliveryTodayOrderNum = deliveryTodayOrderNum;
    }

    public Long getSignedTodayOrderNum() {
        return signedTodayOrderNum;
    }

    public void setSignedTodayOrderNum(Long signedTodayOrderNum) {
        this.signedTodayOrderNum = signedTodayOrderNum;
    }

    public Long getConfirmTodayOrderNum() {
        return confirmTodayOrderNum;
    }

    public void setConfirmTodayOrderNum(Long confirmTodayOrderNum) {
        this.confirmTodayOrderNum = confirmTodayOrderNum;
    }

    public Long getRefundTodayOrderNum() {
        return refundTodayOrderNum;
    }

    public void setRefundTodayOrderNum(Long refundTodayOrderNum) {
        this.refundTodayOrderNum = refundTodayOrderNum;
    }

    public Long getTraderateTodayOrderNum() {
        return traderateTodayOrderNum;
    }

    public void setTraderateTodayOrderNum(Long traderateTodayOrderNum) {
        this.traderateTodayOrderNum = traderateTodayOrderNum;
    }

}
