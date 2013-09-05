package com.yunat.ccms.node.biz.wait;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.NodeValidationContext;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;

@Component
@Scope("prototype")
public class NodeWaitValidator implements NodeValidator<NodeWait> {

	@Autowired
	private NodeWaitQuery nodeWaitQuery;

	@Override
	public ValidateMessage validate(NodeWait nodeEntity, NodeValidationContext validateContext) {
		if (nodeEntity == null) {
			return ValidateMessage.forNodeError("等待节点未设置", validateContext.getNodeId());
		}
		// TODO
		return null;
	}

}
