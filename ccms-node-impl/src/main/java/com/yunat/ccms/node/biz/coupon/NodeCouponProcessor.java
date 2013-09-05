package com.yunat.ccms.node.biz.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.configuration.variable.SystemVariable;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.support.channel.AbstractChannelProcessor;

@Component
public class NodeCouponProcessor extends AbstractChannelProcessor<NodeCoupon> {

	@Autowired
	private SystemVariable applicationVariable;

	@Autowired
	protected NodeCouponActuator nodeCouponActuator;

	@Override
	public void generateChannelUserRecord(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		nodeCouponActuator.generateChannelUserRecord(nodeConfig, context, schemaName);
	}

	@Override
	public void refreshExecutionRecord(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		nodeCouponActuator.refreshExecutionRecord(nodeConfig, context, schemaName);
	}

	@Override
	public String builderSQL(NodeCoupon nodeConfig, NodeProcessingContext context) {
		return nodeCouponActuator.builderSQL(nodeConfig, context);
	}

	@Override
	public void executeDelivery(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		nodeCouponActuator.executeDelivery(nodeConfig, context, schemaName);
	}

	@Override
	protected String buildOutputMessage(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		return nodeCouponActuator.buildOutputMessage(schemaName);
	}

	@Override
	protected String rebuildOutputSchemaName(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName) {
		return nodeCouponActuator.rebuildOutputSchemaName(nodeConfig, context, schemaName);
	}

}
