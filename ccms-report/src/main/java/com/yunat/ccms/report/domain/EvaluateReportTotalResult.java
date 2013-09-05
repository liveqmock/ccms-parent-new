package com.yunat.ccms.report.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "evaluate_report_total_result")
public class EvaluateReportTotalResult {

	private static final long serialVersionUID = -1703300322661502298L;

	private Long evaluateResultId;

	private Long jobId;

	private Long nodeId;

	private Long payCustomerCount;

	private Double payPaymentSum;

	private Long productCount;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluate_result_Id", unique = true, nullable = false, precision = 20, scale = 0)
	@NotNull
	public Long getEvaluateResultId() {
		return evaluateResultId;
	}

	public void setEvaluateResultId(Long evaluateResultId) {
		this.evaluateResultId = evaluateResultId;
	}

	@Column(name = "job_id", nullable = false)
	@NotNull
	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "node_id", nullable = false)
	@NotNull
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "pay_customer_count")
	public Long getPayCustomerCount() {
		return payCustomerCount;
	}

	public void setPayCustomerCount(Long payCustomerCount) {
		this.payCustomerCount = payCustomerCount;
	}

	@Column(name = "pay_payment_sum")
	public Double getPayPaymentSum() {
		return payPaymentSum;
	}

	public void setPayPaymentSum(Double payPaymentSum) {
		this.payPaymentSum = payPaymentSum;
	}

	@Column(name = "product_count")
	public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}

}
