package com.yunat.ccms.rule.center.drl;

/**
 * DRL的RHS部分
 * RHSExpressionBuilder,输入一个对象，活动相应对象的LHS片段
 * 
 * @author tao.yang
 * 
 */

public interface RHSFragmentBuilder<T> {
	public RHSFragment toRHS(T obj);
}
