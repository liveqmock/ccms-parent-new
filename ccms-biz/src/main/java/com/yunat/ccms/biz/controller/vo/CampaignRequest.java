package com.yunat.ccms.biz.controller.vo;

import java.util.Date;

public class CampaignRequest {
	private String campName;
	private Long progId;
	private Long templateId;
	private Long campTypeId;
	private Long inverstigatorId;
	private Date startTime;
	private Date endTime;
	private String campDesc;
	private String platCode;

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
	}

	public Long getProgId() {
		return progId;
	}

	public void setProgId(Long progId) {
		this.progId = progId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getCampTypeId() {
		return campTypeId;
	}

	public void setCampTypeId(Long campTypeId) {
		this.campTypeId = campTypeId;
	}

	public Long getInverstigatorId() {
		return inverstigatorId;
	}

	public void setInverstigatorId(Long inverstigatorId) {
		this.inverstigatorId = inverstigatorId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCampDesc() {
		return campDesc;
	}

	public void setCampDesc(String campDesc) {
		this.campDesc = campDesc;
	}

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

}