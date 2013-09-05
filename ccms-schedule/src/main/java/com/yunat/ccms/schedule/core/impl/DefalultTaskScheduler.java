package com.yunat.ccms.schedule.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskRouter;
import com.yunat.ccms.schedule.core.TaskScheduler;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.quartz.QuartzService;
import com.yunat.ccms.schedule.core.task.TaskRegistry;
import com.yunat.ccms.schedule.core.trigger.DurableTrigger;

@Component
@Scope("singleton")
public class DefalultTaskScheduler extends TaskScheduler {

	@Autowired
	private TaskRouter taskRouter;

	@Autowired
	private TaskRegistry taskRegistry;

	@Autowired
	private QuartzService quartzService;

	@Override
	public void submit(Task task) throws Exception {
		TaskTrigger trigger = task.getTrigger();
		if (trigger instanceof DurableTrigger) {
			quartzService.scheduleTimer((DurableTrigger) trigger);
		} else {
			quartzService.cancleTimer(task.getTaskId());
			taskRouter.route(task).execute(task);
		}
	}

	@Override
	public void cancel(Task task) throws Exception {
		TaskTrigger trigger = task.getTrigger();
		if (trigger instanceof DurableTrigger) {
			quartzService.cancelTimer((DurableTrigger) trigger);
		} else {
			taskRouter.route(task).cancel(task);
		}
		List<Task> subTasks = taskRegistry.getSubTasks(task.getTaskId());
		if (subTasks != null) {
			for (Task subTask : subTasks) {
				this.cancel(subTask);
			}
		}
		taskRegistry.unregister(task);
	}

	@Override
	public Object statistics() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tasks", taskRouter.statistics());
		return map;
	}
}
