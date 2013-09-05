package com.yunat.ccms.tradecenter.controller.vo;

import com.yunat.ccms.tradecenter.controller.BaseVO;

public class SendLogVO extends BaseVO {
	private String serviceStaff;
	private String date;
	private Integer manualUrpayStatus;
	private String content;

	public String getServiceStaff() {
		return serviceStaff;
	}
	public void setServiceStaff(String serviceStaff) {
		this.serviceStaff = serviceStaff;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getManualUrpayStatus() {
		return manualUrpayStatus;
	}
	public void setManualUrpayStatus(Integer manualUrpayStatus) {
		this.manualUrpayStatus = manualUrpayStatus;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
