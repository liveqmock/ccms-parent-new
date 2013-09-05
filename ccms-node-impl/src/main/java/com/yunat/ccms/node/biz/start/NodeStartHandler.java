package com.yunat.ccms.node.biz.start;

import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;

@Component
public class NodeStartHandler implements NodeCloneHandler {

	@Override
	public void clone(Long nodeId, Long newNodeId) {}

	@Override
	public void delete(Long nodeId) {}

	@Override
	public boolean updatable() {
		return false;
	}

	@Override
	public void refresh(Long nodeId, Long newNodeId) {
		throw new CcmsBusinessException("the updateable is false, will invoke this function is error .");
	}

}
