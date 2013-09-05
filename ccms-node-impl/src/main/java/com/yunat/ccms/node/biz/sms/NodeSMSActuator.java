package com.yunat.ccms.node.biz.sms;

import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.support.channel.AbstractChannelBusiness;

public interface NodeSMSActuator extends AbstractChannelBusiness<NodeSMS> {
	String buildOutputMessage(String schemaName);

	String rebuildOutputSchemaName(NodeProcessingContext context, String schemaName);

}
