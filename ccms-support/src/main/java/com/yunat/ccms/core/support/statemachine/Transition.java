package com.yunat.ccms.core.support.statemachine;


/**
 * 定义一种状态的转变（即状态和状态之间的变化动作）
 * @author xiaojing.qu
 * 
 */
public  interface  Transition{
	
	
	public String getId();
	
	/**
	 * @return newState 如果该转换能够作用于该状态则返回转换后的状态，否者抛出异常
	 * 20130312这个地方有问题，导致maven编译不了，把泛型去掉，将方法移到子类中
	 */
	//public abstract T  afterTransition(T oldState);

}
