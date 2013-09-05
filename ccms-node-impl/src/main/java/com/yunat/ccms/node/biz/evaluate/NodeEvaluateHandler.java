package com.yunat.ccms.node.biz.evaluate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;

@Component
public class NodeEvaluateHandler implements NodeCloneHandler {

	@Autowired
	NodeEvaluateQuery nodeEvaluateQuery;

	@Autowired
	NodeEvaluateCommand nodeEvaluateCommand;

	@Override
	public void clone(Long nodeId, Long newNodeId) {
		NodeEvaluate nodeEvaluate = nodeEvaluateQuery.findByNodeId(nodeId);
		if (null != nodeEvaluate) {
			NodeEvaluate newNodeEvaluate = new NodeEvaluate();
			BeanUtils.copyProperties(nodeEvaluate, newNodeEvaluate);
			newNodeEvaluate.setNodeId(newNodeId);
			nodeEvaluateCommand.saveEvaluateNode(newNodeEvaluate);
		}

	}

	@Override
	public void delete(Long nodeId) {
		nodeEvaluateCommand.deleteById(nodeId);

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
