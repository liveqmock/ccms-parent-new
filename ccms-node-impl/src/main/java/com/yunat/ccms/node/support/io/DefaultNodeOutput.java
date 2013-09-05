package com.yunat.ccms.node.support.io;

import java.util.List;

import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeOutput;

/**
 * 该节点输出作为后面的节点的公共输入，即给后面每个节点的数据是相同的
 * 
 * @author xiaojing.qu
 * 
 */
public class DefaultNodeOutput implements NodeOutput {

	private final Long nodeId;
	private final NodeData sharedNodeData;
	private final String outputMessage;

	/**
	 * 默认节点输出(无outputMsg显示)
	 * 
	 * @param nodeId
	 *            当前输出数据的节点Id
	 * @param sharedNodeData
	 *            当前节点输出的数据
	 */
	public DefaultNodeOutput(Long nodeId, NodeData sharedNodeData) {
		this.nodeId = nodeId;
		this.sharedNodeData = sharedNodeData;
		this.outputMessage = null;
	}

	/**
	 * 默认节点输出(有outputMsg显示)
	 * 
	 * @param nodeId
	 *            当前输出数据的节点Id
	 * @param sharedNodeData
	 *            当前节点输出的数据
	 * @param outputMessage
	 *            节点上显示的信息
	 */
	public DefaultNodeOutput(Long nodeId, NodeData sharedNodeData, String outputMessage) {
		this.nodeId = nodeId;
		this.sharedNodeData = sharedNodeData;
		this.outputMessage = outputMessage;
	}

	@Override
	public Long getNodeId() {
		return nodeId;
	}

	@Override
	public List<Long> getOutputNodes() {
		// should not care about who is his next node
		return null;
	}

	@Override
	public NodeData getOutputData(Long outputNodeId) {
		return sharedNodeData;
	}

	@Override
	public String getOutputMsg() {
		return outputMessage;
	}

}