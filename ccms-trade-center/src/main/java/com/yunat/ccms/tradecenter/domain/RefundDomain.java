package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plt_taobao_refund")
public class RefundDomain extends BaseDomain {

	/**
	 *
	 */
	private static final long serialVersionUID = 2615291688071722778L;

	@Id
	@Column(name="refund_id")
	private String refundId;

	@Column(name="tid")
	private String tid;

	@Column(name="oid")
	private String oid;

	@Column(name="dp_id")
	private String dpId;

	@Column(name="total_fee")
	private String totalFee;

	@Column(name="buyer_nick")
	private String buyerNick;

	@Column(name="seller_nick")
	private String sellerNick;

	@Column(name="order_status")
	private String orderStatus;

	@Column(name="status")
	private String status;

	@Column(name="good_status")
	private String goodStatus;

	@Column(name="has_good_return")
	private String hasGoodReturn;

	@Column(name="refund_fee")
	private Double refundFee;

	private String payment;

	private String reason;

	@Column(name="refund_desc")
	private String refundDesc;

	@Column(name="title")
	private String title;

	@Column(name="num")
	private Integer num;

	@Column(name="company_name")
	private String companyName;

	@Column(name="sid")
	private String sid;

	@Column(name="cs_status")
	private Integer csStatus;

	@Column(name="price")
	private Double price;

	@Column(name="good_return_time")
	private Date goodReturnTime;

	@Column(name="num_iid")
	private String numIid;

	@Column(name="remind_type")
	private Integer remindType;

	@Column(name="exist_timeout")
	private String existTimeout;

	private Date timeout;

	private Date created;

	private Date modified;

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
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

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getSellerNick() {
		return sellerNick;
	}

	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoodStatus() {
		return goodStatus;
	}

	public void setGoodStatus(String goodStatus) {
		this.goodStatus = goodStatus;
	}

	public String getHasGoodReturn() {
		return hasGoodReturn;
	}

	public void setHasGoodReturn(String hasGoodReturn) {
		this.hasGoodReturn = hasGoodReturn;
	}

	public Double getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Double refundFee) {
		this.refundFee = refundFee;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Integer getCsStatus() {
		return csStatus;
	}

	public void setCsStatus(Integer csStatus) {
		this.csStatus = csStatus;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getGoodReturnTime() {
		return goodReturnTime;
	}

	public void setGoodReturnTime(Date goodReturnTime) {
		this.goodReturnTime = goodReturnTime;
	}

	public String getNumIid() {
		return numIid;
	}

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	public Integer getRemindType() {
		return remindType;
	}

	public void setRemindType(Integer remindType) {
		this.remindType = remindType;
	}

	public String getExistTimeout() {
		return existTimeout;
	}

	public void setExistTimeout(String existTimeout) {
		this.existTimeout = existTimeout;
	}

	public Date getTimeout() {
		return timeout;
	}

	public void setTimeout(Date timeout) {
		this.timeout = timeout;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
}
