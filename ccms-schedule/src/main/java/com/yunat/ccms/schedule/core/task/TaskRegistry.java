package com.yunat.ccms.schedule.core.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.core.Task;

/**
 * 注册当前的任务
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class TaskRegistry {

	private static Logger logger = LoggerFactory.getLogger(TaskRegistry.class);

	/*** 所有任务注册 */
	private static final Map<String, Task> taskRegistery = new ConcurrentHashMap<String, Task>();

	/*** 所有任务的子任务注册 */
	private static final Map<String, List<Task>> subTaskRegitery = new ConcurrentHashMap<String, List<Task>>();

	/*** 所有定时的任务 */
	private static final Set<String> timedTaskRegistery = new ConcurrentSkipListSet<String>();

	/**
	 * 注册一个Task
	 */
	public void register(Task task) {
		logger.info("register Task:{} !", task.getTaskId());
		taskRegistery.put(task.getTaskId(), task);
		if (task instanceof FlowTask) {
			FlowTask flowTask = (FlowTask) task;
			String campTaskId = TaskNamingUtil.getCampTaskId(task.getCampId(), task.isTest());
			List<Task> tasks = subTaskRegitery.get(campTaskId);
			if (tasks == null) {
				tasks = new ArrayList<Task>();
				subTaskRegitery.put(campTaskId, tasks);
			}
			tasks.add(flowTask);
		}
		if (task instanceof NodeTask) {
			NodeTask nodeTask = (NodeTask) task;
			String flowTaskId = TaskNamingUtil
					.getFlowTaskId(nodeTask.getCampId(), nodeTask.isTest(), nodeTask.getJobId());
			List<Task> tasks = subTaskRegitery.get(flowTaskId);
			if (tasks == null) {
				tasks = new ArrayList<Task>();
				subTaskRegitery.put(flowTaskId, tasks);
			}
			tasks.add(nodeTask);
		}
	}

	/**
	 * 注销一个Task
	 */
	public void unregister(Task task) {
		logger.info("unregister Task:{} !", task.getTaskId());
		taskRegistery.remove(task.getTaskId());
		subTaskRegitery.remove(task.getTaskId());
	}

	/**
	 * 获得一个已创建的Task，若不存在，返回null
	 * 
	 * @param taskId
	 * @return
	 */
	public Task getTaskById(String taskId) {
		Task task = taskRegistery.get(taskId);
		if (task == null) {
			logger.error("Task:{} not Found!", taskId);
		}
		return task;
	}

	/**
	 * 获取一个任务的子任务
	 * 
	 * @param parentTaksId
	 * @return
	 */
	public List<Task> getSubTasks(String parentTaksId) {
		return subTaskRegitery.get(parentTaksId);
	}
}
