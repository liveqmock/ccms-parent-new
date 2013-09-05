package com.yunat.ccms.node.biz.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NodeTimeQueryImpl implements NodeTimeQuery {

	@Autowired
	private NodeTimeRepository repository;

	@Override
	public NodeTime findByNodeId(Long nodeId) {
		return repository.findByNodeId(nodeId);
	}

	@Override
	public NodeTime findTimeNodeByWorkflowId(Long workflowId) {
		return repository.findTimeNodeByWorkflowId(workflowId);
	}

}
