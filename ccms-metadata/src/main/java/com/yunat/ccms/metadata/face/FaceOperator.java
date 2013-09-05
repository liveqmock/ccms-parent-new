package com.yunat.ccms.metadata.face;

import org.apache.commons.lang3.EnumUtils;

import com.yunat.ccms.metadata.metamodel.MetaOperator;

/**
 * 接口对象：界面的操作符
 * 
 * @author kevin.jiang 2013-3-16
 */
public class FaceOperator {

	private String value;
	private String name;

	public FaceOperator() {

	}

	public FaceOperator(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaOperator toMetaOperator() {
		return EnumUtils.isValidEnum(MetaOperator.class, value) ? MetaOperator.valueOf(value) : null;
	}

}
