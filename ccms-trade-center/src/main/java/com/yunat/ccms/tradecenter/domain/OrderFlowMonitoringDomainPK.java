package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderFlowMonitoringDomainPK extends BaseDomain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583115988505305896L;
	
	private String dpId;
	private String orderStatus;
	
	@Column(name = "dp_id")
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	
	@Column(name = "order_status")
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
