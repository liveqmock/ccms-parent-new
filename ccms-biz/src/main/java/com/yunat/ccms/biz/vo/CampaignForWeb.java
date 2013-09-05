package com.yunat.ccms.biz.vo;

import com.yunat.ccms.core.support.auth.AclManaged;

@AclManaged(value = "campaign", getIdMethodName = "getCampId")
public class CampaignForWeb {

	private long campId;
	private String campName;
	private String createdTime;
	private String startTime;
	private String endTime;
	private String status;
	private String edition;
	private String picUrl;
	private String creater;
	private String investigator;
	private String progName;

	public long getCampId() {
		return campId;
	}

	public void setCampId(final long campId) {
		this.campId = campId;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(final String campName) {
		this.campName = campName;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(final String createdTime) {
		this.createdTime = createdTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(final String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(final String endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(final String edition) {
		this.edition = edition;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(final String picUrl) {
		this.picUrl = picUrl;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(final String creater) {
		this.creater = creater;
	}

	public String getInvestigator() {
		return investigator;
	}

	public void setInvestigator(final String investigator) {
		this.investigator = investigator;
	}

	public String getProgName() {
		return progName;
	}

	public void setProgName(final String progName) {
		this.progName = progName;
	}

}