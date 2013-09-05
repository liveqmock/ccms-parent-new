package com.yunat.ccms.schedule.service;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.core.TaskCountDownLatchRepository;
import com.yunat.ccms.schedule.core.latch.FlowCountDownLatch;
import com.yunat.ccms.schedule.core.latch.GatewayCountDownLatch;
import com.yunat.ccms.schedule.core.latch.NodeCountDownLatch;
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
public class CountDownLatchService {

	private static Logger logger = LoggerFactory.getLogger(CountDownLatchService.class);

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private TaskCountDownLatchRepository countDownLatchRepository;

	/**
	 * 流程初始化，重建内存的countDownLatch
	 * 
	 * @param campId
	 * @param jobId
	 */
	public void rebuildLatchForJob(Long campId, Long jobId) {
		// 流程执行前，初始化一些关键节点需要处理的逻辑数据结构
		WorkFlow wf = workflowDao.getWorkflowByCampId(campId);

		WorkFlowGraph<Node, Connect> workflow = WorkFlowGraph.getWorkFlowGraph(wf);
		final FlowCountDownLatch flowLatch = countDownLatchRepository
				.createNewFlowLatch(jobId, workflow.getNodeCount());
		for (Node node : workflow.getAllNodes()) {
			int inDegree = workflow.getInDegree(node);
			if (inDegree > 1) {
				// 先创建，后面再设置
				countDownLatchRepository.createNewGateWayLatch(jobId, node.getId(), inDegree);
			}
		}
		for (Node node : workflow.getAllNodes()) {
			final NodeCountDownLatch nodeLatch = countDownLatchRepository.createNewNodeLatch(jobId, node.getId());
			// 将jobLatch设置到前面节点的相应属性中
			nodeLatch.setFlowCountDownLatch(flowLatch);
			Collection<Node> nextNodes = workflow.getNextNodes(node);
			for (Node next : nextNodes) {
				if (workflow.getInDegree(next) > 1) {
					GatewayCountDownLatch gatewayLatch = countDownLatchRepository.retrieveGateWayLatch(jobId,
							next.getId());
					// 将gatewayLatch设置到前面节点的相应属性中
					nodeLatch.addGatewayCountDownLatch(gatewayLatch);
				}
			}

		}
	}

	/**
	 * Recover节点专用,用于恢复CountDownLatch
	 * 
	 * @param jobId
	 * @param nodeIds
	 */
	public void resetLatchForRecoverNodes(Long jobId, List<Long> nodeIds) {
		for (Long nodeId : nodeIds) {
			NodeCountDownLatch nodeCountDownLatch = countDownLatchRepository.retrieveNodeLatch(jobId, nodeId);
			if (nodeCountDownLatch != null) {
				boolean isReallyCountUp = nodeCountDownLatch.countUp();
				logger.info("countUp Latch:{},Result:{}", nodeCountDownLatch.getLatchId(), isReallyCountUp);
			}
		}
	}

}
