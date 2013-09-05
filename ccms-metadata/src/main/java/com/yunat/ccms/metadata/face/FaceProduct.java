package com.yunat.ccms.metadata.face;

import java.util.ArrayList;
import java.util.List;

public class FaceProduct {

	private List<String> ids = new ArrayList<String>();
	private List<FaceProductKeyword> keys = new ArrayList<FaceProductKeyword>();

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public List<FaceProductKeyword> getKeys() {
		return keys;
	}

	public void setKeys(List<FaceProductKeyword> keys) {
		this.keys = keys;
	}

}
