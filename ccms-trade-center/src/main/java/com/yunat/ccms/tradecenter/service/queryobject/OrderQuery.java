package com.yunat.ccms.tradecenter.service.queryobject;


public class OrderQuery extends BaseQuery{
	/**
	 *
	 */
	private static final long serialVersionUID = -8965793635421211592L;

	private String dpId;
	private String customerno;
	private String mobile;
	private Integer urpayStatus;
	private String serviceStaffName;
	private String createdStartTime;
	private String createdEndTime;
	private Boolean isHide;
	private String title;
	private String outerIid;
	private String order;

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getCustomerno() {
		return customerno;
	}
	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String receiverMobile) {
		this.mobile = receiverMobile;
	}
	public Integer getUrpayStatus() {
		return urpayStatus;
	}
	public void setUrpayStatus(Integer urpayQueryType) {
		this.urpayStatus = urpayQueryType;
	}
	public String getServiceStaffName() {
		return serviceStaffName;
	}
	public void setServiceStaffName(String serviceStaffId) {
		this.serviceStaffName = serviceStaffId;
	}

	public String getCreatedStartTime() {
		return createdStartTime;
	}

	public void setCreatedStartTime(String createdStartTime) {
		this.createdStartTime = createdStartTime;
	}

	public String getCreatedEndTime() {
		return createdEndTime;
	}

	public void setCreatedEndTime(String createdEndTime) {
		this.createdEndTime = createdEndTime;
	}

	public Boolean getIsHide() {
		return isHide;
	}

	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOuterIid() {
		return outerIid;
	}
	public void setOuterIid(String outerIid) {
		this.outerIid = outerIid;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
