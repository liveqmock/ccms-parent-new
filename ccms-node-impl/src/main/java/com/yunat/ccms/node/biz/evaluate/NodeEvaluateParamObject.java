package com.yunat.ccms.node.biz.evaluate;

import java.util.Date;

public class NodeEvaluateParamObject {

	private Long jobId;
	private Long nodeId;
	private String previousNodeOutput;
	private Date startDate;
	private Date endDate;
	private String shopId;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getPreviousNodeOutput() {
		return previousNodeOutput;
	}

	public void setPreviousNodeOutput(String previousNodeOutput) {
		this.previousNodeOutput = previousNodeOutput;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

}
