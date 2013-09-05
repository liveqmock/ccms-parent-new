package com.yunat.ccms.tradecenter.controller.vo;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.SendLogDomain;

/**
 * 发货事务查询结果对象
 *
 * @author shaohui.li
 * @version $Id: SendGoodsResultVO.java, v 0.1 2013-7-9 下午02:03:54 shaohui.li Exp $
 */
public class SendGoodsResultVO {

    /** 订单id **/
    private String tid;

    /** 买家昵称 **/
    private String buyeyNick;

    /** 会员等级 **/
    private String grade;

    /** 付款时间 **/
    private String payTime;

    /** 收货人姓名 **/
    private String receiverName;

    /** 收货人手机 **/
    private String receiverPhone;

    /** 省 **/
    private String receiverState;

    /** 市 **/
    private String receiverCity;

    /** 街道 **/
    private String receiverDistrict;

    /** 地址 **/
    private String receiverAddress;

    /** 邮编 **/
    private String receiverZip;

    /** 交易来源 **/
    private String tradeFrom;

    /** 子订单 **/
    private List<OrderItemVO> goodsItems;

    /** 实付金额 **/
    private Double payment;

    /** 邮费 **/
    private Double postFee;

    private String orderStatus = "等待卖家发货";

    /** 关怀历史 **/
    private List<SendLogDomain> careLog;

    /** 发货状态 **/
    private String sendStatus;

    /** 延迟时间 **/
    private String delayTime;

    /** 是否隐藏 **/
    private Boolean isHide;

    /** 是否跟进 **/
    private Boolean isFollowUp;

    /** 跟进状态 **/
    private Integer followStatus;

    /** 事务id **/
    private Integer followId;

    /** 买家留言对象  **/
    private TradeMemoVO memoVo;

    public TradeMemoVO getMemoVo() {
        return memoVo;
    }

    public Integer getFollowId() {
        return followId;
    }

    public void setFollowId(Integer followId) {
        this.followId = followId;
    }

    public void setMemoVo(TradeMemoVO memoVo) {
        this.memoVo = memoVo;
    }

    public Boolean getIsFollowUp() {
        return isFollowUp;
    }

    public void setIsFollowUp(Boolean isFollowUp) {
        this.isFollowUp = isFollowUp;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getBuyeyNick() {
        return buyeyNick;
    }

    public void setBuyeyNick(String buyeyNick) {
        this.buyeyNick = buyeyNick;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getTradeFrom() {
        return tradeFrom;
    }

    public void setTradeFrom(String tradeFrom) {
        this.tradeFrom = tradeFrom;
    }

    public List<OrderItemVO> getGoodsItems() {
        return goodsItems;
    }

    public void setGoodsItems(List<OrderItemVO> goodsItems) {
        this.goodsItems = goodsItems;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getPostFee() {
        return postFee;
    }

    public void setPostFee(Double postFee) {
        this.postFee = postFee;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<SendLogDomain> getCareLog() {
        return careLog;
    }

    public void setCareLog(List<SendLogDomain> careLog) {
        this.careLog = careLog;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public Boolean getIsHide() {
        return isHide;
    }

    public void setIsHide(Boolean isHide) {
        this.isHide = isHide;
    }
}
