package com.yunat.ccms.node.biz.evaluate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NodeEvaluateQueryImpl implements NodeEvaluateQuery {

	@Autowired
	private NodeEvaluateRepository repository;

	@Override
	public NodeEvaluate findByNodeId(Long nodeId) {
		return repository.findByNodeId(nodeId);
	}

}
