package com.yunat.ccms.report.repository;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.report.domain.EvaluateReportDayResult;
import com.yunat.ccms.report.domain.EvaluateReportResult;

/**
 * 
 * 评估报表节点-结果集-存储
 * 
 * @author yin
 * @see http://docs.oracle.com/cd/E19798-01/821-1841/gjrij/index.html
 * 
 */

@Repository
public class EvaluateResultSetRepository {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	// 根据jobId 和 nodeId 获取渠道节点在某个批次下的评估报表结果集-汇总

	public Long findCollectByTotal(Long jobId, Long nodeId) throws Exception {

		Long customerCount = 0L;

		StringBuffer sb = new StringBuffer();

		sb.append(" select  e.pay_customer_count, e.pay_payment_sum  from evaluate_report_total_result  e   "
				+ " where e.job_id = :jobId  and  e.node_id = :nodeId ");

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("jobId", jobId);
		paramMap.put("nodeId", nodeId);

		List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sb.toString(), paramMap);

		if (!list.isEmpty() && list.get(0).get("pay_customer_count") != null) {
			customerCount = Long.valueOf(list.get(0).get("pay_customer_count").toString());
		}

		return customerCount;

	}

	// 根据jobId 和 nodeId 获取渠道节点在某个批次下的评估报表结果集-按天

	public List<EvaluateReportDayResult> findCollectByDay(Long jobId, Long nodeId, Date evaluateStartTime,
			Date evaluateEndTime) throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append(" select  DATE_FORMAT(e.evaluateTime, '%Y-%m-%d') as evaluateTime , "
				+ " sum(e.buyOrderCount) as  buyOrderCount,  sum(e.buyCustomerCount) as  buyCustomerCount, sum(e.buyPaymentSum) as  buyPaymentCount,  "
				+ " sum(e.payOrderCount) as  payOrderCount,  sum(e.payCustomerCount) as  payCustomerCount, sum(e.payPaymentSum) as  payPaymentSum , "
				+ " sum(e.productCount) as  productCount  "
				+ " from EvaluateReportDayResult e    "
				+ " where e.jobId = :jobId  and  e.nodeId =  :nodeId    "
				+ " and e.evaluateTime >= DATE_FORMAT(:evaluateStartTime,'%Y-%m-%d')   and  e.evaluateTime<= DATE_FORMAT(:evaluateEndTime,'%Y-%m-%d')   "
				+ " group by  DATE_FORMAT(e.evaluateTime, '%Y-%m-%d')   ");

		Query query = entityManager.createQuery(sb.toString());
		query.setParameter("jobId", jobId);
		query.setParameter("nodeId", nodeId);
		query.setParameter("evaluateStartTime", evaluateStartTime);
		query.setParameter("evaluateEndTime", evaluateEndTime);

		@SuppressWarnings("unchecked")
		List<Object[]> convertBeforeResults = query.getResultList();

		List<EvaluateReportDayResult> convertAfterResults = castEntity(convertBeforeResults,
				EvaluateReportDayResult.class);

		return convertAfterResults;

	}

	private static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) throws Exception {
		List<T> returnList = new ArrayList<T>();
		if (list.size() == 0) {
			return returnList;
		} else {
			Object[] co = list.get(0);
			@SuppressWarnings("rawtypes")
			Class[] c2 = new Class[co.length];

			// 确定构造方法
			for (int i = 0; i < co.length; i++) {
				c2[i] = co[i].getClass();
			}

			for (Object[] o : list) {
				Constructor<T> constructor = clazz.getConstructor(c2);
				returnList.add(constructor.newInstance(o));
			}

			return returnList;
		}

	}

	// 根据jobId 和 nodeId 获取渠道节点在某个批次下的评估报表结果集-按小时

	public List<EvaluateReportResult> findCollectByHour(Long jobId, Long nodeId, String evaluateTime)
			throws ParseException {
		Query query = entityManager
				.createQuery("select e from EvaluateReportResult e where e.jobId = :jobId  and  e.nodeId =  :nodeId  and "
						+ " e.evaluateTime like  :evaluateTime  ");
		query.setParameter("jobId", jobId).setParameter("nodeId", nodeId)
				.setParameter("evaluateTime", "%" + evaluateTime + "%");

		@SuppressWarnings("unchecked")
		List<EvaluateReportResult> results = query.getResultList();
		return results;

	}

	// XXX 需要搬移到订单明细的Repository中
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findOrderItemByTid(String tid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sqlString = "select i.oid, i.num_iid, p.title,i.num,p.price,i.total_fee,i.payment,"
				+ " CASE WHEN i.ccms_order_status = 10 THEN '已下单未付款'  "
				+ " WHEN i.ccms_order_status = 21 THEN '已付款未发货'    "
				+ " WHEN i.ccms_order_status = 22 THEN '已发货待确认'   " + " WHEN i.ccms_order_status = 23 THEN '交易成功' "
				+ " ELSE '交易失败' END    "
				+ "  from plt_taobao_order_item i, plt_taobao_product p where i.num_iid = p.num_iid and i.tid=:tid ";
		List<Object[]> results = entityManager.createNativeQuery(sqlString).setParameter("tid", tid).getResultList();

		for (Object[] o : results) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("oid", o[0] != null ? o[0].toString() : null);
			map.put("productNum", o[1] != null ? o[1].toString() : null);
			map.put("productTitle", o[2] != null ? o[2].toString() : null);
			map.put("num", o[3] != null ? Integer.parseInt(o[3].toString()) : null);
			map.put("productPrice", o[4] != null ? Double.parseDouble(o[4].toString()) : null);
			map.put("totalFee", o[5] != null ? Double.parseDouble(o[5].toString()) : null);
			map.put("payment", o[6] != null ? Double.parseDouble(o[6].toString()) : null);
			map.put("status", o[7] != null ? o[7].toString() : null);

			list.add(map);
		}

		return list;
	}

}
