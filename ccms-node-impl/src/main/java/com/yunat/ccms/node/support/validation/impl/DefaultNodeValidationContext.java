package com.yunat.ccms.node.support.validation.impl;

import java.util.Date;

import com.yunat.ccms.node.spi.support.NodeValidationContext;

public class DefaultNodeValidationContext implements NodeValidationContext {

	private final Long nodeId;
	private final Date referenceTime;

	public DefaultNodeValidationContext(Long nodeId, Date referenceTime) {
		this.nodeId = nodeId;
		this.referenceTime = referenceTime;
	}

	@Override
	public Long getNodeId() {
		return nodeId;
	}

	@Override
	public Date getReferenceTime() {
		return referenceTime;
	}

}
