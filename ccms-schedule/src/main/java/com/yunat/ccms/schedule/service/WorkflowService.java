package com.yunat.ccms.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluate;
import com.yunat.ccms.node.biz.start.NodeStart;
import com.yunat.ccms.node.biz.time.NodeTime;
import com.yunat.ccms.node.biz.wait.NodeWait;
import com.yunat.ccms.schedule.repository.WorkflowDao;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.domain.WorkFlow;
import com.yunat.ccms.workflow.domain.WorkFlowGraph;

/**
 * 
 * @author xiaojing.qu
 * 
 */
@Component
@Scope("singleton")
public class WorkflowService {

	@Autowired
	private WorkflowDao workflowDao;

	public Node getNodeById(Long nodeId) {
		return workflowDao.getNode(nodeId);
	}

	public Node getStartNodeByCampId(Long campId) {
		return workflowDao.getNodeByCampIdAndType(campId, NodeStart.TYPE).get(0);
	}

	public Node getTimeNodeByCampId(Long campId) {
		return workflowDao.getNodeByCampIdAndType(campId, NodeTime.TYPE).get(0);
	}

	public WorkFlowGraph<Node, Connect> getWorkFlowGraph(Long campId) {
		WorkFlow workflow = workflowDao.getWorkflowByCampId(campId);
		return WorkFlowGraph.getWorkFlowGraph(workflow);
	}

	public NodeTime getNodeTime(Long nodeId) {
		return workflowDao.getTimeNodeByID(nodeId);
	}

	public NodeWait getNodeWait(Long nodeId) {
		return workflowDao.getWaitNodeByID(nodeId);
	}

	public NodeEvaluate getNodeEvaluate(Long nodeId) {
		return workflowDao.getEvaluateNodeByID(nodeId);
	}

}
