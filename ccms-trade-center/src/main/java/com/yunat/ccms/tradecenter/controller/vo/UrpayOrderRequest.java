package com.yunat.ccms.tradecenter.controller.vo;

import com.yunat.ccms.tradecenter.support.cons.SysType;


public class UrpayOrderRequest {
	private String tid;
	private String smsContent;
	private long gatewayId;
	private boolean filterBlacklist;

	// 催付成功订单
	private int currPage;
	private int pageSize;
	private String dpId;

	private Integer type;
	private String mobileOrNickOrTid;

	private Long startCreated;
	private Long endCreated;

	private Long sendStartCreated;
	private Long sendEndCreated;

	// 批量隐藏
	private boolean isHide;
	private String hideColumnName;
	// 批量催付
	private String[] tids;
	// 旺旺催付需要
	private String note;

	private SysType sysType;

	public SysType getSysType() {
		return sysType;
	}

	public void setSysType(SysType sysType) {
		this.sysType = sysType;
	}

	public String getHideColumnName() {
		return hideColumnName;
	}

	public void setHideColumnName(String hideColumnName) {
		this.hideColumnName = hideColumnName;
	}

	public Long getSendStartCreated() {
		return sendStartCreated;
	}

	public void setSendStartCreated(Long sendStartCreated) {
		this.sendStartCreated = sendStartCreated;
	}

	public Long getSendEndCreated() {
		return sendEndCreated;
	}

	public void setSendEndCreated(Long sendEndCreated) {
		this.sendEndCreated = sendEndCreated;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMobileOrNickOrTid() {
		return mobileOrNickOrTid;
	}

	public void setMobileOrNickOrTid(String mobileOrNickOrTid) {
		this.mobileOrNickOrTid = mobileOrNickOrTid;
	}

	public Long getStartCreated() {
		return startCreated;
	}

	public void setStartCreated(Long startCreated) {
		this.startCreated = startCreated;
	}

	public Long getEndCreated() {
		return endCreated;
	}

	public void setEndCreated(Long endCreated) {
		this.endCreated = endCreated;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setIsHide(boolean isHide) {
		this.isHide = isHide;
	}

	public int getCurrPage() {
		return currPage;
	}

	public String getDpId() {
		return dpId;
	}

	public long getGatewayId() {
		return gatewayId;
	}

	public String getNote() {
		return note;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public String getTid() {
		return tid;
	}

	public String[] getTids() {
		return tids;
	}

	public boolean isFilterBlacklist() {
		return filterBlacklist;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public void setFilterBlacklist(boolean filterBlacklist) {
		this.filterBlacklist = filterBlacklist;
	}

	public void setGatewayId(long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public void setTids(String[] tids) {
		this.tids = tids;
	}

}
