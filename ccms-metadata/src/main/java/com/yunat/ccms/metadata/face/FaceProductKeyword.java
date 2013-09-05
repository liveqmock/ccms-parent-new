package com.yunat.ccms.metadata.face;

import java.util.ArrayList;
import java.util.List;

public class FaceProductKeyword {

	private List<String> keywords = new ArrayList<String>();
	private String dpId;

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
}
