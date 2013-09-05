package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_tc_warn_status")
public class WarnStatusDomain extends BaseDomain {

	/**
	 *
	 */
	private static final long serialVersionUID = -8419739198121214029L;

	@Id
	@Column(name = "oid")
	private String oid;

	@Column(name = "created")
	private Date created;

	@Column(name = "updated")
	private Date updated;

	@Column(name = "not_good_warn_status")
	private Integer notGoodWarnStatus;

	@Column(name = "not_good_warn_time")
	private Date notGoodWarnTime;

	@Column(name = "refund_warn_status")
	private Integer refundWarnStatus;

	@Column(name = "refund_warn_time")
	private Date refundWarnTime;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Integer getNotGoodWarnStatus() {
		return notGoodWarnStatus;
	}

	public void setNotGoodWarnStatus(Integer notGoodWarnStatus) {
		this.notGoodWarnStatus = notGoodWarnStatus;
	}

	public Date getNotGoodWarnTime() {
		return notGoodWarnTime;
	}

	public void setNotGoodWarnTime(Date notGoodWarnTime) {
		this.notGoodWarnTime = notGoodWarnTime;
	}

	public Integer getRefundWarnStatus() {
		return refundWarnStatus;
	}

	public void setRefundWarnStatus(Integer refundWarnStatus) {
		this.refundWarnStatus = refundWarnStatus;
	}

	public Date getRefundWarnTime() {
		return refundWarnTime;
	}

	public void setRefundWarnTime(Date refundWarnTime) {
		this.refundWarnTime = refundWarnTime;
	}


}
