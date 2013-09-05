package com.yunat.ccms.node.spi.repository;

import com.yunat.ccms.node.spi.NodeEntity;

public interface NodeEntityRepository {

	NodeEntity getNodeEntity(String nodeType, Long nodeId);

}
