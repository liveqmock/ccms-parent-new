package com.yunat.ccms.rule.center.runtime.mq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.yunat.ccms.rule.center.RuleCenterCons;

@Component
public class RcOrderServiceImpl implements RcOrderService {

	private static Logger logger = LoggerFactory.getLogger(RcOrderServiceImpl.class);

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void save(final long batchId, final List<String> allTids) {
		@SuppressWarnings("unchecked")
		final Map<String, ?>[] batchValues = new Map[allTids.size()];
		int idx = 0;
		for (final String tid : allTids) {
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("batchId", batchId);
			map.put("tid", tid);
			batchValues[idx++] = map;
		}
		try {
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_INSERT_RC_ORDER_BUFFER, batchId);
			final int[] updated = namedParameterJdbcTemplate.batchUpdate(
					RuleCenterCons.SQLExpression.SQL_INSERT_RC_ORDER_BUFFER, batchValues);
			logger.info("规则引擎：成功更新记录数:{}", updated.toString());
		} catch (final Exception e) {
			logger.error("规则引擎：订单id后保存到rc_order_buffer失败,原因{}", e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void updateStatus(final long batchId) {
		try {
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_UPDATE_ORDER_TO_RC_ORDER_BUFFER,
					batchId);
			namedParameterJdbcTemplate.getJdbcOperations().update(
					RuleCenterCons.SQLExpression.SQL_UPDATE_ORDER_TO_RC_ORDER_BUFFER, batchId);
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_UPDATE_RC_ORDER_BUFFER_1, batchId);
			namedParameterJdbcTemplate.getJdbcOperations().update(
					RuleCenterCons.SQLExpression.SQL_UPDATE_RC_ORDER_BUFFER_1, batchId);
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_UPDATE_RC_ORDER_BUFFER_2, batchId);
			namedParameterJdbcTemplate.getJdbcOperations().update(
					RuleCenterCons.SQLExpression.SQL_UPDATE_RC_ORDER_BUFFER_2, batchId);
		} catch (final DataAccessException e) {
			logger.error("规则引擎：更新待处理订单状态失败,原因{}", e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void toRcJob(final long batchId) {
		try {
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_INSERT_IGNORE_RC_JOB, batchId);
			namedParameterJdbcTemplate.getJdbcOperations().update(
					RuleCenterCons.SQLExpression.SQL_INSERT_IGNORE_RC_JOB, batchId);
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_UPDATE_RC_JOB_MODIFIED, batchId);
			namedParameterJdbcTemplate.getJdbcOperations().update(
					RuleCenterCons.SQLExpression.SQL_UPDATE_RC_JOB_MODIFIED, batchId);
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_BACKUP_RC_ORDER_BUFFER, batchId);
			namedParameterJdbcTemplate.getJdbcOperations().update(
					RuleCenterCons.SQLExpression.SQL_BACKUP_RC_ORDER_BUFFER, batchId);
			logger.info("规则引擎：SQL:{},batchId:{}", RuleCenterCons.SQLExpression.SQL_DELETE_AFTER_BACKUP_RC_ORDER_BUFFER,
					batchId);
			namedParameterJdbcTemplate.getJdbcOperations().update(
					RuleCenterCons.SQLExpression.SQL_DELETE_AFTER_BACKUP_RC_ORDER_BUFFER, batchId);
		} catch (final DataAccessException e) {
			logger.error("规则引擎：将订单转入规则引擎处理队列 出现异常,原因{}", e.getMessage());
			e.printStackTrace();
		}
	}
}
