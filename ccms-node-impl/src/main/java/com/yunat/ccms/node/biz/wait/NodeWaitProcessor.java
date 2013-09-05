package com.yunat.ccms.node.biz.wait;

import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.AbstractNodeProcessor;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessingException;

@Component
public class NodeWaitProcessor extends AbstractNodeProcessor<NodeWait> {

	@Override
	public NodeOutput process(NodeWait nodeWait, NodeProcessingContext context) throws NodeProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

}
