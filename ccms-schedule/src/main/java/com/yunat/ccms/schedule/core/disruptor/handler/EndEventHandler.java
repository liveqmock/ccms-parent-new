package com.yunat.ccms.schedule.core.disruptor.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.EventHandler;
import com.yunat.ccms.core.support.statemachine.state.JobState;
import com.yunat.ccms.core.support.statemachine.state.SubjobState;
import com.yunat.ccms.core.support.table.TmpTableService;
import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskCountDownLatchRepository;
import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.core.latch.FlowCountDownLatch;
import com.yunat.ccms.schedule.core.latch.GatewayCountDownLatch;
import com.yunat.ccms.schedule.core.latch.NodeCountDownLatch;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.core.task.TaskNamingUtil;
import com.yunat.ccms.schedule.core.task.TaskRegistry;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.service.WorkflowService;
import com.yunat.ccms.schedule.support.ParamHolder;
import com.yunat.ccms.schedule.support.ScheduleCons;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.domain.WorkFlowGraph;

/**
 * 结束事件处理
 * 
 * @author xiaojing.qu
 * 
 */
@Component("endEventHandler")
@Scope("singleton")
public class EndEventHandler implements EventHandler<Event>, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(EndEventHandler.class);

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private TaskCountDownLatchRepository countDownLatchRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private TmpTableService tmpTableService;

	@Autowired
	private TaskRegistry taskRegistry;

	@Override
	public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
		Map<String, ?> data = event.getData();
		Object task = data.get(ScheduleCons.KEY_TASK);
		logger.info("处理结束事件:" + task.toString());
		try {
			if (task instanceof CampTask) {
				handleCampaignTaskEnd((CampTask) task);
			}
			if (task instanceof FlowTask) {
				handleFlowTaskEnd((FlowTask) task);
			}
			if (task instanceof NodeTask) {
				handleNodeTaskEnd((NodeTask) task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void handleNodeTaskEnd(NodeTask nodeTask) throws Exception {
		Long campId = nodeTask.getCampId();
		Long jobId = nodeTask.getJobId();
		Long nodeId = nodeTask.getNodeId();
		boolean isTest = nodeTask.isTest();
		Node node = workflowService.getNodeById(nodeId);
		LogJob job = jobLogService.getJob(jobId);
		LogSubjob subjob = jobLogService.getSubjob(jobId, nodeId);

		NodeCountDownLatch nodeCountDownLatch = countDownLatchRepository.retrieveNodeLatch(jobId, nodeId);
		nodeCountDownLatch.countDown();

		WorkFlowGraph<Node, Connect> workflowGraph = workflowService.getWorkFlowGraph(job.getCampId());
		Collection<Node> nextNodes = workflowGraph.getNextNodes(node);
		for (Node nextNode : nextNodes) {
			int inDegree = workflowGraph.getInDegree(nextNode);
			boolean startNextTask = false;
			if (inDegree == 1) {
				startNextTask = true;
			} else if (inDegree > 1) {
				GatewayCountDownLatch gatewayLatch = countDownLatchRepository.retrieveGateWayLatch(job.getJobId(),
						nextNode.getId());
				if (gatewayLatch != null && gatewayLatch.check()) {
					startNextTask = true;
				}
			}
			if (startNextTask) {
				Collection<Node> preNodes = workflowGraph.getPreNodes(nextNode);
				List<Long> nodeIds = new ArrayList<Long>();
				for (Node preNode : preNodes) {
					nodeIds.add(preNode.getId());
				}
				boolean predecessorsAllSuccess = jobLogService.checkPreNodeSuccess(jobId, nodeIds);
				logger.info("前面节点是否全部成功[camp_id={},node_id={},job_id={}],返回{}",
						new Object[] { job.getCampId(), node.getId(), jobId, predecessorsAllSuccess });

				ParamHolder params = new ParamHolder();
				params.put(ScheduleCons.KEY_PRE_NODE_ID, node.getId());
				params.put(ScheduleCons.KEY_PRE_NODE_TYPE, node.getType());
				final NodeTask nextNodeTask = taskRepository.createNodeTask(campId, jobId, nextNode.getId(),
						nextNode.getType(), job.isTest(), params);
				if (predecessorsAllSuccess) {
					disruptor.fireStartEvent(nextNodeTask);
				} else {
					disruptor.fireSkipEvent(nextNodeTask);
				}
			}
		}
		SubjobState subjobState = subjob.getState();
		subjob.setStatus(subjobState.handleComplete().getCode());
		jobLogService.updateSubjob(subjob);
		FlowCountDownLatch flowLatch = nodeCountDownLatch.getFlowCountDownLatch();
		if (flowLatch.check()) {
			String flowTaskId = TaskNamingUtil.getFlowTaskId(campId, isTest, jobId);
			// TODO 确认不为空
			Task flowTask = taskRegistry.getTaskById(flowTaskId);
			disruptor.fireEndEvent(flowTask);
		}
		taskRegistry.unregister(nodeTask);
	}

	/**
	 * 处理Job结束
	 * 
	 * @param flowTask
	 * @throws Exception
	 */
	private void handleFlowTaskEnd(FlowTask flowTask) throws Exception {
		Long jobId = flowTask.getJobId();
		Long campId = flowTask.getCampId();
		boolean isTest = flowTask.isTest();
		LogJob job = jobLogService.getJob(jobId);
		JobState jobState = JobState.fromCode(job.getStatus()).handleFinish();
		job.setStatus(jobState.getCode());
		job.setEndtime(DateTime.now().toDate());
		if (jobState.isSuccess()) {
			tmpTableService.clearOnJobSuccess(job.getJobId());
		}
		// TODO process flow to end status, also do some cleanup work here
		jobLogService.updateJob(job);

		boolean isLastJob = jobLogService.isThisLastJob(job);
		if (isLastJob) {
			String taskId = TaskNamingUtil.getCampTaskId(campId, isTest);
			Task campTask = taskRegistry.getTaskById(taskId);
			disruptor.fireEndEvent(campTask);
		}
		taskRegistry.unregister(flowTask);
		logger.info(flowTask.toString() + "执行结束");
	}

	/**
	 * 处理活动结束
	 * 
	 * @param campTask
	 * @throws Exception
	 */
	private void handleCampaignTaskEnd(CampTask campTask) throws Exception {
		jobLogService.updateCampaignStatus(campTask.getCampId(), campTask.isTest());
		taskRegistry.unregister(campTask);
		logger.info(campTask.toString() + "执行结束");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("setting endEventHandler");
		disruptor.handEndEventWith(this);
	}

}
