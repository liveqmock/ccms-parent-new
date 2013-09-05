package com.yunat.ccms.biz.support.filter;



public interface CampaignSimpleFilter {
	public void setFilterName(String filterName);

	public String getFilterName();

	public String getWorkflowType();
	
	public String getPlatCode();
	
	public void setPlatCode(String platCode);
	
}