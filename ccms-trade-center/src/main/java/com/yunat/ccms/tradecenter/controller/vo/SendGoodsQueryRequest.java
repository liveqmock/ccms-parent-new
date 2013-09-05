package com.yunat.ccms.tradecenter.controller.vo;

import com.yunat.ccms.tradecenter.service.queryobject.BaseQuery;

/**
 *
 * 发货查询请求
 *
 * @author shaohui.li
 * @version $Id: SendGoodsQueryRequest.java, v 0.1 2013-7-9 下午01:31:19 shaohui.li Exp $
 */
public class SendGoodsQueryRequest extends BaseQuery{

    /**  */
    private static final long serialVersionUID = -8717270925017064068L;

    /**  店铺id **/
    private String dpId;

    /**  发货等待天数 **/
    private Integer waitDay;

    /**  关怀状态 **/
    private Integer careStatus;

    /**  订单排序 **/
    private String orderSort;

    /**  买家昵称 **/
    private String customerno;

    /**  收货省 **/
    private String receiverState;

    /**  付款开始时间 **/
    private String payedStartTime;

    /**  付款结束时间 **/
    private String payedEndTime;

    /**  是否隐藏 **/
    private Integer isHide;

    /**  宝贝名称 **/
    private String title;

    /**  订单id **/
    private String tid;

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public Integer getWaitDay() {
        return waitDay;
    }

    public void setWaitDay(Integer waitDay) {
        this.waitDay = waitDay;
    }

    public Integer getCareStatus() {
        return careStatus;
    }

    public void setCareStatus(Integer careStatus) {
        this.careStatus = careStatus;
    }

    public String getOrderSort() {
        return orderSort;
    }

    public void setOrderSort(String orderSort) {
        this.orderSort = orderSort;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getPayedStartTime() {
        return payedStartTime;
    }

    public void setPayedStartTime(String payedStartTime) {
        this.payedStartTime = payedStartTime;
    }

    public String getPayedEndTime() {
        return payedEndTime;
    }

    public void setPayedEndTime(String payedEndTime) {
        this.payedEndTime = payedEndTime;
    }

    public Integer getIsHide() {
        return isHide;
    }

    public void setIsHide(Integer isHide) {
        this.isHide = isHide;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
