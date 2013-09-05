package com.yunat.ccms.biz.support.filter;

public interface CampaignFilter extends CampaignSimpleFilter {

	public void setCampState(String campState);

	public String getCampState();

	public void setCampCategory(String campCategory);

	public String getCampCategory();

	public void setKeywords(String keywords);

	public String getKeywords();
	
	public Long getProgramId();

	public void setProgramId(Long programId);
	
}
