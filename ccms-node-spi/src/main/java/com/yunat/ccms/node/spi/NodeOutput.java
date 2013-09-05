package com.yunat.ccms.node.spi;

import java.util.List;

public interface NodeOutput {

	/**
	 * 当前节点ID
	 * 
	 * @return
	 */
	public Long getNodeId();

	/**
	 * 该输出数据的目标后续节点ID
	 * 
	 * @return
	 */
	public List<Long> getOutputNodes();

	/**
	 * 输出的数据
	 * 
	 * @return
	 */
	public NodeData getOutputData(Long outputNodeId);

	/**
	 * 节点上显示的信息（以前的OutputCount）， <br>
	 * 背景：匹配订单节点不仅要输出人数还要输出单数，因此考虑将接口提供出来让节点来实现
	 * 
	 * @return
	 */
	public String getOutputMsg();

}
