package com.yunat.ccms.schedule.core.disruptor.handler;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.EventHandler;
import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskCountDownLatchRepository;
import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.core.latch.FlowCountDownLatch;
import com.yunat.ccms.schedule.core.latch.GatewayCountDownLatch;
import com.yunat.ccms.schedule.core.latch.NodeCountDownLatch;
import com.yunat.ccms.schedule.core.task.TaskNamingUtil;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.core.task.TaskRegistry;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.service.WorkflowService;
import com.yunat.ccms.schedule.support.ScheduleCons;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.domain.WorkFlowGraph;

/**
 * 跳过事件处理
 * 
 * @author xiaojing.qu
 * 
 */
@Component("skipEventHandler")
@Scope("singleton")
public class SkipEventHandler implements EventHandler<Event>, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(SkipEventHandler.class);

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private TaskCountDownLatchRepository countDownLatchRepository;

	@Autowired
	private TaskRegistry taskRegistry;

	@Override
	public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
		Map<String, ?> data = event.getData();
		Object task = data.get(ScheduleCons.KEY_TASK);
		logger.info("处理跳过事件:" + task.toString());
		try {
			if (task instanceof NodeTask) {
				handleNodeTaskSkip((NodeTask) task);
			}
			// 后面两种逻辑上不可能出现
			if (task instanceof FlowTask) {
				handleFlowTaskSkip((FlowTask) task);
			}
			if (task instanceof CampTask) {
				handleCampaignTaskSkip((CampTask) task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void handleNodeTaskSkip(NodeTask nodeTask) throws Exception {
		Long campId = nodeTask.getCampId();
		Long jobId = nodeTask.getJobId();
		Long nodeId = nodeTask.getNodeId();
		boolean isTest = nodeTask.isTest();
		LogSubjob logSubjob = jobLogService.getSubjob(jobId, nodeId);

		NodeCountDownLatch nodeCountDownLatch = countDownLatchRepository.retrieveNodeLatch(jobId, nodeId);
		nodeCountDownLatch.countDown();

		WorkFlowGraph<Node, Connect> workflow = workflowService.getWorkFlowGraph(campId);
		Collection<Node> nextNodeList = workflow.getNextNodes(logSubjob.getNode());
		for (Node nextNode : nextNodeList) {
			int inDegree = workflow.getInDegree(nextNode);
			boolean startNextTask = false;
			if (inDegree == 1) {
				startNextTask = true;
			} else if (inDegree > 1) {
				GatewayCountDownLatch gatewayLatch = countDownLatchRepository.retrieveGateWayLatch(jobId,
						nextNode.getId());
				if (gatewayLatch != null && gatewayLatch.check()) {
					startNextTask = true;
				}
			}
			if (startNextTask) {
				NodeTask nextNodeTask = taskRepository.createNodeTask(campId, jobId, nextNode.getId(),
						nextNode.getType(), nodeTask.isTest(), null);
				disruptor.fireSkipEvent(nextNodeTask);
			}
		}

		logSubjob.setStatus(logSubjob.getState().handleSkip().getCode());
		jobLogService.updateSubjob(logSubjob);

		FlowCountDownLatch flowLatch = nodeCountDownLatch.getFlowCountDownLatch();
		if (flowLatch.check()) {
			String flowTaskId = TaskNamingUtil.getFlowTaskId(campId, isTest, jobId);
			Task flowTask = taskRegistry.getTaskById(flowTaskId);
			disruptor.fireEndEvent(flowTask);
		}
		taskRegistry.unregister(nodeTask);
	}

	private void handleFlowTaskSkip(FlowTask flowTask) throws Exception {
		logger.error("handleFlowTaskSkip will never be implemented");
	}

	private void handleCampaignTaskSkip(CampTask campaignTask) throws Exception {
		logger.error("handleCampaignTaskSkip  will never be implemented");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		disruptor.handSkipEventWith(this);
	}

}
