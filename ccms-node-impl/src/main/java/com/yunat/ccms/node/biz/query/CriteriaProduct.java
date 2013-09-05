package com.yunat.ccms.node.biz.query;

public class CriteriaProduct {

	private String id; // num_iid;商品ID
	private String title; // 商品标题
	private String dpId; // 店铺ID
	private String outerId; // 外部编码

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
}
