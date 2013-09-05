package com.yunat.ccms.core.support.statemachine;


/**
 * 定义一种状态（对应状态图中的状态）
 * @author xiaojing.qu
 * 
 */
public interface State {
	
	/**
	 * @return 本状态的名字
	 */
	public String getId();
	
	
}
