package com.yunat.ccms.node.biz.sms;


public interface NodeSMSQuery {

	NodeSMS findByNodeId(Long nodeId);
	
	ExecutionRecord findByNodeIdAndSubjobId(Long nodeId, Long subjobId);

}