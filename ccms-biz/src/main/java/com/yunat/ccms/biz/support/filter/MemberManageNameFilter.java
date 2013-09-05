package com.yunat.ccms.biz.support.filter;

import com.yunat.ccms.biz.support.cons.WorkflowEnum;


public class MemberManageNameFilter implements CampaignSimpleFilter {

	private String filterName;
	private String platCode;

	@Override
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	@Override
	public String getFilterName() {
		return filterName;
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
		return WorkflowEnum.MEMBER_MANAGE.getCode();
	}

}