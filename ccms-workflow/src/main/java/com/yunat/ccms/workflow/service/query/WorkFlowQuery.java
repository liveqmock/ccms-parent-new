package com.yunat.ccms.workflow.service.query;

import com.yunat.ccms.workflow.vo.MxGraph;

public interface WorkFlowQuery {

	MxGraph findMxGraphById(Long workflowId);
}
