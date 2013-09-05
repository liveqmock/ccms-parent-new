package com.yunat.ccms.node.biz.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.NodeValidationContext;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;

@Component
@Scope("prototype")
public class TimeValidator implements NodeValidator<NodeTime> {

	@Autowired
	private NodeTimeQuery nodeTimeQuery;

	@Override
	public ValidateMessage validate(NodeTime timeNode, NodeValidationContext validateContext) {
		if (timeNode == null) {
			return ValidateMessage.forNodeError("时间节点未设置", validateContext.getNodeId());
		}
		return null;
	}

}