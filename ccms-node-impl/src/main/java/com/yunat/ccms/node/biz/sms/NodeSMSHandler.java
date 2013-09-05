package com.yunat.ccms.node.biz.sms;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;

@Component
public class NodeSMSHandler implements NodeCloneHandler {

	@Autowired
	private NodeSMSQuery nodeSMSQuery;

	@Autowired
	private NodeSMSCommand nodeSMSCommand;

	@Override
	public void clone(Long nodeId, Long newNodeId) {
		NodeSMS node = nodeSMSQuery.findByNodeId(nodeId);
		if (null != node) {
			NodeSMS newNode = new NodeSMS();
			BeanUtils.copyProperties(node, newNode);
			newNode.setNodeId(newNodeId);
			nodeSMSCommand.saveOrUpdate(newNode);
		}
	}

	@Override
	public void delete(Long nodeId) {
		nodeSMSCommand.deleteByNodeId(nodeId);
	}

	@Override
	public boolean updatable() {
		return false;
	}

	@Override
	public void refresh(Long nodeId, Long newNodeId) {
		throw new CcmsBusinessException("the updateable is false, will invoke this function is error .");
	}

}