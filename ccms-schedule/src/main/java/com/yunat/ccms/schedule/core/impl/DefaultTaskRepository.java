package com.yunat.ccms.schedule.core.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.TaskTriggerRepository;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.core.task.TaskRegistry;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.support.ScheduleCons;
import com.yunat.ccms.schedule.support.ParamHolder;

@Component
@Scope("singleton")
public class DefaultTaskRepository implements TaskRepository {

	@Autowired
	private TaskTriggerRepository triggerRepostory;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private TaskRegistry taskRegistry;

	@Override
	public CampTask createCampTask(Long campId, boolean isTest, ParamHolder extra) {
		extra = (extra == null) ? new ParamHolder() : extra;
		TaskTrigger trigger = triggerRepostory.createCampTaskTrigger(campId, isTest, extra);
		CampTask task = new CampTask(campId, isTest, trigger, extra);
		taskRegistry.register(task);
		return task;
	}

	@Override
	public FlowTask createFlowTask(Long campId, Long jobId, boolean isTest, ParamHolder extra) {
		extra = (extra == null) ? new ParamHolder() : extra;
		TaskTrigger trigger = triggerRepostory.createFlowTaskTrigger(jobId, isTest, extra);
		FlowTask task = new FlowTask(campId, jobId, isTest, trigger, extra);
		taskRegistry.register(task);
		return task;
	}

	@Override
	public NodeTask createNodeTask(Long campId, Long jobId, Long nodeId, String nodeType, boolean isTest,
			ParamHolder extra) {
		extra = (extra == null) ? new ParamHolder() : extra;
		TaskTrigger trigger = triggerRepostory.createNodeTaskTrigger(jobId, nodeId, nodeType, isTest, extra);
		Date plantime = trigger.getNextFireTime();
		LogSubjob subjob = jobLogService.getOrCreatSubjob(jobId, nodeId, plantime);
		NodeTask task = new NodeTask(campId, jobId, nodeId, subjob.getSubjobId(), nodeType, isTest, trigger, extra);
		boolean fireByQuartz = extra.getSafely(ScheduleCons.KEY_TRIGGER_FIRE_BY_QUARTZ, false);
		if (!fireByQuartz) {
			// there was an old one with DurableTrigger(for cancel mechanism )
			taskRegistry.register(task);
		}
		return task;
	}

}
