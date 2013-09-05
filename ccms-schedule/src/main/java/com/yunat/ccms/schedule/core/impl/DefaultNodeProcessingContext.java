package com.yunat.ccms.schedule.core.impl;

import com.yunat.ccms.node.spi.NodeInput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.schedule.domain.LogSubjob;

public class DefaultNodeProcessingContext implements NodeProcessingContext {

	private final Long subjobId;
	private final Long jobId;
	private final Long nodeId;
	private final Long campaingId;
	private final boolean isTest;
	private final NodeInput nodeInput;

	public DefaultNodeProcessingContext(LogSubjob subjob, NodeInput nodeInput) {
		this.subjobId = subjob.getSubjobId();
		this.jobId = subjob.getJobId();
		this.nodeId = subjob.getNode().getId();
		this.campaingId = subjob.getCampId();
		this.isTest = subjob.isTest();
		this.nodeInput = nodeInput;
	}

	public DefaultNodeProcessingContext(Long subjobId, Long jobId, Long nodeId, Long campaingId, boolean isTest,
			NodeInput nodeInput) {
		this.subjobId = subjobId;
		this.jobId = jobId;
		this.nodeId = nodeId;
		this.campaingId = campaingId;
		this.isTest = isTest;
		this.nodeInput = nodeInput;
	}

	@Override
	public Long getSubjobId() {
		return subjobId;
	}

	@Override
	public Long getJobId() {
		return jobId;
	}

	@Override
	public Long getNodeId() {
		return nodeId;
	}

	@Override
	public Long getCampId() {
		return campaingId;
	}

	@Override
	public boolean isTestExecute() {
		return isTest;
	}

	@Override
	public NodeInput getNodeInput() {
		return nodeInput;
	}

}
