package com.yunat.ccms.workflow.service.query.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.repository.ConnectRepository;
import com.yunat.ccms.workflow.repository.NodeRepository;
import com.yunat.ccms.workflow.service.query.WorkFlowQuery;
import com.yunat.ccms.workflow.vo.MxConnect;
import com.yunat.ccms.workflow.vo.MxGraph;
import com.yunat.ccms.workflow.vo.MxNode;

@Service
public class WorkFlowQueryImpl implements WorkFlowQuery {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkFlowQueryImpl.class);

	@Autowired
	private NodeRepository nodeRepository;

	@Autowired
	private ConnectRepository connectRepository;

	@Override
	public MxGraph findMxGraphById(Long workflowId) {
		MxGraph mxGraph = new MxGraph();
		List<MxNode> mxNode = Lists.newArrayList();
		List<Node> nodes = nodeRepository.findByWorkflowId(workflowId);
		for (Node node : nodes) {
			MxNode mn = new MxNode();
			BeanUtils.copyProperties(node, mn);
			mxNode.add(mn);
		}
		mxGraph.setNodes(mxNode);

		List<MxConnect> mxConnect = Lists.newArrayList();
		List<Connect> conncets = connectRepository.findByWorkflowId(workflowId);
		for (Connect connect : conncets) {
			MxConnect mc = new MxConnect();
			BeanUtils.copyProperties(connect, mc);
			mxConnect.add(mc);
		}
		mxGraph.setConnects(mxConnect);

		return mxGraph;
	}

}
