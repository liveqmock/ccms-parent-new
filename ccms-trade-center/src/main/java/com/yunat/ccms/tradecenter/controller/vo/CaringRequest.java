package com.yunat.ccms.tradecenter.controller.vo;

/**
 * 关怀请求类，单个、批量
 * @author Administrator
 *
 */
public class CaringRequest {

	private String tid;
	private String oid;
	private String dpId;
	private String customerno;
	/** 关怀类型 ：短信，旺旺，电话 */
	private String caringType;
	private long gatewayId;
	private String content;
	/** 操作人 */
	private String caringperson;
	/** 是否过滤黑名单 */
	private boolean filterBlacklist;

	private String[] tids;

	private String[] oids;

	public String[] getOids() {
		return oids;
	}

	public void setOids(String[] oids) {
		this.oids = oids;
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

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

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

	public String getCaringType() {
		return caringType;
	}

	public void setCaringType(String caringType) {
		this.caringType = caringType;
	}

	public long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCaringperson() {
		return caringperson;
	}

	public void setCaringperson(String caringperson) {
		this.caringperson = caringperson;
	}

	public boolean isFilterBlacklist() {
		return filterBlacklist;
	}

	public void setFilterBlacklist(boolean filterBlacklist) {
		this.filterBlacklist = filterBlacklist;
	}

}
