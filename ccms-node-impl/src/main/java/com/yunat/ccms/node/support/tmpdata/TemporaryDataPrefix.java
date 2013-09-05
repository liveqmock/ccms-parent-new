package com.yunat.ccms.node.support.tmpdata;

import com.yunat.ccms.core.support.annotation.ResultType;

public enum TemporaryDataPrefix {
	TABLE_PREFIX("tmp_log_node"), 
	VIEW_PREFIX("vtmp_log_node"),
	NULL_PREFIX("");
	
	private String name;
	private TemporaryDataPrefix(String _name) {
		this.name = _name;
	}
	
	public static String getTemporaryDataName(ResultType resultType) {
		if (resultType.equals(ResultType.TABLE)) {
			return TABLE_PREFIX.getName();
		} else if (resultType.equals(ResultType.VIEW)) {
			return VIEW_PREFIX.getName();
		}
		return NULL_PREFIX.getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}