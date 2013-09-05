package com.yunat.ccms.node.support.io;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.MapUtils;

import com.google.common.collect.Lists;
import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeOutput;

/**
 * 该节点输出，对于不同的后续节点输出不同的数据 例如：拆分节点
 * 
 * @author xiaojing.qu
 * 
 */
public class RespectiveNodeOutput implements NodeOutput {

	private final Long nodeId;
	private final Map<Long, NodeData> idDataMap;
	private final String outputMessage;

	/**
	 * 区分目标节点的节点输出(无outputMsg显示)
	 * 
	 * @param nodeId
	 *            当前输出数据的节点Id
	 * @param map
	 *            输出的数据 与 目标节点Id的映射
	 */
	public RespectiveNodeOutput(Long nodeId, Map<Long, NodeData> idDataMap) {
		this.nodeId = nodeId;
		this.idDataMap = MapUtils.unmodifiableMap(idDataMap);
		this.outputMessage = null;

	}

	/**
	 * 区分目标节点的节点输出(有outputMsg显示)
	 * 
	 * @param nodeId
	 *            当前输出数据的节点Id
	 * @param map
	 *            输出的数据 与 目标节点Id的映射
	 * @param outputMessage
	 *            节点上显示的信息,如果该节点不需要显示可为null
	 */
	public RespectiveNodeOutput(Long nodeId, Map<Long, NodeData> idDataMap, String outputMessage) {
		this.nodeId = nodeId;
		this.idDataMap = MapUtils.unmodifiableMap(idDataMap);
		this.outputMessage = outputMessage;

	}

	@Override
	public Long getNodeId() {
		return nodeId;
	}

	@Override
	public List<Long> getOutputNodes() {
		return Lists.newArrayList(idDataMap.keySet());
	}

	@Override
	public NodeData getOutputData(Long outputNodeId) {
		return idDataMap.get(outputNodeId);
	}

	@Override
	public String getOutputMsg() {
		return outputMessage;
	}

}
