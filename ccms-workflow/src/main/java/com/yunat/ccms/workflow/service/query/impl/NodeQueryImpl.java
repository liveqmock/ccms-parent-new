package com.yunat.ccms.workflow.service.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.repository.NodeRepository;
import com.yunat.ccms.workflow.service.query.NodeQuery;

@Service
public class NodeQueryImpl implements NodeQuery {

	@Autowired
	private NodeRepository repository;
	
	@Override
	public Node findById(Long nodeId) {
		return repository.findById(nodeId);
	}

	@Override
	public List<Node> findByWorkflowId(Long workflowId) {
		return repository.findByWorkflowId(workflowId);
	}

	@Override
	public List<Node> findNextNodesById(Long nodeId) {
		return repository.findNextNodesByIdAndTypeIn(nodeId, null);
	}

	@Override
	public List<Node> findNextNodesByIdAndTypeIn(Long nodeId,
			String[] nodeTypes) {
		return repository.findNextNodesByIdAndTypeIn(nodeId, nodeTypes);
	}

	@Override
	public List<Node> findPreviousNodeById(Long nodeId) {
		return repository.findPreviousNodeByIdAndTypeIn(nodeId, null);
	}

	@Override
	public List<Node> findPreviousNodeByIdAndTypeIn(Long nodeId,
			String[] nodeTypes) {
		return repository.findPreviousNodeByIdAndTypeIn(nodeId, nodeTypes);
	}

}