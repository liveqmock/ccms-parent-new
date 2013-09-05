package com.yunat.ccms.rule.center.engine;

import java.io.Serializable;

/**
 * 
 * 可被RuleEngine操作的Fact
 * 
 * @author xiaojing.qu
 * 
 */
public abstract class Fact<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private ID factId;
	private FactResult result = new FactResult();;

	/**
	 * 在drl中引用，用来设置结果
	 * 
	 * @param planId
	 * @param ruleId
	 */
	public void hit(long planId, long ruleId) {
		result.hit(planId, ruleId);
	}

	public ID getFactId() {
		return factId;
	}

	/**
	 * 规则引擎处理Fact后得到的结果
	 * 
	 * @return
	 */
	public FactResult getResult() {
		return result;
	}

}
