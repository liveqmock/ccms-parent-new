package com.yunat.ccms.node.spi.repository;

import com.yunat.ccms.node.spi.support.NodeCloneHandler;

public interface NodeCloneHandlerRepository {

	public NodeCloneHandler getNodeCloneHandler(String nodeType);

}
