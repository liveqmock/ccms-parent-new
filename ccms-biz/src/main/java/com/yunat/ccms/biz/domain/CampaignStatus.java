package com.yunat.ccms.biz.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tds_campaign_status")
public class CampaignStatus implements Serializable, Comparable<CampaignStatus> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2015520218131845117L;

	private String statusId;
	private String statusValue;
	private Long orderId;
	
	public CampaignStatus(){
	}
	
	public CampaignStatus(String statusId){
		this.statusId = statusId;
	}
	

	@Id
	@Column(name = "status_id", length = 20)
	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	@Column(name = "status_value")
	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	@Column(name = "orderid", nullable = false)
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Transient
	@Override
	public int compareTo(CampaignStatus campState) {
		Long otherId = campState.getOrderId();
		if (orderId.compareTo(otherId) < 0) {
			return -1;
		} else if (orderId.compareTo(otherId) > 0) {
			return 1;
		}
		return 0;
	}

}
