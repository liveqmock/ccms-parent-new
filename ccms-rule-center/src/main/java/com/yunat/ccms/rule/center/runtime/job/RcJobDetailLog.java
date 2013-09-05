package com.yunat.ccms.rule.center.runtime.job;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rc_job_detail_log")
public class RcJobDetailLog {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "tid", nullable = false)
	private String tid;

	/*** 店铺id */
	@Column(name = "shop_id")
	private String shopId;

	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "rule_id")
	private Long ruleId;

	@Column(name = "count_flag")
	private boolean countFlag = false;

	public RcJobDetailLog() {
	}

	public RcJobDetailLog(String tid, String shopId, Long planId, Long ruleId) {
		super();
		this.tid = tid;
		this.shopId = shopId;
		this.planId = planId;
		this.ruleId = ruleId;
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

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public boolean getCountFlag() {
		return countFlag;
	}

	public void setCountFlag(boolean countFlag) {
		this.countFlag = countFlag;
	}

	@Override
	public String toString() {
		return "RcJobDetailLog [id=" + id + ", tid=" + tid + ", shopId=" + shopId + ", planId=" + planId + ", ruleId="
				+ ruleId + "]";
	}

}
