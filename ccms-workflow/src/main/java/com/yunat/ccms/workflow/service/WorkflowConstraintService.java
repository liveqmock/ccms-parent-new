package com.yunat.ccms.workflow.service;

import java.util.Map;

import com.yunat.ccms.workflow.validation.NodeConnectConstraint;
import com.yunat.ccms.workflow.validation.NodeConstraint;

public interface WorkflowConstraintService {
	
	public Map<String, NodeConstraint> getNodeConstraintMap();

	public Map<String, NodeConnectConstraint> getNodeConnectConstraintMap();
}
