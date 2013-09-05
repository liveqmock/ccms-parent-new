package com.yunat.ccms.metadata.face;
/**
 * 接口对象：目标值的显示类型（可以是一个具体值，一个范围，或者一组离散值）
 * @author kevin.jiang
 * 2013-3-16
 */
public class FaceValueType {

	private String key;
	private String name;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
