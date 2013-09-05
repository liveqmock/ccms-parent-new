package com.yunat.ccms.rule.center.drl;


/**
 * DRL的LHS部分
 * LHSExpressionBuilder,输入一个对象，活动相应对象的LHS片段
 * 
 * @author xiaojing.qu
 * 
 */
public interface LHSFragmentBuilder<T> {

	public LHSFragment toLHS(T obj);

}
