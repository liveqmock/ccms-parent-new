package com.yunat.ccms.rule.center.runtime.job;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rc_job")
public class RcJob {

	@Id
	@Column(name = "tid", nullable = false)
	private String tid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified")
	private Date modified;

	@Column(name = "shop_id", nullable = false)
	private String shopId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submit_time")
	private Date submitTime;

	@Column(name = "job_status")
	private String status;

	@Column(name = "error_code")
	private String errorCode;

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
