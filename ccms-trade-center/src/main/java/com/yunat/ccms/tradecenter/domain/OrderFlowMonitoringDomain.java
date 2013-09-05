package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_tc_order_flow_monitoring")
public class OrderFlowMonitoringDomain extends BaseDomain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5996123685734177715L;
	
	/**
	 * 定义复合主键
	 */
	private OrderFlowMonitoringDomainPK orderFlowMonitoringPK;
	private Integer orderCount;
	private String flowRate;
	private String flowIntervalTime;
	private Integer orderId;
	private String statusName;
	private String statusDesc;
	
	
	@EmbeddedId
	public OrderFlowMonitoringDomainPK getOrderFlowMonitoringPK() {
		return orderFlowMonitoringPK;
	}
	public void setOrderFlowMonitoringPK(OrderFlowMonitoringDomainPK orderFlowMonitoringPK) {
		this.orderFlowMonitoringPK = orderFlowMonitoringPK;
	}
	
	@Column(name = "order_count")
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	
	@Column(name = "flow_rate")
	public String getFlowRate() {
		return flowRate;
	}
	public void setFlowRate(String flowRate) {
		this.flowRate = flowRate;
	}
	
	@Column(name = "flow_interval_time")
	public String getFlowIntervalTime() {
		return flowIntervalTime;
	}
	public void setFlowIntervalTime(String flowIntervalTime) {
		this.flowIntervalTime = flowIntervalTime;
	}

	@Column(name = "order_id")
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name = "status_name")
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	@Column(name = "status_desc")
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	
	
}
