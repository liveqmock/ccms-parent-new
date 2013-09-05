package com.yunat.ccms.node.biz.sms;

import java.util.List;

import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.NodeProcessingContext;

@Component
public class DeliveryOrderActuator extends AbstractNodeSMSActuator {

	@Override
	public void generateChannelUserRecord(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshExecutionRecord(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String buildOutputMessage(String schemaName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String rebuildOutputSchemaName(NodeProcessingContext context, String schemaName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String builderSQL(NodeSMS nodeConfig, NodeProcessingContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildDeliveryDetailList(NodeSMS nodeSMS, NodeProcessingContext context, String deliveryTableName,
			List<Long> markIdAttributeList, String messageSubstitute, String testDomainMessageSubstitute) {
		// TODO Auto-generated method stub
		
	}
	
}