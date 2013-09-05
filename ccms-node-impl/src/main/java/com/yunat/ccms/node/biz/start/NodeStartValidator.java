package com.yunat.ccms.node.biz.start;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.NodeValidationContext;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;

/**
 * 开始节点
 * 由于开始节点的pojo类不存在，只能用Object代替
 * 
 * @author liujingyu
 * 
 */
@Component
@Scope("prototype")
public class NodeStartValidator implements NodeValidator<NodeStart> {

	@Override
	public ValidateMessage validate(NodeStart nodeEntity, NodeValidationContext validateContext) {
		return null;
	}

}
