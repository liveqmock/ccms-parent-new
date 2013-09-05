package com.yunat.ccms.node.biz.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.channel.support.DeliveryCategory;
import com.yunat.ccms.configuration.variable.SystemVariable;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.support.channel.AbstractChannelProcessor;

@Component
public class NodeSMSProcessor extends AbstractChannelProcessor<NodeSMS> {

	private static Logger logger = LoggerFactory.getLogger(NodeSMSProcessor.class);

	@Autowired
	private SystemVariable applicationVariable;

	protected NodeSMSActuator getActuatorInstance(NodeSMS nodeSMS) {
		NodeSMSActuator actuator = null;
		if (DeliveryCategory.PERSON.getCode().equals(nodeSMS.getDeliveryCategory())) {
			actuator = applicationVariable.getApplicationContext().getBean(DeliveryPersonActuator.class);
		} else if (DeliveryCategory.ORDER.getCode().equals(nodeSMS.getDeliveryCategory())) {
			actuator = applicationVariable.getApplicationContext().getBean(DeliveryOrderActuator.class);
		}
		logger.info("{}", actuator);
		return actuator;
	}

	@Override
	public void generateChannelUserRecord(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		getActuatorInstance(nodeConfig).generateChannelUserRecord(nodeConfig, context, schemaName);
	}

	@Override
	public void refreshExecutionRecord(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		getActuatorInstance(nodeConfig).refreshExecutionRecord(nodeConfig, context, schemaName);
	}

	@Override
	public String builderSQL(NodeSMS nodeConfig, NodeProcessingContext context) {
		return getActuatorInstance(nodeConfig).builderSQL(nodeConfig, context);
	}

	@Override
	public void executeDelivery(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		getActuatorInstance(nodeConfig).executeDelivery(nodeConfig, context, schemaName);
	}

	@Override
	public String buildOutputMessage(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		return getActuatorInstance(nodeConfig).buildOutputMessage(schemaName);
	}

	@Override
	public String rebuildOutputSchemaName(NodeSMS nodeConfig, NodeProcessingContext context, String schemaName) {
		return getActuatorInstance(nodeConfig).rebuildOutputSchemaName(context, schemaName);
	}

}