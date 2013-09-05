package com.yunat.ccms.schedule.support;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.core.task.TaskNamingUtil;

/**
 * Task终结者
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class NodeTaskTerminator {

	private static final Logger logger = LoggerFactory.getLogger(NodeTaskTerminator.class);

	/*** 获取节点任务的数据库连接ID */
	private static final String SQL_GET_CONNECTIONID = " select ID from information_schema.PROCESSLIST where DB=DATABASE() and ID!= CONNECTION_ID() and INFO like ? ;";
	/*** 中断正在执行的SQL */
	private static final String SQL_KILL_QUERY = "kill query ? ;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private ConcurrentSkipListSet<String> killedTasks = new ConcurrentSkipListSet<String>();

	/**
	 * 通过kill query方式来中止正在执行的节点执行
	 * 
	 * @param subjobId
	 */
	public void killNodeTask(NodeTask nodeTask) {
		try {
			Long campId = nodeTask.getCampId();
			Long jobId = nodeTask.getJobId();
			Long nodeId = nodeTask.getNodeId();
			Long subjobId = nodeTask.getSubjobId();
			boolean isTest = nodeTask.isTest();
			String nodeTaskDebugInfo = TaskNamingUtil.getNodeDebugInfo(campId, jobId, nodeId, subjobId, isTest);
			if (subjobId != null) {
				List<Long> connectionIds = jdbcTemplate.queryForList(SQL_GET_CONNECTIONID, Long.class, "%"
						+ nodeTaskDebugInfo + "%");
				if (connectionIds != null) {
					for (Long cid : connectionIds) {
						killedTasks.add(nodeTask.getTaskId());
						logger.info("Kill JDBC Query:{} of NodeTask:{}", cid, nodeTask.getTaskId());
						jdbcTemplate.update(SQL_KILL_QUERY, cid);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 某个任务是否被kill
	 * 
	 * @return
	 */
	public boolean isNodeTaskKilled(NodeTask nodeTask) {
		if (nodeTask != null && killedTasks.contains(nodeTask.getTaskId())) {
			killedTasks.remove(nodeTask.getTaskId());// 阅后即焚
			return true;
		}
		return false;
	}
}
