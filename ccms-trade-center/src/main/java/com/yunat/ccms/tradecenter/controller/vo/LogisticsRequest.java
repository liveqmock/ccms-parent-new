package com.yunat.ccms.tradecenter.controller.vo;

/**
 * 物流事务 ， 请求参数对象
 *
 * @author ming.peng
 * @date 2013-7-9
 * @since 4.2.0
 */
public class LogisticsRequest {

	private String tid;

	/** 延长天数 */
	private Long extensionDays;

	/** 店铺ID 即 shopId */
	private String dpId;

	private String[] tids;

	private boolean isHide;

	/** 延长天数 */
	public Long getExtensionDays() {
		return extensionDays;
	}

	public void setExtensionDays(Long extensionDays) {
		this.extensionDays = extensionDays;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String[] getTids() {
		return tids;
	}

	public void setTids(String[] tids) {
		this.tids = tids;
	}

	public String getTid() {
		return tid;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setIsHide(boolean isHide) {
		this.isHide = isHide;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

}
