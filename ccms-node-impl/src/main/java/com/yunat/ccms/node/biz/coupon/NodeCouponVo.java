package com.yunat.ccms.node.biz.coupon;

public class NodeCouponVo {

	private Long nodeId;
	private Long channelId;
	private String shopId;
	private Long couponId;
	private String previewCustomers;
	private Integer outputControl = 1;
	private String remark;

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getPreviewCustomers() {
		return previewCustomers;
	}

	public void setPreviewCustomers(String previewCustomers) {
		this.previewCustomers = previewCustomers;
	}

	public Integer getOutputControl() {
		return outputControl;
	}

	public void setOutputControl(Integer outputControl) {
		this.outputControl = outputControl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
