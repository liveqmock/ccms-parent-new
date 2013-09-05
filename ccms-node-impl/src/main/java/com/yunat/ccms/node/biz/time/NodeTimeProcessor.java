package com.yunat.ccms.node.biz.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.AbstractNodeProcessor;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessingException;
import com.yunat.ccms.node.support.io.DefaultNodeOutput;
import com.yunat.ccms.node.support.io.View;
import com.yunat.ccms.node.support.tmpdata.TemporaryViewDecorator;

/**
 * 
 * 把所有用户输出视图,往后流转
 * 
 * @author yinwei
 * 
 */
@Component
public class NodeTimeProcessor extends AbstractNodeProcessor<NodeTime> {

	private static final String suffix = null;

	@Autowired
	TemporaryViewDecorator nodeTempViewDecorator;

	@Override
	public NodeOutput process(NodeTime nodeTime, NodeProcessingContext context) throws NodeProcessingException {
		long subjobId = context.getSubjobId();
		long jobId = context.getJobId();
		long nodeId = context.getNodeId();
		String sql = "select uni_id , -1 as control_group_type from uni_customer";
		String viewName = nodeTempViewDecorator.generate(jobId, subjobId, suffix, sql);
		return new DefaultNodeOutput(nodeId, new View(viewName));
	}

}