package com.yunat.ccms.schedule.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluate;
import com.yunat.ccms.node.biz.time.NodeTime;
import com.yunat.ccms.node.biz.wait.NodeWait;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.TaskTriggerRepository;
import com.yunat.ccms.schedule.core.quartz.JobNamingUtil;
import com.yunat.ccms.schedule.core.trigger.TriggerFactory;
import com.yunat.ccms.schedule.service.WorkflowService;
import com.yunat.ccms.schedule.support.ParamHolder;
import com.yunat.ccms.schedule.support.ScheduleCons;
import com.yunat.ccms.workflow.domain.Node;

@Component
@Scope("singleton")
public class DefaultTriggerRepository implements TaskTriggerRepository {

	@Autowired
	private WorkflowService workflowService;

	@Override
	public TaskTrigger createCampTaskTrigger(Long campId, boolean isTest, ParamHolder extra) {
		boolean fireByQuartz = extra.getSafely(ScheduleCons.KEY_TRIGGER_FIRE_BY_QUARTZ, false);
		if (isTest || fireByQuartz) {
			return TriggerFactory.instantTrigger();
		}
		Node timeNode = workflowService.getTimeNodeByCampId(campId);
		Long nodeId = timeNode == null ? null : timeNode.getId();
		NodeTime nodeTime = workflowService.getNodeTime(nodeId);
		String jobName = JobNamingUtil.getCampJobName(campId);
		return TriggerFactory.createCampTrigger(jobName, nodeTime);
	}

	@Override
	public TaskTrigger createFlowTaskTrigger(Long jobId, boolean isTest, ParamHolder extra) {
		return TriggerFactory.instantTrigger();
	}

	@Override
	public TaskTrigger createNodeTaskTrigger(Long jobId, Long nodeId, String nodeType, boolean isTest, ParamHolder extra) {
		boolean fireByQuartz = extra.getSafely(ScheduleCons.KEY_TRIGGER_FIRE_BY_QUARTZ, false);
		if (isTest || fireByQuartz) {
			return TriggerFactory.instantTrigger();
		}
		String jobName = JobNamingUtil.getNodeJobName(jobId, nodeId);
		Long preNodeId = extra.get(ScheduleCons.KEY_PRE_NODE_ID);
		String preNodeType = extra.get(ScheduleCons.KEY_PRE_NODE_TYPE);
		if (preNodeId != null && preNodeType != null) {
			if (NodeWait.TYPE.equals(preNodeType)) {// 获得前面的等待节点
				NodeWait nodeWait = workflowService.getNodeWait(preNodeId);
				return TriggerFactory.createWaitNodeTrigger(jobName, nodeWait);
			}
		}
		if (NodeEvaluate.TYPE.equals(nodeType)) {
			NodeEvaluate nodeEvaluate = workflowService.getNodeEvaluate(nodeId);
			return TriggerFactory.createEvaluateNodeTrigger(jobName, nodeEvaluate);
		}
		return TriggerFactory.instantTrigger();

	}

	@Override
	public TaskTrigger instantTrigger() {
		return TriggerFactory.instantTrigger();
	}

}
