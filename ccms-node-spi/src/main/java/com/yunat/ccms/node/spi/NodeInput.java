package com.yunat.ccms.node.spi;

import java.util.List;

/**
 * 
 * @author xiaojing.qu
 * 
 */
public interface NodeInput {

	/**
	 * 当前节点ID
	 * 
	 * @return
	 */
	public Long getNodeId();

	/**
	 * 给当前节点提此输入的前面节点ID
	 * 
	 * @return
	 */
	public List<Long> getInputNodes();

	/**
	 * 根据前面节点的ID获得前面节点向当前节点输入的数据
	 * 
	 * @return
	 */
	public NodeData getInputData(Long inputNodeId);

}
