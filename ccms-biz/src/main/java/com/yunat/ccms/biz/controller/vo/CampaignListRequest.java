package com.yunat.ccms.biz.controller.vo;

public class CampaignListRequest extends PageListRequest {
	private String show_activity;
	private String campState;

	public String getShow_activity() {
		return show_activity;
	}

	public void setShow_activity(String show_activity) {
		this.show_activity = show_activity;
	}

	public String getCampState() {
		return campState;
	}

	public void setCampState(String campState) {
		this.campState = campState;
	}

}