package com.yunat.ccms.report.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 评估节点-商品明细-实体
 * 
 * @author yin
 * 
 */
@Entity
@Table(name = "twf_node_evaluate_product_detail")
public class EvaluateReportProductDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7841195794578087552L;

	private Long evaluateProductId;

	private Long jobId;

	private Long nodeId;

	private String evaluateTime;

	private String productId;

	private String productName;

	private Integer customerCount;

	private Integer orderCount;

	private Integer buyNum;

	private Double buyFee;

	private String outerId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluate_product_id", unique = true, nullable = false, precision = 20, scale = 0)
	public Long getEvaluateProductId() {
		return evaluateProductId;
	}

	public void setEvaluateProductId(Long evaluateProductId) {
		this.evaluateProductId = evaluateProductId;
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

	@Column(name = "product_id")
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(name = "product_name")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "customer_count")
	public Integer getCustomerCount() {
		return customerCount;
	}

	public void setCustomerCount(Integer customerCount) {
		this.customerCount = customerCount;
	}

	@Column(name = "order_count")
	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	@Column(name = "buy_num")
	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}

	@Column(name = "buy_fee")
	public Double getBuyFee() {
		return buyFee;
	}

	public void setBuyFee(Double buyFee) {
		this.buyFee = buyFee;
	}

	@Column(name = "outer_id")
	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.evaluateProductId != null ? this.evaluateProductId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the
		// evaluateResultId fields are not set
		if (!(object instanceof EvaluateReportResult)) {
			return false;
		}
		EvaluateReportProductDetail other = (EvaluateReportProductDetail) object;
		if (this.evaluateProductId != other.evaluateProductId
				&& (this.evaluateProductId == null || !this.evaluateProductId.equals(other.evaluateProductId)))
			return false;
		return true;
	}

}
