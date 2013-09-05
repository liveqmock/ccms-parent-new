package com.yunat.ccms.node.biz.sms;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "twf_node_sms_execution_record")
public class ExecutionRecord implements Serializable {
	private static final long serialVersionUID = -3709374333928835612L;

	private Long id;
	private Long nodeId;
	private Long subjobId;
	private Long targetGroupCustomers; // 目标组客户数
	private Long controlGroupCustomers; // 控制组用户数
	private Long validPhoneAmount; // 手机号码有效数
	private Long invalidPhoneAmount; // 无效手机号码数
	
	private Long sendingTotalNum; // 短信发送总条数
	private Double sendingPrice; // 发送时刻单价
	private Date createdTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "node_id")
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "subjob_id")
	public Long getSubjobId() {
		return subjobId;
	}

	public void setSubjobId(Long subjobId) {
		this.subjobId = subjobId;
	}

	@Column(name = "target_group_customers")
	public Long getTargetGroupCustomers() {
		return targetGroupCustomers;
	}

	public void setTargetGroupCustomers(Long targetGroupCustomers) {
		this.targetGroupCustomers = targetGroupCustomers;
	}

	@Column(name = "control_group_customers")
	public Long getControlGroupCustomers() {
		return controlGroupCustomers;
	}

	public void setControlGroupCustomers(Long controlGroupCustomers) {
		this.controlGroupCustomers = controlGroupCustomers;
	}

	@Column(name = "valid_phone_amount")
	public Long getValidPhoneAmount() {
		return validPhoneAmount;
	}

	public void setValidPhoneAmount(Long validPhoneAmount) {
		this.validPhoneAmount = validPhoneAmount;
	}

	@Column(name = "invalid_phone_amount")
	public Long getInvalidPhoneAmount() {
		return invalidPhoneAmount;
	}

	public void setInvalidPhoneAmount(Long invalidPhoneAmount) {
		this.invalidPhoneAmount = invalidPhoneAmount;
	}

	@Column(name = "sending_total_num")
	public Long getSendingTotalNum() {
		return sendingTotalNum;
	}

	public void setSendingTotalNum(Long sendingTotalNum) {
		this.sendingTotalNum = sendingTotalNum;
	}

	@Column(name = "sending_price")
	public Double getSendingPrice() {
		return sendingPrice;
	}

	public void setSendingPrice(Double sendingPrice) {
		this.sendingPrice = sendingPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}