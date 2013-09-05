package com.yunat.ccms.report.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "evaluate_report_day_result")
public class EvaluateReportDayResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1703300322661502298L;

	private Long evaluateResultId;

	private Long jobId;

	private Long nodeId;

	private String evaluateTime;

	private Long buyOrderCount;

	private Long buyCustomerCount;

	private Double buyPaymentSum;

	private Long payOrderCount;

	private Long payCustomerCount;

	private Double payPaymentSum;

	private Double customerAvgFee;

	private Long productCount;

	private String roi;

	public EvaluateReportDayResult() {
	}

	// dont delete this Construct
	public EvaluateReportDayResult(String evaluateTime, Long buyOrderCount, Long buyCustomerCount,
			Double buyPaymentSum, Long payOrderCount, Long payCustomerCount, Double payPaymentSum, Long productCount) {
		setEvaluateTime(evaluateTime);
		setBuyOrderCount(buyOrderCount);
		setBuyCustomerCount(buyCustomerCount);
		setBuyPaymentSum(buyPaymentSum);
		setPayOrderCount(payOrderCount);
		setPayCustomerCount(payCustomerCount);
		setPayPaymentSum(payPaymentSum);
		setProductCount(productCount);
	}

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

	@Column(name = "evaluate_time", nullable = false)
	@NotNull
	public String getEvaluateTime() {
		return evaluateTime;
	}

	public void setEvaluateTime(String evaluateTime) {
		this.evaluateTime = evaluateTime;
	}

	@Column(name = "buy_order_count")
	public Long getBuyOrderCount() {
		return buyOrderCount;
	}

	public void setBuyOrderCount(Long buyOrderCount) {
		this.buyOrderCount = buyOrderCount;
	}

	@Column(name = "buy_customer_count")
	public Long getBuyCustomerCount() {
		return buyCustomerCount;
	}

	public void setBuyCustomerCount(Long buyCustomerCount) {
		this.buyCustomerCount = buyCustomerCount;
	}

	@Column(name = "buy_payment_sum")
	public Double getBuyPaymentSum() {
		return buyPaymentSum;
	}

	public void setBuyPaymentSum(Double buyPaymentSum) {
		this.buyPaymentSum = buyPaymentSum;
	}

	@Column(name = "pay_order_count")
	public Long getPayOrderCount() {
		return payOrderCount;
	}

	public void setPayOrderCount(Long payOrderCount) {
		this.payOrderCount = payOrderCount;
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

	@Column(name = "customer_avg_fee")
	public Double getCustomerAvgFee() {
		return customerAvgFee;
	}

	public void setCustomerAvgFee(Double customerAvgFee) {
		this.customerAvgFee = customerAvgFee;
	}

	@Column(name = "product_count")
	public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}

	@Column(name = "roi")
	public String getRoi() {
		return roi;
	}

	public void setRoi(String roi) {
		this.roi = roi;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.evaluateResultId != null ? this.evaluateResultId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the
		// evaluateResultId fields are not set
		if (!(object instanceof EvaluateReportDayResult)) {
			return false;
		}
		EvaluateReportDayResult other = (EvaluateReportDayResult) object;
		if (this.evaluateResultId != other.evaluateResultId
				&& (this.evaluateResultId == null || !this.evaluateResultId.equals(other.evaluateResultId)))
			return false;
		return true;
	}

}
