package com.yunat.ccms.workflow.service.query;

import java.util.List;

import com.yunat.ccms.workflow.domain.Connect;

public interface ConnectQuery {
	Connect findById(Long connectId);
	
	List<Connect> findByWorkflowId(Long workflowId);
	
	List<Connect> findByTargetNodeId(Long targetNodeId);
}
