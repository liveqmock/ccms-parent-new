package com.yunat.ccms.schedule.core.task;

import java.text.MessageFormat;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.core.support.statemachine.state.JobState;
import com.yunat.ccms.core.support.statemachine.state.SubjobState;
import com.yunat.ccms.node.spi.NodeEntity;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessingException;
import com.yunat.ccms.node.spi.NodeProcessor;
import com.yunat.ccms.node.spi.NodeRetryAble;
import com.yunat.ccms.node.support.NodeSQLExecutor;
import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.service.JobDataService;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.service.NodeProcessService;
import com.yunat.ccms.schedule.service.WorkflowService;
import com.yunat.ccms.schedule.support.NodeTaskTerminator;
import com.yunat.ccms.schedule.support.ParamHolder;
import com.yunat.ccms.schedule.support.ScheduleCons;
import com.yunat.ccms.workflow.domain.Node;

public class NodeTask extends Task {

	private static final Logger logger = LoggerFactory.getLogger(NodeTask.class);

	private final long jobId;
	private final long nodeId;
	private final long subjobId;
	private final String nodeType;

	public NodeTask(long campId, long jobId, long nodeId, long subjobId, String nodeType, boolean isTest,
			TaskTrigger trigger, ParamHolder extra) {
		super(campId, isTest, trigger, extra);
		this.jobId = jobId;
		this.nodeId = nodeId;
		this.subjobId = subjobId;
		this.nodeType = nodeType;
	}

	@Autowired
	private NodeProcessService nodeProcessService;

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private JobLogService joblogService;

	@Autowired
	private JobDataService jobDataService;

	@Autowired
	private NodeSQLExecutor nodeSQLExecutor;

	@Autowired
	private NodeTaskTerminator nodeTaskTerminator;

	@Override
	public String getTaskId() {
		return TaskNamingUtil.getNodeTaskId(campId, isTest, jobId, nodeId);
	}

	@Override
	public void run() {
		nodeSQLExecutor.setDebugInfo(TaskNamingUtil.getNodeDebugInfo(campId, jobId, nodeId, subjobId, isTest));
		try {
			action();
		} catch (Exception e) {// TO prevent Thread leak
			e.printStackTrace();
		} finally {
			nodeSQLExecutor.removeDebugInfo();
		}
	}

	private void action() {

		logger.info("节点执行开始：" + this.toString());
		Node node = workflowService.getNodeById(nodeId);
		LogJob job = joblogService.getJob(jobId);
		LogSubjob subjob = joblogService.getSubjob(jobId, nodeId);
		subjob.setStatus(subjob.getState().handleStart().getCode().longValue());
		subjob.setStarttime(DateTime.now().toDate());
		joblogService.updateSubjob(subjob);

		NodeProcessor processor = nodeProcessService.getNodeProcessor(node.getType()); // com.yunat.nodeimpl.NodeTime
		NodeProcessingContext context = nodeProcessService.createNodeProcessingContext(subjob);
		NodeEntity config = nodeProcessService.getNodeEntity(nodeType, nodeId);

		Boolean triggerFireAgain = this.getRuntimeParam(ScheduleCons.KEY_TRIGGER_FIRE_AGAIG, false);
		try {
			NodeOutput output = processor.process(config, context);
			jobDataService.saveJobData(subjob, output);
			subjob.setOutputMsg(output != null ? output.getOutputMsg() : null);
			logger.info("节点正常执行：" + this.toString() + ",显示：" + subjob.getOutputMsg());
			subjob.setStatus(subjob.getState().handleFinish().getCode());
		} catch (NodeProcessingException npe) {
			logger.error("节点抛出异常：" + this.toString(), npe);
			if (processor instanceof NodeRetryAble && ((NodeRetryAble) processor).canRetry(context)) {
				subjob.setStatus(subjob.getState().handleException().getCode());
				subjob.setMemo(npe.getMessage());
			} else {
				subjob.setStatus(subjob.getState().handleError().getCode());
				subjob.setMemo(npe.getMessage());
			}
		} catch (Exception e) {
			logger.error("节点抛出异常：" + this.toString(), e);
			subjob.setStatus(subjob.getState().handleError().getCode());
		} finally {
			if (nodeTaskTerminator.isNodeTaskKilled(this)) {
				logger.info("节点被用户手动中止：" + this.toString());
				subjob.setStatus(SubjobState.FINISH_STOP.getCode());
				subjob.setMemo("活动被停止执行");
			}
		}
		if (triggerFireAgain) {// 多次执行的节点
			logger.info("节点将在后面再次触发：" + this.toString());
			subjob.setStatus(subjob.getState().handleWaitTimer().getCode());
		}
		subjob.setEndtime(DateTime.now().toDate());
		joblogService.updateSubjob(subjob);
		if (subjob.getState().isError()) {
			job = joblogService.getJob(job.getJobId());
			job.setStatus(JobState.fromCode(job.getStatus()).handleError().getCode());
			joblogService.updateJob(job);
		}
		logger.info("节点执行结束：" + this.toString());
		if (!triggerFireAgain) {
			disruptor.fireEndEvent(this);
		}
	}

	@Override
	public String toString() {
		return MessageFormat.format("NodeTask[camp_id={0},job_id={1},node_id={2},subjob_id={3},{4}]",
				String.valueOf(campId), String.valueOf(jobId), String.valueOf(nodeId), String.valueOf(subjobId),
				ScheduleCons.execType(isTest));
	}

	public String getNodeType() {
		return nodeType;
	}

	public long getJobId() {
		return jobId;
	}

	public long getNodeId() {
		return nodeId;
	}

	public long getSubjobId() {
		return subjobId;
	}

}
