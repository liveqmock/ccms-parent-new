package com.yunat.ccms.rule.center.conf.condition;

public class SearchProductParameters {
	private String q;
	private long pageNo;
	private long pageSize;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
}
