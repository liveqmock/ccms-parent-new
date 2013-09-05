package com.yunat.ccms.workflow.service.command;

import com.yunat.ccms.workflow.domain.WorkFlow;
import com.yunat.ccms.workflow.vo.MxGraph;


public interface WorkFlowCommand {
	WorkFlow createWorkflow(MxGraph mxGraph);

	void updateWorkflow(MxGraph mxGraph, Long workflowId);

	WorkFlow clone(Long workflowId);
}
