package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 退款关怀状态表
 *
 * @author ming.peng
 * @date 2013-7-16
 * @since 4.2.0
 */
@Entity
@Table(name = "tb_tc_refund_status")
public class RefundCareDomain extends BaseDomain {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkid")
	private Long pkid;

	@Column(name = "tid")
	private String tid;

	@Column(name = "oid")
	private String oid;

	@Column(name = "dp_id")
	private String dpId;

	@Column(name = "auto_refund_care")
	private Integer autoRefundCare;

	@Column(name = "refund_care")
	private Boolean refundCare;

    @Column(name = "refund_followup")
    private Boolean refundFollowup;

    @Column(name = "traderate_followup")
    private Boolean traderateFollowup;

	@Column(name = "created")
	private Date created;

	@Column(name = "updated")
	private Date updated;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public Integer getAutoRefundCare() {
		return autoRefundCare;
	}

	public void setAutoRefundCare(Integer autoRefundCare) {
		this.autoRefundCare = autoRefundCare;
	}

	public Boolean getRefundCare() {
		return refundCare;
	}

	public void setRefundCare(Boolean refundCare) {
		this.refundCare = refundCare;
	}

    public Boolean getRefundFollowup() {
        return refundFollowup;
    }

    public void setRefundFollowup(Boolean refundFollowup) {
        this.refundFollowup = refundFollowup;
    }

    public Boolean getTraderateFollowup() {
        return traderateFollowup;
    }

    public void setTraderateFollowup(Boolean traderateFollowup) {
        this.traderateFollowup = traderateFollowup;
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

}
