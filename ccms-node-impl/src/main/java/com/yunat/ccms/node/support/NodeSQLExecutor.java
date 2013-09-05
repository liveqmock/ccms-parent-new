package com.yunat.ccms.node.support;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 用于在节点内执行SQL，在节点Processor中只允许使用该实例访问DB
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public final class NodeSQLExecutor {

	private static final Logger logger = LoggerFactory.getLogger(NodeSQLExecutor.class);

	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/*** 用来记日志的，不要用作其他用途 */
	private ThreadLocal<String> debugHelper = new ThreadLocal<String>();

	private String logAndTransform(String sql) {
		if (debugHelper.get() == null) {
			logger.info(sql);
			return sql;
		}
		String extraInfo = debugHelper.get().trim();
		if (extraInfo.matches(".*\\*/.*")) {
			logger.info(sql);
			return sql;
		}
		String sqlWithLogInfo = "/* " + extraInfo + " */ " + sql;
		logger.info(sqlWithLogInfo);
		return sqlWithLogInfo;
	}

	public void setDebugInfo(String debugInfo) {
		debugHelper.remove();
		if (debugInfo.matches(".*\\*/.*")) {
			logger.warn(debugInfo);
			return;
		}
		debugHelper.set(debugInfo);
	}

	public void removeDebugInfo() {
		debugHelper.remove();
	}

	public int execute(String sql, Object... args) {
		return namedParameterJdbcTemplate.getJdbcOperations().update(logAndTransform(sql), args);
	}

	public int execute(String sql, Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.update(logAndTransform(sql), paramMap);
	}

	public int queryForInt(String sql, Object... args) {
		return namedParameterJdbcTemplate.getJdbcOperations().queryForInt(logAndTransform(sql), args);
	}

	public int queryForInt(String sql, Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForInt(logAndTransform(sql), paramMap);
	}

	public <T> List<T> queryForList(String sql, Class<T> type, Object... args) {
		return namedParameterJdbcTemplate.getJdbcOperations().queryForList(logAndTransform(sql), type, args);
	}

	public <T> List<T> queryForList(String sql, Class<T> type, Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForList(logAndTransform(sql), paramMap, type);
	}

	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return namedParameterJdbcTemplate.getJdbcOperations().queryForList(logAndTransform(sql), args);
	}

	public int[] batchUpdate(String sql, BatchPreparedStatementSetter args) {
		return namedParameterJdbcTemplate.getJdbcOperations().batchUpdate(sql, args);
	}

	public long queryForLong(String sql, Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForLong(logAndTransform(sql), paramMap);
	}

	public int update(String sql, Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.update(logAndTransform(sql), paramMap);
	}

}