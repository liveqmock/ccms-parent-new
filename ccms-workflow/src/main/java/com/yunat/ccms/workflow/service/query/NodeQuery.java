package com.yunat.ccms.workflow.service.query;

import java.util.List;

import com.yunat.ccms.workflow.domain.Node;

public interface NodeQuery {
	Node findById(Long nodeId);
	
	List<Node> findByWorkflowId(Long workflowId);
	
	List<Node> findNextNodesById(Long nodeId);
	
	List<Node> findNextNodesByIdAndTypeIn(Long nodeId, String[] nodeTypes);
	
	List<Node> findPreviousNodeById(Long nodeId);
	
	List<Node> findPreviousNodeByIdAndTypeIn(Long nodeId, String[] nodeTypes);
}
