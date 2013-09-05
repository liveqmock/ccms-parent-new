package com.yunat.ccms.node.biz.time;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;

@Component
public class NodeTimeHandler implements NodeCloneHandler {

	@Autowired
	private NodeTimeQuery nodeTimeQuery;

	@Autowired
	private NodeTimeCommand nodeTimeCommand;

	@Override
	public boolean updatable() {
		return false;
	}

	@Override
	public void refresh(Long nodeId, Long newNodeId) {
		throw new CcmsBusinessException("the updateable is false, will invoke this function is error .");
	}

	@Override
	public void clone(Long nodeId, Long newNodeId) {
		NodeTime nodeTime = nodeTimeQuery.findByNodeId(nodeId);
		if (null != nodeTime) {
			NodeTime newTimeNode = new NodeTime();
			BeanUtils.copyProperties(nodeTime, newTimeNode);
			newTimeNode.setId(newNodeId);
			nodeTimeCommand.saveTimeNode(newTimeNode);
		}
	}

	@Override
	public void delete(Long nodeId) {
		nodeTimeCommand.deleteById(nodeId);
	}

}