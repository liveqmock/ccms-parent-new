package com.yunat.ccms.tradecenter.urpay.filter;

import java.util.ArrayList;
import java.util.List;

import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 * 订单过滤结果
 *
 * @author shaohui.li
 * @version $Id: OrderFilterResult.java, v 0.1 2013-6-7 下午06:48:30 shaohui.li Exp $
 */
public class OrderFilterResult {

    //待发送列表
    private List<OrderDomain> smsList = new ArrayList<OrderDomain>();

    //去重的订单
    private List<OrderDomain> repeatList = new ArrayList<OrderDomain>();

    //第二天需要发送的订单
    private List<OrderDomain> sendNextDayList = new ArrayList<OrderDomain>();

    //永远也不会发送的订单
    private List<OrderDomain> notSendList = new ArrayList<OrderDomain>();

    //被过滤掉的订单
    private List<OrderDomain> filteredList = new ArrayList<OrderDomain>();

    public List<OrderDomain> getSmsList() {
        return smsList;
    }

    public void setSmsList(List<OrderDomain> smsList) {
        this.smsList = smsList;
    }

    public List<OrderDomain> getRepeatList() {
        return repeatList;
    }

    public void setRepeatList(List<OrderDomain> repeatList) {
        this.repeatList = repeatList;
    }

    public List<OrderDomain> getNotSendList() {
        return notSendList;
    }

    public void setNotSendList(List<OrderDomain> notSendList) {
        this.notSendList = notSendList;
    }

    public List<OrderDomain> getSendNextDayList() {
        return sendNextDayList;
    }

    public void setSendNextDayList(List<OrderDomain> sendNextDayList) {
        this.sendNextDayList = sendNextDayList;
    }

    public List<OrderDomain> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<OrderDomain> filteredList) {
        this.filteredList = filteredList;
    }

}
