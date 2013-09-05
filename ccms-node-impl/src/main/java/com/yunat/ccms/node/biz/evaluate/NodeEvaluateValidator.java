package com.yunat.ccms.node.biz.evaluate;

import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.NodeValidationContext;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;

@Component
public class NodeEvaluateValidator implements NodeValidator<NodeEvaluate> {

	@Override
	public ValidateMessage validate(NodeEvaluate nodeEvaluate, NodeValidationContext validateContext) {
		Long nodeId = validateContext.getNodeId();
		// 没有节点数据
		if (nodeEvaluate == null) {
			return ValidateMessage.forNodeError("效果评估节点未配置", nodeId);
		}
		return null;
	}

}
