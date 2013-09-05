package com.yunat.ccms.biz.support.filter;

import com.yunat.ccms.biz.support.cons.WorkflowEnum;


public class NormalCampaignFilter implements CampaignFilter {
	private String filterName;
	private String campState;
	private String campCategory;
	private String keywords;
	private Boolean isSelf;
	private Long programId;
	private String platCode;

	@Override
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	@Override
	public String getFilterName() {
		return this.filterName;
	}

	@Override
	public void setCampState(String campState) {
		this.campState = campState;
	}

	@Override
	public String getCampState() {
		return this.campState;
	}

	@Override
	public void setCampCategory(String campCategory) {
		this.campCategory = campCategory;
	}

	@Override
	public String getCampCategory() {
		return this.campCategory;
	}

	@Override
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public String getKeywords() {
		return this.keywords;
	}

	public Boolean getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(Boolean isSelf) {
		this.isSelf = isSelf;
	}

	@Override
	public Long getProgramId() {
		return this.programId;
	}

	@Override
	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	@Override
	public String getPlatCode() {
		return this.platCode;
	}

	@Override
	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	@Override
	public String getWorkflowType() {
		return WorkflowEnum.STANDARD.getCode();
	}
	
}