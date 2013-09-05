package com.yunat.ccms.node.support.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.MapUtils;

import com.google.common.collect.Lists;
import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeInput;

public class DefaultNodeInput implements NodeInput{
	
	private final Long nodeId;
	private final Map<Long,NodeData> map;
	
	
	/**
	 * 唯一输入
	 * @param nodeId
	 * @param map
	 */
	public DefaultNodeInput(Long nodeId,Long uniquPreNodeId,NodeData data){
		this.nodeId = nodeId;
		Map<Long,NodeData> newMap = new HashMap<Long,NodeData>();
		newMap.put(uniquPreNodeId, data);
		this.map =  MapUtils.unmodifiableMap(newMap);
	}
	
	/**
	 * 多个输入
	 * @param nodeId
	 * @param map
	 */
	public DefaultNodeInput(Long nodeId,Map<Long,NodeData> map){
		this.nodeId = nodeId;
		this.map = MapUtils.unmodifiableMap(map);
	}

	@Override
	public Long getNodeId() {
		return nodeId;
	}

	@Override
	public List<Long> getInputNodes() {
		return Lists.newArrayList(map.keySet());
	}

	@Override
	public NodeData getInputData(Long inputNodeId) {
		return map.get(inputNodeId);
	}

}
