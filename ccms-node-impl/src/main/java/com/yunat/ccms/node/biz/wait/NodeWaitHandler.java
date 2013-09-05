package com.yunat.ccms.node.biz.wait;

import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;

@Component
public class NodeWaitHandler implements NodeCloneHandler {

	@Override
	public void clone(Long nodeId, Long newNodeId) {
		// 开发等待节点时，需要实现
	}

	@Override
	public void delete(Long nodeId) {
		// 开发等待节点时，需要实现
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