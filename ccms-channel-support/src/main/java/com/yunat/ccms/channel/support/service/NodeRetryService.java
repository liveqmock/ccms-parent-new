package com.yunat.ccms.channel.support.service;

import com.yunat.ccms.channel.support.domain.NodeRetry;

public interface NodeRetryService {

	public void saveNodeRetry(NodeRetry nodeRetry);

	public NodeRetry getNodeRetry(Long jobId, Long nodeId, boolean isTestExecute);
}
