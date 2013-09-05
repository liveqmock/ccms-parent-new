package com.yunat.ccms.report.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateSerializer;

/**
 * 评估节点-订单明细-实体
 * 
 * @author yin
 * 
 */

@Entity
@Table(name = "twf_node_evaluate_order_detail")
public class EvaluateReportOrderDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8608636098610070119L;

	// 非业务字段主键ID
	private Long evaluateOrderId;

	private Long jobId;

	private Long nodeId;

	private String evaluateTime;

	private String tid;

	private String customerno;

	private Date created;

	private Date payTime;

	private Date consignTime;

	private Double totalFee;

	private Double payment;

	private String status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluate_order_id", unique = true, nullable = false, precision = 20, scale = 0)
	public Long getEvaluateOrderId() {
		return evaluateOrderId;
	}

	public void setEvaluateOrderId(Long evaluateOrderId) {
		this.evaluateOrderId = evaluateOrderId;
	}

	@Column(name = "job_id")
	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "node_id")
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "evaluate_time")
	public String getEvaluateTime() {
		return evaluateTime;
	}

	public void setEvaluateTime(String evaluateTime) {
		this.evaluateTime = evaluateTime;
	}

	@Column(name = "tid")
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	@Column(name = "customerno")
	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "pay_time")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	@Column(name = "consign_time")
	public Date getConsignTime() {
		return consignTime;
	}

	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}

	@Column(name = "total_fee")
	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	@Column(name = "payment")
	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.evaluateOrderId != null ? this.evaluateOrderId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the
		// evaluateResultId fields are not set
		if (!(object instanceof EvaluateReportResult)) {
			return false;
		}
		EvaluateReportOrderDetail other = (EvaluateReportOrderDetail) object;
		if (this.evaluateOrderId != other.evaluateOrderId
				&& (this.evaluateOrderId == null || !this.evaluateOrderId.equals(other.evaluateOrderId)))
			return false;
		return true;
	}

}
