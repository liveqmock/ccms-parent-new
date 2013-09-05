package com.yunat.ccms.schedule.core.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskExecutor;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.support.NodeTaskTerminator;

public class QueuedTaskExecutor implements TaskExecutor, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(QueuedTaskExecutor.class);

	private final String name;
	private final ThreadPoolExecutor executor;

	@Autowired
	private NodeTaskTerminator nodeTaskTerminator;

	public QueuedTaskExecutor(String name, ThreadPoolExecutor executor) {
		this.name = name;
		this.executor = executor;
	}

	@Override
	public void execute(Task task) throws Exception {
		applicationContext.getAutowireCapableBeanFactory().autowireBean(task);
		executor.execute(task);
		logger.info("Task:[{}] submit to Executor:[{}],current count:{}", new Object[] { task.getTaskId(), name,
				executor.getTaskCount() });
	}

	@Override
	public void cancel(Task task) throws Exception {
		boolean removed = executor.remove(task);
		logger.info("Task[{}] removed from Executor[{}],return:{}", new Object[] { task.getTaskId(), name, removed });
		if (!removed && task != null && task instanceof NodeTask) {
			nodeTaskTerminator.killNodeTask((NodeTask) task);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object statistics() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("active", executor.getActiveCount());
		map.put("poolSize", executor.getPoolSize());
		map.put("queueSize", executor.getQueue().size());
		return map;
	}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		applicationContext.getAutowireCapableBeanFactory().autowireBean(this);

	}

}
