package com.yunat.ccms.rule.center.conf.drl;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.yunat.ccms.rule.center.conf.drl.PlanDRL.PlanDRLPK;

@Entity
@Table(name = "rc_drl")
@IdClass(PlanDRLPK.class)
public class PlanDRL implements Serializable {

	private static final long serialVersionUID = 8154012263772725146L;

	@Id
	@Column(name = "shop_id")
	private String shopId;

	@Id
	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "drl")
	private String drl;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "operator_id")
	private Long operatorId;

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

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public static class PlanDRLPK implements Serializable {
		/***  */
		private static final long serialVersionUID = 4929095008322783425L;
		private String shopId;
		private Long planId;

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
	}

}