package com.yunat.ccms.node.spi;

/**
 * 节点执行上下文接口，提供节点执行必须的数据获取
 * 
 */
public interface NodeProcessingContext {

	/**
	 * 当前子任务ID
	 * 
	 * @return not null
	 */
	public abstract Long getSubjobId();

	/**
	 * 当前任务ID
	 * 
	 * @return not null
	 */
	public abstract Long getJobId();

	/**
	 * 当前节点ID
	 * 
	 * @return not null
	 */
	public abstract Long getNodeId();

	/**
	 * 当前活动ID
	 * 
	 * @return not null
	 */
	public abstract Long getCampId();

	/**
	 * 节点是否测试执行
	 * 
	 * @return not null
	 */
	public abstract boolean isTestExecute();

	/**
	 * 获取节点输入
	 * 
	 * @return not null
	 */
	public abstract NodeInput getNodeInput();

}
