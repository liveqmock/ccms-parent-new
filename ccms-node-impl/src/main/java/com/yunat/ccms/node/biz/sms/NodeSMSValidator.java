package com.yunat.ccms.node.biz.sms;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.NodeValidationContext;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;

@Component
@Scope("prototype")
public class NodeSMSValidator implements NodeValidator<NodeSMS> {

	@Override
	public ValidateMessage validate(NodeSMS nodeEntity, NodeValidationContext validateContext) {
		Long nodeId = validateContext.getNodeId();
		if (nodeEntity == null) {
			return ValidateMessage.forNodeError("短信节点未配置", nodeId);
		}
		return null;
	}
}