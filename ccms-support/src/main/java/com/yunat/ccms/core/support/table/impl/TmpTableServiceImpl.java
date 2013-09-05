package com.yunat.ccms.core.support.table.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.table.TmpTableService;

@Component
public class TmpTableServiceImpl implements TmpTableService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JdbcTemplate jdbcExecutor;

	public void clearOnJobSuccess(Long jobId) {
		logger.info("job:{}成功结束,删除相关中间表", jobId);
		List<Map<String, Object>> list = jdbcExecutor
				.queryForList("select table_view_name,table_view_type from  twf_log_node_mids where job_id=" + jobId);
		for (Map<String, Object> map : list) {
			String name = map.get("table_view_name").toString();
			String type = map.get("table_view_type").toString();
			String sql = null;
			if (type.equalsIgnoreCase("table")) {
				sql = "drop table if exists " + name + " cascade ";
				logger.info("删除Job:{}临时表:{}", jobId, name);
			} else if (type.equalsIgnoreCase("view")) {
				sql = "drop view if exists " + name + " cascade";
				logger.info("删除Job:{}临时视图:{}", jobId, name);
			} else {

			}
			try {
				if (sql != null) {
					jdbcExecutor.execute(sql);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 每隔5分钟清除一次临时表/视图
	 */
	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void scheduled() {
		// 先清理job/subjob已经删掉的记录
		clearDisappeared();

	}

	/**
	 * 删除已经不存在的job的相关临时表和视图
	 */
	public void clearDisappeared() {
		List<Map<String, Object>> list = jdbcExecutor
				.queryForList("select job_id,table_view_name,table_view_type from  twf_log_node_mids m where  not exists (select 1 from twf_log_job j where j.job_id=m.job_id)");
		for (Map<String, Object> map : list) {
			String jobId = map.get("job_id").toString();
			String name = map.get("table_view_name").toString();
			String type = map.get("table_view_type").toString();
			String dropTableViewSql = null;
			String deleteMidsRecored = "delete from twf_log_node_mids where job_id=? and table_view_name =? ";
			if (type.equalsIgnoreCase("table")) {
				dropTableViewSql = "drop table if exists " + name + " cascade ";// +" purge";
				logger.info("定时删除,已被删除的Job:{}产生的临时表:{}", jobId, name);
			} else if (type.equalsIgnoreCase("view")) {
				dropTableViewSql = "drop view if exists " + name + " cascade";
				logger.info("定时删除,已被删除的Job:{}产生的临时视图:{}", jobId, name);
			} else {

			}
			try {
				jdbcExecutor.execute(dropTableViewSql);
				jdbcExecutor.update(deleteMidsRecored, jobId, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除已过时的
	 * 1：节点执行中产生，后面不再使用，并且节点已结束
	 * 2：节点传输给下个节点，并且后面的节点都已结束
	 */
	public void clearTimeouted() {

	}

}
