package com.yunat.ccms.tradecenter.controller.vo;

/**
* @Description: TODO 评价事务-自动评价设置
* @author fanhong.meng
* @date 2013-7-12 下午4:58:46 
*/
public class TraderateAutoSetRequest {
	
	private String dpId;
	/** 评价方式*/
	private String type;
	private String content;
	/** 是否开启自动评价*/
	private Integer status;
	
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
