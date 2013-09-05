package com.yunat.ccms.node.biz.time;

public interface NodeTimeRepository {

	NodeTime findByNodeId(Long nodeId);

	NodeTime findTimeNodeByWorkflowId(Long workflowId);

	void saveOrUpdate(NodeTime newTimeNode);

	void deleteById(Long nodeId);

}
