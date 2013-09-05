package com.yunat.ccms.node.spi.support;

public interface NodeCloneHandler {

	/**
	 * 复制节点配置
	 * clone & copy node attribute persistence
	 * 
	 * @param nodeId
	 * @param newNodeId
	 */
	public void clone(Long nodeId, Long newNodeId);

	/**
	 * 删除节点配置
	 * delete workflow has delete all nodes
	 * 
	 * @param nodeId
	 */
	public void delete(Long nodeId);

	/**
	 * may be execute #{@link #refresh(Long, Long)}
	 * 
	 * @return
	 */
	public boolean updatable();

	/**
	 * clone node after refresh
	 * 
	 * @param nodeId
	 * @param newNodeId
	 */
	public void refresh(Long nodeId, Long newNodeId);

}
