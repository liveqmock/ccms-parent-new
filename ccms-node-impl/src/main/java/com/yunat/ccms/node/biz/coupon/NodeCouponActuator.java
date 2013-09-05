package com.yunat.ccms.node.biz.coupon;

import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.support.channel.AbstractChannelBusiness;

public interface NodeCouponActuator extends AbstractChannelBusiness<NodeCoupon> {
	
	String buildOutputMessage(String schemaName);

	String rebuildOutputSchemaName(NodeCoupon nodeConfig, NodeProcessingContext context, String schemaName);

}
