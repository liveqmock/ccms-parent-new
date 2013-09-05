package com.yunat.ccms.node.support.validation;

import java.util.List;

import com.yunat.ccms.node.spi.support.ValidateMessage;

public class WorkflowValidateResultVO {

	private Long campaignId;
	private Long workflowId;
	private boolean pass;
	private String visit;
	private List<ValidateMessage> details;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getVisit() {
		return visit;
	}

	public void setVisit(String visit) {
		this.visit = visit;
	}

	public List<ValidateMessage> getDetails() {
		return details;
	}

	public void setDetails(List<ValidateMessage> details) {
		this.details = details;
	}

}
