package com.yunat.ccms.rule.center.conf.drl;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rc_drl_changelog")
public class PlanDRLChangelog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3348305763505229097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10, scale = 0)
	private Long changlogId;

	@Column(name = "shop_id")
	private String shopId;

	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "drl")
	private String drl;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "operator_id")
	private Long operatorId;

	public PlanDRLChangelog() {
	}

	public PlanDRLChangelog(PlanDRL planDRL) {
		this.shopId = planDRL.getShopId();
		this.planId = planDRL.getPlanId();
		this.drl = planDRL.getDrl();
		this.startTime = planDRL.getStartTime();
		this.endTime = new Date();
		this.operatorId = planDRL.getOperatorId();
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getDrl() {
		return drl;
	}

	public void setDrl(String drl) {
		this.drl = drl;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getChanglogId() {
		return changlogId;
	}

	public void setChanglogId(Long changlogId) {
		this.changlogId = changlogId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

}