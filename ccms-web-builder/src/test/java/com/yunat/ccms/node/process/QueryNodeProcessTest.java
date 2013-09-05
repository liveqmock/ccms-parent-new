package com.yunat.ccms.node.process;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.node.biz.query.NodeQuery;
import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeInput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessor;
import com.yunat.ccms.node.support.io.DefaultNodeInput;
import com.yunat.ccms.node.support.io.Table;
import com.yunat.ccms.schedule.core.impl.DefaultNodeProcessingContext;

@Service
public class QueryNodeProcessTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	NodeProcessor<NodeQuery> queryProcessor;

	/**
	 * 测试：【正式执行】属性查询
	 */
	@Test
	public void testNormalPropertyQueryTaskProcess() {

		NodeQuery nodeQuery = new NodeQuery();

		Long subjobId = 101L;
		Long jobId = 101L;
		Long nodeId = 1039L;
		Long campaingId = 101L;
		boolean isTest = false;
		Long preNodeId = 99L;
		NodeData data = new Table("uni_customer");
		NodeInput input = new DefaultNodeInput(nodeId, preNodeId, data);
		NodeProcessingContext context = new DefaultNodeProcessingContext(subjobId, jobId, nodeId, campaingId, isTest,
				input);
		queryProcessor.process(nodeQuery, context);
	}

	/**
	 * 测试：【测试执行】属性查询
	 */
	@Test
	public void testTestPropertyQueryTaskProcess() {

		NodeQuery nodeQuery = new NodeQuery();

		Long subjobId = 100L;
		Long jobId = 100L;
		Long nodeId = 555L;
		Long campaingId = 100L;
		boolean isTest = true;
		Long preNodeId = 99L;
		NodeData data = new Table("uni_customer");
		NodeInput input = new DefaultNodeInput(nodeId, preNodeId, data);
		NodeProcessingContext context = new DefaultNodeProcessingContext(subjobId, jobId, nodeId, campaingId, isTest,
				input);
		queryProcessor.process(nodeQuery, context);
	}
}
