package com.yunat.ccms.rule.center.conf.condition;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.yunat.ccms.rule.center.conf.BaseVO;

/**
 * 条件的指标
 * 
 * @author wenjian.liang
 */
public class ConditionProperty extends BaseVO {

	private static final long serialVersionUID = 222879912950139936L;

	private String name;
	private String type;
	private Collection<? extends Object> supportOps;
	private final Collection<ProvidedValues> providedValues = new LinkedHashSet<ProvidedValues>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public Collection<? extends Object> getSupportOps() {
		return supportOps;
	}

	public void setSupportOps(final Collection<? extends Object> supportOps) {
		this.supportOps = supportOps;
	}

	/**
	 * 可选值列表.如为空,则是一个输入值而非选择值.
	 * 
	 * @return
	 */
	public Collection<ProvidedValues> getProvidedValues() {
		return providedValues;
	}

	public void setProvidedValues(final Collection<ProvidedValues> providedValues) {
		this.providedValues.addAll(providedValues);
	}

}
