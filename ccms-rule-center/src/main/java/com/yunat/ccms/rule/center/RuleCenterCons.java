package com.yunat.ccms.rule.center;

import java.util.regex.Pattern;

import com.yunat.ccms.rule.center.runtime.job.RcJobStatus;
import com.yunat.ccms.rule.center.runtime.mq.RcOrderFilterResult;

public interface RuleCenterCons {

	/**
	 * 一个店铺最多有多少个方案
	 */
	int MAX_PLANS_COUNT_OF_SHOP = 3;

	/**
	 * 一个方案最多有多少个规则
	 */
	int MAX_RULES_COUNT_OF_PLAN = 10;

	/**
	 * 一个规则最多有多少个条件
	 */
	int MAX_CONDITIONS_COUNT_OF_RULE = 30;

	/**
	 * 规则的备注最多多少个字
	 */
	int REMARK_CONTENT_MAX_LEN = 100;

	/**
	 * 方案组的备注签名最多多少个字.
	 */
	int SIGN_MAX_LEN = 20;

	/**
	 * 方案名字最长字数
	 */
	int PLAN_NAME_MAX_LEN = 20;
	/**
	 * 规则名字最长字数
	 */
	int RULE_NAME_MAX_LEN = 30;
	/**
	 * 条件名字最长字数
	 */
	int CONDITION_NAME_MAX_LEN = 100;

	String RULE_STORE_ROOT = "drools";
	String RULE_STORE_RUNTIME = "runtime";
	String RULE_STORE_BACKUP = "backup";
	String DRL_FILE_EXTENSION = ".drl";

	/**
	 * 备注分隔符
	 */
	String REMARK_CONTENT_SEPERATOR = "；";

	public static final String DEFAULT_SIGN = "数云";

	public static final Pattern COMMA = Pattern.compile("\\s*,\\s*");

	interface SQLExpression {
		String SQL_INSERT_RC_ORDER_BUFFER = "insert into rc_order_buffer(batch_id,tid) values(:batchId,:tid);";

		/*** 将订单最新的状态更新到rc_order_buffer */
		String SQL_UPDATE_ORDER_TO_RC_ORDER_BUFFER = "update rc_order_buffer o INNER JOIN plt_taobao_order_tc t ON o.tid=t.tid set o.status=t.status,o.modified=t.modified, o.shop_id=t.dp_id where o.batch_id=?";

		/*** rc_order_buffer中状态不为WAIT_SELLER_SEND_GOODS的标记为不处理 */
		String SQL_UPDATE_RC_ORDER_BUFFER_1 = "update rc_order_buffer set decision= '"
				+ RcOrderFilterResult.SKIP.name() + "' where status!='WAIT_SELLER_SEND_GOODS' and batch_id=?";

		/*** rc_order_buffer中状态为WAIT_SELLER_SEND_GOODS的标记为需要处理 */
		String SQL_UPDATE_RC_ORDER_BUFFER_2 = "update rc_order_buffer set decision= '"
				+ RcOrderFilterResult.PROCESS.name() + "' where status='WAIT_SELLER_SEND_GOODS' and batch_id=?";

		/*** 将需要处理的订单插入rc_job表中待处理 */
		String SQL_INSERT_IGNORE_RC_JOB = "insert IGNORE into rc_job(tid,shop_id,submit_time,job_status) "
				+ " select tid,shop_id,now(),'" + RcJobStatus.INIT.name()
				+ "' from rc_order_buffer where batch_id=? and decision= '" + RcOrderFilterResult.PROCESS.name()
				+ "'  ";

		/*** 更新还未处理的订单的最后更新时间 */
		String SQL_UPDATE_RC_JOB_MODIFIED = " update rc_job j INNER JOIN rc_order_buffer o ON j.tid=o.tid  set j.modified=o.modified where  o.batch_id=? and j.job_status in ('"
				+ RcJobStatus.INIT + "','" + RcJobStatus.ACQUIRED + "')";

		String SQL_BACKUP_RC_ORDER_BUFFER = " insert IGNORE into rc_order_backlog select * from rc_order_buffer where batch_id=? ";
		String SQL_DELETE_AFTER_BACKUP_RC_ORDER_BUFFER = "delete from rc_order_buffer where batch_id=?  ";

	}
}
