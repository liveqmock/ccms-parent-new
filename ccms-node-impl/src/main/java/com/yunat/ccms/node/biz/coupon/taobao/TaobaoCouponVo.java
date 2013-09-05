package com.yunat.ccms.node.biz.coupon.taobao;

public class TaobaoCouponVo {

	private String couponName;
	private String startTime;
	private String endTime;
	private Long threshold;
	private String shopId;
	private Long denominationValue;

	private Boolean enable;
	private String remark;

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getThreshold() {
		return threshold;
	}

	public void setThreshold(Long threshold) {
		this.threshold = threshold;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean isEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Long getDenominationValue() {
		return denominationValue;
	}

	public void setDenominationValue(Long denominationValue) {
		this.denominationValue = denominationValue;
	}

	@Override
	public String toString() {
		return "TaobaoCouponVo [couponName=" + couponName + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", threshold=" + threshold + ", shopId=" + shopId + ", denominationValue=" + denominationValue
				+ ", enable=" + enable + ", remark=" + remark + "]";
	}

}
