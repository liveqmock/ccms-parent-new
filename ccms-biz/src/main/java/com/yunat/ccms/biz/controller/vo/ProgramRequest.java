package com.yunat.ccms.biz.controller.vo;

import java.util.Date;

public class ProgramRequest {
	private String progName;
	private Long progType;
	private Date startTime;
	private Date endTime;
	private String progDesc;
	private String platCode;

	public String getProgName() {
		return progName;
	}

	public void setProgName(String progName) {
		this.progName = progName;
	}

	public Long getProgType() {
		return progType;
	}

	public void setProgType(Long progType) {
		this.progType = progType;
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

	public String getProgDesc() {
		return progDesc;
	}

	public void setProgDesc(String progDesc) {
		this.progDesc = progDesc;
	}

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

}