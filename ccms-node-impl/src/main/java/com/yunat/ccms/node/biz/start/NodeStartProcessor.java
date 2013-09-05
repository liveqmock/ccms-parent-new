package com.yunat.ccms.node.biz.start;

import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.AbstractNodeProcessor;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessingException;

/**
 * 空实现
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class NodeStartProcessor extends AbstractNodeProcessor<NodeStart> {

	@Override
	public NodeOutput process(NodeStart nodeStart, NodeProcessingContext context) throws NodeProcessingException {
		return null;
	}

}
