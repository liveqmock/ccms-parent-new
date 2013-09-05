package com.yunat.ccms.node.biz.sms;

public interface NodeSMSCommand {

	void saveOrUpdate(NodeSMS newNode);

	void deleteByNodeId(Long nodeId);

	void saveExecutionRecord(ExecutionRecord executionRecord);

}