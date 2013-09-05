package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_tc_care_status")
public class CareStatusDomain extends BaseDomain {

	/**
	 *
	 */
	private static final long serialVersionUID = -9105260494793851988L;

	/**
	 * 订单id
	 */
	private String tid;

	/**
	 * 下单关怀状态
	 * 0：默认，1：已催付，2：次日催付
	 *
	 */
	private Integer orderCareStatus;

	/**
	 * 发货通知状态
	 * 0：默认，1：已催付，2：次日催付
	 *
	 */
	private Integer shipmentCareStatus;

	/**
	 * 同城通知状态
	 * 0：默认，1：已催付，2：次日催付
	 *
	 */
	private Integer arriveCareStatus;

	/**
	 * 派件通知状态
	 * 0：默认，1：已催付，2：次日催付
	 *
	 */
	private Integer deliveryCareStatus;

	/**
	 * 签收通知状态
	 * 0：默认，1：已催付，2：次日催付
	 */
	private Integer signCareStatus;

	/**
	 * 退款关怀状态
	 * 0：默认，1：已催付，2：次日催付
	 */
	private Integer refundCareStatus;

	/**
	 * 确认收货关怀状态
	 * 0：默认，1：已催付，2：次日催付
	 */
	private Integer confirmCareStatus;

	/**
	 * 评价关怀状态
	 * 0：默认，1：已催付，2：次日催付
	 */
	private Integer assessCareStatus;

	private Date updated;
	private Date created;

	@Id
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	@Column(name="order_care_status")
	public Integer getOrderCareStatus() {
		return orderCareStatus;
	}
	public void setOrderCareStatus(Integer orderCareStatus) {
		this.orderCareStatus = orderCareStatus;
	}

	@Column(name="shipment_care_status")
	public Integer getShipmentCareStatus() {
		return shipmentCareStatus;
	}
	public void setShipmentCareStatus(Integer shipmentCareStatus) {
		this.shipmentCareStatus = shipmentCareStatus;
	}

	@Column(name="arrive_care_status")
	public Integer getArriveCareStatus() {
		return arriveCareStatus;
	}
	public void setArriveCareStatus(Integer arriveCareStatus) {
		this.arriveCareStatus = arriveCareStatus;
	}

	@Column(name="delivery_care_status")
	public Integer getDeliveryCareStatus() {
		return deliveryCareStatus;
	}
	public void setDeliveryCareStatus(Integer deliveryCareStatus) {
		this.deliveryCareStatus = deliveryCareStatus;
	}

	@Column(name="sign_care_status")
	public Integer getSignCareStatus() {
		return signCareStatus;
	}
	public void setSignCareStatus(Integer signCareStatus) {
		this.signCareStatus = signCareStatus;
	}

	@Column(name="refund_care_status")
	public Integer getRefundCareStatus() {
		return refundCareStatus;
	}
	public void setRefundCareStatus(Integer refundCareStatus) {
		this.refundCareStatus = refundCareStatus;
	}

	@Column(name="confirm_care_status")
	public Integer getConfirmCareStatus() {
		return confirmCareStatus;
	}
	public void setConfirmCareStatus(Integer confirmCareStatus) {
		this.confirmCareStatus = confirmCareStatus;
	}

	@Column(name="assess_care_status")
	public Integer getAssessCareStatus() {
		return assessCareStatus;
	}
	public void setAssessCareStatus(Integer assessCareStatus) {
		this.assessCareStatus = assessCareStatus;
	}

	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}
