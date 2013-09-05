package com.yunat.ccms.schedule.controler.vo;

import java.util.List;

public class CampaignSnapShot {

	/*** 活动id */
	private Long campaignId;
	/*** 活动状态 */
	private String campaignStatus;
	/*** 活动中各个周期状态状态 */
	private List<JobSnapShot> jobStatusList;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public List<JobSnapShot> getJobStatusList() {
		return jobStatusList;
	}

	public void setJobStatusList(List<JobSnapShot> jobStatusList) {
		this.jobStatusList = jobStatusList;
	}

}
