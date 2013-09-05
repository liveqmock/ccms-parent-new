package com.yunat.ccms.node.spi.repository;

import com.yunat.ccms.node.spi.NodeProcessor;

public interface NodeProcessorRepository {

	public NodeProcessor getNodeProcessor(String nodeType);

}
