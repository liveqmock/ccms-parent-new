package com.yunat.ccms.tradecenter.support.cons;

public enum OrderFlowMonitoringStatusEnum {
	
	CREATE_ORDER("create_order", "下单", 1, "拍下时间在30天内的主订单数"),
	PAY_ORDER("pay_order", "付款", 2, "拍下时间在30天内，并且付款（订单状态）成功的主订单数"),
	SEND_GOODS_ORDER("send_goods_order", "发货", 3, "拍下时间在30天内，并且发货（订单状态）成功的主订单数"),
	SIGNED_ORDER("signed_order", "签收", 4, "拍下时间在30天内，并且签收（物流判断）成功的主订单数"),
	FINISHED_ORDER("finished_order", "确认", 5, "拍下时间在30天内，并且确认收货（订单状态）成功的主订单数"),
	TRADERATE_ORDER("traderate_order", "评价", 6, "拍下时间在30天内，并且评价（评价状态）成功的订单数（主订单）一张主订单内，只要有一个子订单有评价，也算评价成功");
	
	private String statusCod;
	private String statusName;
	private Integer orderId;
	private String statusDesc;
	
	private OrderFlowMonitoringStatusEnum(String statusCod, String statusName, Integer orderId, String statusDesc) {
		this.statusCod = statusCod;
		this.statusName = statusName;
		this.orderId = orderId;
		this.statusDesc = statusDesc;
	}
	
	public String getStatusCod() {
		return statusCod;
	}
	public void setStatusCod(String statusCod) {
		this.statusCod = statusCod;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	
	
	
	

}
