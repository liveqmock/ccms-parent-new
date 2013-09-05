package com.yunat.ccms.tradecenter.controller.vo;

/**
 * 告警配置VO-对应前台参数
 * 
 * @author tim.yin
 * 
 */
public class WarnConfigVO {

	// 店铺
	private String dpId;

	// 告警开始时间
	private String warnStartTime;

	// 告警结束时间
	private String warnEndTime;

	// 告警类型
	private Integer warnType;

	// 告警内容
	private String content;

	// 发送给卖家的手机号
	private String mobiles;

	// 开关标记 0:关闭 1:开启
	private Integer isOpen;

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getWarnStartTime() {
		return warnStartTime;
	}

	public void setWarnStartTime(String warnStartTime) {
		this.warnStartTime = warnStartTime;
	}

	public String getWarnEndTime() {
		return warnEndTime;
	}

	public void setWarnEndTime(String warnEndTime) {
		this.warnEndTime = warnEndTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public Integer getWarnType() {
		return warnType;
	}

	public void setWarnType(Integer warnType) {
		this.warnType = warnType;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

}
