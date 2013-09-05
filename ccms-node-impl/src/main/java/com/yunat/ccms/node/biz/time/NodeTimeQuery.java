package com.yunat.ccms.node.biz.time;

public interface NodeTimeQuery {

	NodeTime findByNodeId(Long nodeId);

	NodeTime findTimeNodeByWorkflowId(Long workflowId);
}
