package com.yunat.ccms.node.biz.query;

import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.NodeValidationContext;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;

@Component
public class NodeQueryValidator implements NodeValidator<NodeQuery> {

	@Override
	public ValidateMessage validate(NodeQuery nodeQuery, NodeValidationContext validateContext) {
		Long nodeId = validateContext.getNodeId();
		// 没有节点数据
		if (nodeQuery == null) {
			return ValidateMessage.forNodeError("查询节点未配置", nodeId);
		}
		// 没有配置是否排除和时间类型
		if (nodeQuery.getIsExclude() == null || nodeQuery.getTimeType() == null) {
			return ValidateMessage.forNodeError("查询节点未配置", nodeId);
		}
		// 没有具体的查询筛选配置
		if (nodeQuery.getQueryDefineds() == null || nodeQuery.getQueryDefineds().size() <= 0) {
			return ValidateMessage.forNodeError("查询节点未配置", nodeId);
		}
		return null;
	}

}
