package com.yunat.ccms.metadata.face;

import java.util.ArrayList;
import java.util.List;

public class FaceNode {

	private String exclude;
	private String timeType;
	private String plat;
	private List<FaceQuery> queries = new ArrayList<FaceQuery>();
	private List<String> delqueries = new ArrayList<String>();

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getPlat() {
		return plat;
	}

	public void setPlat(String plat) {
		this.plat = plat;
	}

	public List<FaceQuery> getQueries() {
		return queries;
	}

	public void setQueries(List<FaceQuery> queries) {
		this.queries = queries;
	}

	public List<String> getDelqueries() {
		return delqueries;
	}

	public void setDelqueries(List<String> delqueries) {
		this.delqueries = delqueries;
	}
}
