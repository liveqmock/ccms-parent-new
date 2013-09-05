package com.yunat.ccms.node.biz.wait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeWaitQueryImpl implements NodeWaitQuery {

	@Autowired
	private NodeWaitRepository repository;

	@Override
	public NodeWait findByNodeId(Long nodeId) {
		return repository.findByNodeId(nodeId);
	}

}