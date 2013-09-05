package com.yunat.ccms.rule.center.conf.condition;

/**
 * 条件的指标的可选值
 * 
 * @author wenjian.liang
 */
public class ProvidedValues {

	private Long id;
	private String value;
	private String name;

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
}