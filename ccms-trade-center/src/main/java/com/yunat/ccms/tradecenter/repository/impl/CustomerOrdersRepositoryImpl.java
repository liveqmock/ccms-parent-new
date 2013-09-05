package com.yunat.ccms.tradecenter.repository.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.tradecenter.repository.CustomerOrdersRepository;

/**
 * 客服订单关系数据处理
 *
 * @author ming.peng
 * @date 2013-6-4
 * @since 4.1.0
 */
@Repository
public class CustomerOrdersRepositoryImpl implements CustomerOrdersRepository {

	// @PersistenceContext
	// private EntityManager em;

	@Autowired
	private JdbcPaginationHelper helper;

	public double sumTotalAmount(String dpId) {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("dpId", dpId);
		StringBuffer joinTableWhereSql = new StringBuffer("from tb_tc_send_log sl left join plt_taobao_order_tc tt on sl.tid = tt.tid ")
				.append("where sl.dp_id = :dpId and sl.type in (1,2,3,4,5) and sl.created > adddate(now(), interval -3 day) ")
				.append("and sl.created = (select max(created) from tb_tc_send_log where tid = sl.tid and created < tt.pay_time)");
		String columns = " sum(tt.payment) as totalAmount ";
		joinTableWhereSql.insert(0, "select").insert(6, columns);
		Map<String, ?> map = helper.queryForMap(joinTableWhereSql.toString(), paramMap);
		return Double.parseDouble(ObjectUtils.toString(map.get("totalAmount"), "0"));
	}

	public Page<Map<String, Object>> urPayOrdersLogList(Pageable page, String dpId) {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("dpId", dpId);
		StringBuffer joinTableWhereSql = new StringBuffer("from tb_tc_send_log sl left join plt_taobao_order_tc tt on sl.tid = tt.tid ")
				.append("where sl.dp_id = :dpId and sl.type in (1,2,3,4,5) and sl.created > adddate(now(), interval -3 day) ")
				.append("and sl.created = (select max(created) from tb_tc_send_log where tid = sl.tid and created < tt.pay_time)");
		String columns = " sl.tid,sl.send_user as sendUser,date_format(sl.created, '%Y-%m-%d %H:%i:%s') as created,sl.type,date_format(tt.pay_time, '%Y-%m-%d %H:%i:%s') as payTime,tt.customerno,tt.payment ";
		joinTableWhereSql.insert(0, "select").insert(6, columns);
		return helper.queryForMap(joinTableWhereSql, paramMap, page);
	}

	public List<Map<String, Object>> getOrderReceptionWwListByDpId(String dpId) {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("dpId", dpId);
		StringBuffer sql = new StringBuffer("select service_staff_id as serviceStaffName from tb_tc_customer_orders_ship")
				.append(" where dp_id=:dpId and created > adddate(now(), interval -4 day) and service_staff_id is not null group by service_staff_id");
		return helper.queryForList(sql.toString(), paramMap);
	}

	@Transactional
	public int updateCustomerOrdersShipData(Map<String, ?> array, String hideColumnName) {
		StringBuffer sql = new StringBuffer("insert into tb_tc_customer_orders_ship(tid,created,{0}) values(:tid,now(),1)")
				.append(" on duplicate key update {0}=if({0}=1, 0, 1), updated=now()");
		String formatSql = MessageFormat.format(sql.toString(), hideColumnName);
		return helper.update(formatSql, array);
	}

	@Transactional
	public int[] updateCustomerOrdersShipData(Map<String, ?>[] array, String hideColumnName) {
		StringBuffer sql = new StringBuffer("insert into tb_tc_customer_orders_ship(tid,created,{0}) values(:tid,now(),:isHide)");
		sql.append(" on duplicate key update {0}=:isHide, updated=now()");
		String formatSql = MessageFormat.format(sql.toString(), hideColumnName);
		return helper.batchUpdate(formatSql, array);
	}

	@Transactional
	public int[] saveCustomerOrdersShipData(Map<String, ?>[] array) {
		StringBuffer sql = new StringBuffer("insert into tb_tc_customer_orders_ship(service_staff_id,dp_id,tid,order_created,created)")
				.append(" values(:service_staff_id,:dp_id,:tid,:order_created,now())")
				.append(" on duplicate key update service_staff_id=:service_staff_id,dp_id=:dp_id,order_created=:order_created,updated=now()");
		return helper.batchUpdate(sql.toString(), array);
	}

	public List<Map<String, Object>> getCustomerOrdersShipData() {
		StringBuffer sql = new StringBuffer("select * from (select t.tid,t.dp_id,t.customerno,t.order_created,")
				.append("(select c.service_staff_id from plt_taobao_chat_log c where c.dp_id = t.dp_id and c.buyer_nick = t.customerno and c.chat_time < t.created ")
				.append("and c.chat_time > adddate(now(), interval -1 month) order by c.chat_time desc limit 1) as service_staff_id ")
				.append("from plt_taobao_order_tc t where t.order_created > (select ifnull(max(order_created), adddate(now(), interval -1 month)) from tb_tc_customer_orders_ship)) c ")
				.append("where c.service_staff_id is not null");
		return helper.queryForList(sql.toString(), new HashMap<String, Object>(0));
	}

}
