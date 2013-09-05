package com.yunat.ccms.schedule.core.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.start.NodeStart;
import com.yunat.ccms.node.biz.time.NodeTime;
import com.yunat.ccms.node.biz.wait.NodeWait;
import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskExecutor;
import com.yunat.ccms.schedule.core.TaskRouter;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;

/**
 * 
 * 静态的路由，每个Task根据其特征分配到不同的Executor
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class DefaultTaskRouter implements TaskRouter {

	@Autowired
	@Qualifier("FAST_MOVING")
	private TaskExecutor fastExecutor;

	@Autowired
	@Qualifier("IO_BOUND")
	private TaskExecutor iolimitedExecutor;

	@Autowired
	@Qualifier("MEM_BOUND")
	private TaskExecutor memlimitedExecutor;

	@Autowired
	@Qualifier("NORMAL")
	private TaskExecutor defaultExecutor;

	@Override
	public TaskExecutor route(Task task) {
		// TODO stratagy to find
		if (task instanceof CampTask || task instanceof FlowTask) {
			return fastExecutor;
		}
		NodeTask nodeTask = (NodeTask) task;
		String nodeType = nodeTask.getNodeType();
		if (NodeStart.TYPE.equals(nodeType) || NodeTime.TYPE.equals(nodeType) || NodeWait.TYPE.equals(nodeType)) {
			return fastExecutor;
		}
		if (nodeType.startsWith("tfilterfind")) {
			return iolimitedExecutor;
		}
		if (nodeType.startsWith("tcommunicate")) {
			return memlimitedExecutor;
		}
		return defaultExecutor;
	}

	@Override
	public Object statistics() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("FAST_MOVING", fastExecutor.statistics());
		map.put("IO_BOUND", iolimitedExecutor.statistics());
		map.put("MEM_BOUND", memlimitedExecutor.statistics());
		map.put("NORMAL", defaultExecutor.statistics());
		return map;
	}
}
