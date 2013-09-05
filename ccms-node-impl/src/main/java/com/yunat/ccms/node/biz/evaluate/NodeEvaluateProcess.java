package com.yunat.ccms.node.biz.evaluate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.AbstractNodeProcessor;
import com.yunat.ccms.node.biz.sms.ExecutionRecord;
import com.yunat.ccms.node.biz.sms.ExecutionRecordRepository;
import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeInput;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessingException;
import com.yunat.ccms.node.support.NodeSQLExecutor;
import com.yunat.ccms.node.support.service.EvaluateReportDownloadService;

/**
 * 
 * 1.需找要评估的对象 2.对象与订单进行join 查出各个口径的指标。存表保存。 3.每一个固定的时间段内 (前面短信的结束时间-
 * 评估最终时间)每12个小时调用这个process，对存表的数据进行更新(根据jobId 和nodeId先删再插 )。
 * 
 * 
 * 效果评估节点后台流程
 * 
 * @author yin
 * 
 */
@Component
public class NodeEvaluateProcess extends AbstractNodeProcessor<NodeEvaluate> {

	private Logger logger = LoggerFactory.getLogger(NodeEvaluateProcess.class);

	@Autowired
	NodeEvaluateRepository nodeEvaluateRepository;

	@Autowired
	EvaluateReportDownloadService evaluateReportDownloadService;

	@Autowired
	ExecutionRecordRepository executionRecordRepository;

	@Autowired
	private NodeSQLExecutor nodeSQLExecutor;

	private static final String hourTableName = "evaluate_report_result";
	private static final String dayTableName = "evaluate_report_day_result";
	private static final String totalTableName = "evaluate_report_total_result";

	@Override
	public NodeOutput process(final NodeEvaluate nodeEvaluate, final NodeProcessingContext context)
			throws NodeProcessingException {

		NodeEvaluateParamObject nodeEvaluateParamObject = new NodeEvaluateParamObject();

		Long jobId = context.getJobId();
		Long nodeId = context.getNodeId();
		Long subjobId = context.getSubjobId();

		NodeInput nodeInput = context.getNodeInput();

		if (nodeInput.getInputNodes().size() != 1) {
			// 获取评估节点的对象(评估对象:它前面的父亲渠道节点) 业务上评估节点 前面有且仅有一个渠道节点。
			// 不存在多个渠道节点同时链入一个评估节点。
			logger.info("效果评估节点前置输入节点不唯一异常！");
		} else {

			// 正式执行才跑报表节点process
			// if (!context.isTestExecute()) {
			Long preNodeId = nodeInput.getInputNodes().get(0);
			Long preNodeSubjobId = obtainPreNodeSubjobId(jobId, preNodeId);
			NodeData nodeData = nodeInput.getInputData(preNodeId);

			// 获取评估对象(渠道节点输出的临时表)
			String previousNodeOutput = nodeData.getCode();

			final ExecutionRecord executionRecord = obtainPreChannelNodeExecutionRecord(preNodeId, preNodeSubjobId);

			// 前一个短信节点发送费用
			Double channelSendFee = executionRecord.getSendingTotalNum() * executionRecord.getSendingPrice();

			String shopId = nodeEvaluate.getShopId();

			// 每天跑评估节点的时间区间(前一个节点的短信发送时间 - 当前时间 )
			Date evaluateStartDate = executionRecord.getCreatedTime();

			Date evaluateEndDate = new Date();

			nodeEvaluateParamObject.setJobId(jobId);
			nodeEvaluateParamObject.setNodeId(nodeId);
			nodeEvaluateParamObject.setPreviousNodeOutput(previousNodeOutput);
			nodeEvaluateParamObject.setStartDate(evaluateStartDate);
			nodeEvaluateParamObject.setEndDate(evaluateEndDate);
			nodeEvaluateParamObject.setShopId(shopId);

			generateHourScript(nodeEvaluateParamObject, channelSendFee);

			generateDayScript(nodeEvaluateParamObject, channelSendFee);

			generateTotalScript(nodeEvaluateParamObject);

			// 生成客户明细记录
			logger.info("删除老的客户明细记录开始.................................................");
			deleteCustomerDetailTable(nodeEvaluateParamObject);
			logger.info("删除老的客户明细记录结束.................................................");

			logger.info("生成客户明细记录开始----------------------------------------------------");
			insertCustomerDetailTable(nodeEvaluateParamObject);
			evaluateReportDownloadService.builderCustomerCsvFile(subjobId, nodeEvaluateParamObject);
			logger.info("生成客户明细记录结束----------------------------------------------------");

			// 生成商品明细记录
			logger.info("删除老的商品明细记录开始.................................................");
			deleteProductDetailTable(nodeEvaluateParamObject);
			logger.info("删除老的商品明细记录结束.................................................");

			logger.info("生成商品明细记录开始----------------------------------------------------");
			insertProductDetailTable(nodeEvaluateParamObject);
			evaluateReportDownloadService.builderProductCsvFile(subjobId, nodeEvaluateParamObject);
			logger.info("生成商品明细记录结束----------------------------------------------------");

			// 生成订单明细记录
			logger.info("删除老的订单明细记录开始.................................................");
			deleteOrderDetailTable(nodeEvaluateParamObject);
			logger.info("删除老的订单明细记录结束.................................................");

			logger.info("生成订单明细记录开始----------------------------------------------------");
			insertOrderDetailTable(nodeEvaluateParamObject);
			evaluateReportDownloadService.builderOrderCsvFile(subjobId, nodeEvaluateParamObject);
			logger.info("生成订单明细记录结束----------------------------------------------------");

		}

		// }

		return null;

	}

	private Long obtainPreNodeSubjobId(Long jobId, Long nodeId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String sql = " select subjob_id   from twf_log_subjob where job_id=:jobId and node_id =:nodeId";
		paramMap.put("jobId", jobId);
		paramMap.put("nodeId", nodeId);
		return nodeSQLExecutor.queryForLong(sql, paramMap);
	}

	private ExecutionRecord obtainPreChannelNodeExecutionRecord(Long nodeId, Long subjobId) {

		List<ExecutionRecord> executionRecordList = executionRecordRepository
				.findByNodeIdAndSubjobIdOrderByCreatedTimeAsc(nodeId, subjobId);
		if (CollectionUtils.isEmpty(executionRecordList)) {
			return null;
		}
		return executionRecordList.get(0);
	}

	/**
	 * 插入整个周期内汇总的评估结果表
	 */

	private void generateTotalScript(NodeEvaluateParamObject nodeEvaluateParamObject) throws DataAccessException {

		String cleanStr = generateCleanScript(totalTableName);

		Map<String, Object> cleanParameters = setCleanScriptParam(nodeEvaluateParamObject);
		logger.info("评估报表汇总结果表清理数据的sql");
		nodeSQLExecutor.update(cleanStr, cleanParameters);

		generateTotalTargetScript(nodeEvaluateParamObject);

	}

	/**
	 * 插入按天汇总的评估结果表
	 * 
	 * @param nodeEvaluateParamObject
	 * @param channelSendFee
	 * @throws DataAccessException
	 */
	private void generateDayScript(NodeEvaluateParamObject nodeEvaluateParamObject, Double channelSendFee)
			throws DataAccessException {

		String cleanStr = generateCleanScript(dayTableName);

		Map<String, Object> cleanParameters = setCleanScriptParam(nodeEvaluateParamObject);
		logger.info("评估报表按天结果表清理数据的sql");
		nodeSQLExecutor.update(cleanStr, cleanParameters);

		String orderStr = generateSwellOrderDayScript(nodeEvaluateParamObject.getPreviousNodeOutput());

		String payStr = generatePayDayScript(nodeEvaluateParamObject.getPreviousNodeOutput(), channelSendFee);

		String targetDayStr = generateTargetScript(dayTableName, orderStr, payStr);
		Map<String, Object> parameters = setScriptParam(nodeEvaluateParamObject);

		logger.info("评估报表按天结果表插数据参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId() + " startDate: " + nodeEvaluateParamObject.getStartDate()
				+ " endDate: " + nodeEvaluateParamObject.getEndDate() + " shopId: "
				+ nodeEvaluateParamObject.getShopId());
		nodeSQLExecutor.update(targetDayStr, parameters);

	}

	/**
	 * 
	 * 插入按小时汇总的评估结果表
	 * 
	 * @param jobId
	 * @param previousNodeOutput
	 * @param startDate
	 * @param endDate
	 * @param shopId
	 * @throws DataAccessException
	 */
	private void generateHourScript(NodeEvaluateParamObject nodeEvaluateParamObject, Double channelSendFee)
			throws DataAccessException {

		String cleanStr = generateCleanScript(hourTableName);

		Map<String, Object> cleanParameters = setCleanScriptParam(nodeEvaluateParamObject);
		logger.info("评估报表小时结果表清理数据的sql");
		nodeSQLExecutor.update(cleanStr, cleanParameters);

		String orderStr = generateSwellOrderHourScript(nodeEvaluateParamObject.getPreviousNodeOutput());

		String payStr = generatePayHourScript(nodeEvaluateParamObject.getPreviousNodeOutput(), channelSendFee);

		String targetHourStr = generateTargetScript(hourTableName, orderStr, payStr);
		Map<String, Object> parameters = setScriptParam(nodeEvaluateParamObject);

		logger.info("评估报表小时结果表插数据参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId() + " startDate: " + nodeEvaluateParamObject.getStartDate()
				+ " endDate: " + nodeEvaluateParamObject.getEndDate() + " shopId: "
				+ nodeEvaluateParamObject.getShopId());

		nodeSQLExecutor.update(targetHourStr, parameters);
	}

	/**
	 * 按天生成拍下订单脚本(1.订单数 不需要按人-店-天合并 2.人数进行剔重)
	 * 
	 * 
	 * 
	 * @param orderStr
	 */
	private String generateSwellOrderDayScript(String previousNodeOutput) {
		StringBuffer orderStr = new StringBuffer();
		orderStr.append("   select " + "   DATE_FORMAT(o.created , '%Y-%m-%d') AS time,  "
				+ "   count(o.tid) AS buy_order_count, " + "   count(DISTINCT o.customerno) AS buy_customer_count,   "
				+ "   ifnull(sum(o.total_fee), 0) AS buy_payment_sum  " + "   from  " + "   " + previousNodeOutput
				+ "   p ,  plt_taobao_order  o      " + "   where  p.uni_id = CONCAT('taobao|',o.customerno)  "
				+ "   and o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ "   and o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H')  " + "   and o.dp_id = :shopId  "
				+ "   GROUP BY DATE_FORMAT(o.created , '%Y-%m-%d')  "
				+ "   ORDER BY DATE_FORMAT(o.created , '%Y-%m-%d')  ");

		return orderStr.toString();
	}

	/**
	 * 往汇总表插入数据
	 * 
	 * @param nodeEvaluateParamObject
	 */
	private void generateTotalTargetScript(NodeEvaluateParamObject nodeEvaluateParamObject) {

		StringBuffer payStr = new StringBuffer();
		payStr.append(" insert into  evaluate_report_total_result (job_id, node_id,pay_customer_count,pay_payment_sum,product_count)  ");
		payStr.append(" select :jobId as job_id, :nodeId as node_id,   ");
		payStr.append(" count(DISTINCT o.customerno) AS buy_customer_count,   ");
		payStr.append(" sum(ifnull(o.payment, 0)) AS buy_payment_sum,   ");
		payStr.append(" ifnull(sum(o.num), 0)  as  product_number   ");

		payStr.append(" from  " + nodeEvaluateParamObject.getPreviousNodeOutput() + " p  ,  plt_taobao_order  o    ");

		payStr.append("	where  p.uni_id = CONCAT('taobao|',o.customerno)  ");
		payStr.append("  and o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  ");
		payStr.append("  and o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H')  ");
		payStr.append("  and o.ccms_order_status  in  (21,22,23)  " + "   and o.dp_id = :shopId  " + "   ");

		Map<String, Object> parameters = setScriptParam(nodeEvaluateParamObject);

		logger.info("评估报表汇总结果表插数据参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId() + " startDate: " + nodeEvaluateParamObject.getStartDate()
				+ " endDate: " + nodeEvaluateParamObject.getEndDate() + " shopId: "
				+ nodeEvaluateParamObject.getShopId());
		nodeSQLExecutor.update(payStr.toString(), parameters);

	}

	/**
	 * 按天生成付款订单脚本(1.订单数不需要按人-店-天合并 2.人数进行剔重)
	 * 
	 * @param payStr
	 */
	private String generatePayDayScript(String previousNodeOutput, Double channelSendFee) {
		StringBuffer payStr = new StringBuffer();
		payStr.append("   select " + "   DATE_FORMAT(o.created, '%Y-%m-%d') AS time,  "
				+ "   count(o.tid) AS buy_order_count, " + "   count(DISTINCT o.customerno) AS buy_customer_count,   "
				+ "   sum(ifnull(o.payment, 0)) AS buy_payment_sum,  "
				+ "   CASE WHEN count(DISTINCT o.customerno) > 0 THEN  sum(o.payment) / count(DISTINCT o.customerno)  "
				+ "   ELSE NULL END AS  cust_avg_fee , " + "   ifnull(sum(o.num), 0)  as  product_number, "
				+ "   concat(" + channelSendFee + "   ,':' ,sum(ifnull(o.payment, 0)))  AS roi  "

				+ "   from  " + "   " + previousNodeOutput + "  p ,  plt_taobao_order  o      "
				+ "   where  p.uni_id = CONCAT('taobao|',o.customerno)  "
				+ "   and o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ "   and o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H')  "
				+ "   and o.ccms_order_status  in  (21,22,23)  " + "   and o.dp_id = :shopId  "
				+ "   GROUP BY DATE_FORMAT(o.created, '%Y-%m-%d')  "
				+ "   ORDER BY DATE_FORMAT(o.created, '%Y-%m-%d')  ");

		return payStr.toString();
	}

	/**
	 * 按小时生成拍下订单脚本(1.订单数不需要按人-店-天合并 2.人数进行剔重)
	 * 
	 * 
	 * 
	 * @param orderStr
	 */
	private String generateSwellOrderHourScript(String previousNodeOutput) {
		StringBuffer orderStr = new StringBuffer();
		orderStr.append("   select " + "   DATE_FORMAT(o.created , '%Y-%m-%d %H') AS time,  "
				+ "   count(o.tid) AS buy_order_count, " + "   count(DISTINCT o.customerno) AS buy_customer_count,   "
				+ "   ifnull(sum(o.total_fee), 0) AS buy_payment_sum  " + "   from  " + "   " + previousNodeOutput
				+ "   p ,  plt_taobao_order  o      " + "   where  p.uni_id = CONCAT('taobao|',o.customerno)  "
				+ "   and o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ "   and o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H')  " + "   and o.dp_id = :shopId  "
				+ "   GROUP BY DATE_FORMAT(o.created , '%Y-%m-%d %H')  "
				+ "   ORDER BY DATE_FORMAT(o.created , '%Y-%m-%d %H')  ");

		return orderStr.toString();
	}

	/**
	 * 按小时生成付款订单脚本(1.订单数不需要按人-店-天合并 2.人数进行剔重)
	 * 
	 * @param payStr
	 */
	private String generatePayHourScript(String previousNodeOutput, Double channelSendFee) {
		StringBuffer payStr = new StringBuffer();
		payStr.append("   select " + "   DATE_FORMAT(o.created, '%Y-%m-%d %H') AS time,  "
				+ "   count(o.tid) AS buy_order_count, " + "   count(DISTINCT o.customerno) AS buy_customer_count,   "
				+ "   sum(ifnull(o.payment, 0)) AS buy_payment_sum,  "
				+ "   CASE WHEN count(DISTINCT o.customerno) > 0 THEN  sum(o.payment) / count(DISTINCT o.customerno)  "
				+ "   ELSE NULL END AS  cust_avg_fee , " + "   ifnull(sum(o.num), 0)  as  product_number, "
				+ "   concat(" + channelSendFee + "   ,':' ,sum(ifnull(o.payment, 0)))  AS roi  "

				+ "   from  " + "   " + previousNodeOutput + "  p ,  plt_taobao_order  o      "
				+ "   where  p.uni_id = CONCAT('taobao|',o.customerno)  "
				+ "   and o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ "   and o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H')  "
				+ "   and o.ccms_order_status  in  (21,22,23)  " + "   and o.dp_id = :shopId  "
				+ "   GROUP BY DATE_FORMAT(o.created, '%Y-%m-%d %H')  "
				+ "   ORDER BY DATE_FORMAT(o.created, '%Y-%m-%d %H')  ");

		return payStr.toString();
	}

	/**
	 * 清理脚本(清理同一个subjob下的前面跑的记录)
	 */
	private String generateCleanScript(String tableName) {
		StringBuffer cleanStr = new StringBuffer();
		cleanStr.append(" delete  from  " + tableName + "  where job_id =:jobId and node_id = :nodeId    ");
		return cleanStr.toString();
	}

	/**
	 * 设置清空脚本的参数
	 * 
	 * @param nodeEvaluateParamObject
	 * @return
	 */
	private Map<String, Object> setCleanScriptParam(NodeEvaluateParamObject nodeEvaluateParamObject) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jobId", nodeEvaluateParamObject.getJobId());
		parameters.put("nodeId", nodeEvaluateParamObject.getNodeId());
		return parameters;
	}

	/**
	 * 
	 * 设置最终插入脚本的参数
	 * 
	 * @param jobId
	 * @param previousNodeOutput
	 * @param startDate
	 * @param endDate
	 * @param shopId
	 * @return
	 */
	private Map<String, Object> setScriptParam(NodeEvaluateParamObject nodeEvaluateParamObject) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jobId", nodeEvaluateParamObject.getJobId());
		parameters.put("nodeId", nodeEvaluateParamObject.getNodeId());
		parameters.put("startDate", nodeEvaluateParamObject.getStartDate());
		parameters.put("endDate", nodeEvaluateParamObject.getEndDate());
		parameters.put("shopId", nodeEvaluateParamObject.getShopId());
		return parameters;
	}

	/**
	 * 生成最终的脚本
	 * 
	 * @param orderStr
	 * @param payStr
	 * @param targetStr
	 */
	private String generateTargetScript(String tableName, String orderStr, String payStr) {
		StringBuffer targetHourStr = new StringBuffer();
		targetHourStr

				.append("  insert into  "
						+ tableName
						+ "(job_id, node_id,evaluate_time,buy_order_count,buy_customer_count,buy_payment_sum,"
						+ " pay_order_count ,pay_customer_count, pay_payment_sum, customer_avg_fee, product_count, roi   )  "
						+ " select :jobId as job_id, :nodeId as node_id, t.time,"
						+ " ifnull(t.buy_order_count,0) buy_order_count, ifnull(t.buy_customer_count,0) buy_customer_count ,"
						+ " ifnull(t.buy_payment_sum ,0) buy_payment_sum, "
						+ " ifnull(p.buy_order_count ,0) pay_order_count ,ifnull(p.buy_customer_count ,0) pay_customer_count,"
						+ " ifnull(p.buy_payment_sum ,0) pay_payment_sum ,ifnull(p.cust_avg_fee ,0) pay_cust_avg_fee,ifnull(p.product_number ,0) pay_product_number,ifnull(p.roi,0) pay_roi  "
						+ " from  (" + orderStr + ") as t  LEFT JOIN (" + payStr + ") as p    "
						+ " on t.time  =  p.time  ");
		return targetHourStr.toString();
	}

	/**
	 * 删除老的客户明细信息
	 */
	public int deleteCustomerDetailTable(NodeEvaluateParamObject nodeEvaluateParamObject) {
		StringBuffer cleanStr = new StringBuffer();
		cleanStr.append(" delete  from  twf_node_evaluate_customer_detail  where job_id =:jobId and node_id = :nodeId    ");
		Map<String, Object> cleanParameters = new HashMap<String, Object>();
		cleanParameters.put("jobId", nodeEvaluateParamObject.getJobId());
		cleanParameters.put("nodeId", nodeEvaluateParamObject.getNodeId());
		logger.info("参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId());
		return nodeSQLExecutor.update(cleanStr.toString(), cleanParameters);
	}

	/**
	 * 生成相应的客户信息记录(针对的是付款的客户)
	 */
	private String generateCustomerDetailScript(String previousNodeOutput) {

		StringBuffer sb = new StringBuffer();
		sb.append(" select ");
		sb.append(" null,");
		sb.append(" :jobId as jobId,");
		sb.append(" :nodeId  as nodeId, ");
		sb.append(" date_format(o.created,'%Y-%m-%d') AS time, ");
		sb.append(" c.customerno, ");
		sb.append(" c.full_name, ");
		sb.append(" CASE WHEN  c.sex = 'm' THEN '男'   WHEN c.sex = 'f' THEN '女'  ELSE '未知' END as sex, ");
		sb.append(" c.birthday, ");
		sb.append(" c.state, ");
		sb.append(" c.city, ");
		sb.append(" c.district, ");
		// 客户全站等级
		sb.append(" CASE WHEN c.vip_info ='c'  THEN '普通会员'   ");
		sb.append(" WHEN c.vip_info = 'asso_vip' THEN '荣誉会员' ");
		sb.append(" ELSE upper(c.vip_info)  END  AS  vip_info, ");
		sb.append(" c.mobile, ");
		sb.append(" c.email, ");
		// 买家信用等级
		sb.append(" d.show_name, ");
		// 买家好评率
		sb.append(" ifnull(CASE WHEN  c.buyer_credit_total_num >0  then c.buyer_credit_good_num/c.buyer_credit_total_num   ELSE NULL END ,0),");
		sb.append(" c.address, ");
		sb.append(" c.zip, ");
		sb.append(" c.job ");

		sb.append(" from  "
				+ previousNodeOutput
				+ "  p, plt_taobao_order o, plt_taobao_customer  c  LEFT OUTER JOIN    tm_dic_value  d  ON   c.buyer_credit_lev  = d.dic_value  and d.dic_id = 42   ");

		sb.append(" where p.uni_id = CONCAT('taobao|', o.customerno) ");
		sb.append(" and   o.customerno  = c.customerno ");
		sb.append(" and o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ " and o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H') ");
		sb.append(" and o.dp_id IN (:shopId) ");
		sb.append(" and o.ccms_order_status  in  (21,22,23) ");

		sb.append(" group by  date_format(o.created,'%Y-%m-%d'),c.customerno ");
		sb.append(" order by  date_format(o.created,'%y-%m-%d'); ");

		return sb.toString();

	}

	private void insertCustomerDetailTable(NodeEvaluateParamObject nodeEvaluateParamObject) {

		StringBuffer sb = new StringBuffer();
		String customerDetailScript = generateCustomerDetailScript(nodeEvaluateParamObject.getPreviousNodeOutput());

		sb.append(" insert into  twf_node_evaluate_customer_detail ");
		sb.append(" " + customerDetailScript + " ");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jobId", nodeEvaluateParamObject.getJobId());
		parameters.put("nodeId", nodeEvaluateParamObject.getNodeId());
		parameters.put("startDate", nodeEvaluateParamObject.getStartDate());
		parameters.put("endDate", nodeEvaluateParamObject.getEndDate());
		parameters.put("shopId", nodeEvaluateParamObject.getShopId());

		logger.info("参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId() + " startDate: " + nodeEvaluateParamObject.getStartDate()
				+ " endDate: " + nodeEvaluateParamObject.getEndDate() + " shopId: "
				+ nodeEvaluateParamObject.getShopId());

		nodeSQLExecutor.update(sb.toString(), parameters);

	}

	/**
	 * 删除老的商品明细信息
	 */
	public int deleteProductDetailTable(NodeEvaluateParamObject nodeEvaluateParamObject) {
		StringBuffer cleanStr = new StringBuffer();
		cleanStr.append(" delete  from  twf_node_evaluate_product_detail  where job_id =:jobId and node_id = :nodeId    ");
		Map<String, Object> cleanParameters = new HashMap<String, Object>();
		cleanParameters.put("jobId", nodeEvaluateParamObject.getJobId());
		cleanParameters.put("nodeId", nodeEvaluateParamObject.getNodeId());
		logger.info("参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId());
		return nodeSQLExecutor.update(cleanStr.toString(), cleanParameters);
	}

	/**
	 * 生成相应的商品信息记录(针对的是已经付款购买的)
	 * 
	 */
	private String generateProductDetailScript(String previousNodeOutput) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select  ");
		sb.append(" null , ");
		sb.append(" :jobId as jobId,");
		sb.append(" :nodeId  as nodeId, ");
		sb.append(" date_format(o.created,'%Y-%m-%d') AS time, ");
		sb.append(" p.num_iid, ");
		sb.append(" p.title, ");

		// 成交客人数
		sb.append(" count(distinct i.customerno), ");
		// 成交订单数
		sb.append(" count(distinct i.tid), ");
		// 成交数量
		sb.append(" sum(i.num), ");
		// 成交金额
		sb.append(" sum(i.payment), ");

		sb.append(" p.outer_id ");

		sb.append(" from  " + previousNodeOutput
				+ "  n, plt_taobao_order o, plt_taobao_order_item i ,plt_taobao_product  p ");

		sb.append(" where n.uni_id = CONCAT('taobao|', o.customerno) ");
		sb.append(" and   o.tid  = i.tid ");
		sb.append(" and   i.num_iid  = p.num_iid ");
		sb.append(" and   o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ " and   o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H') ");
		sb.append(" and   o.dp_id IN (:shopId) ");
		sb.append(" and   o.ccms_order_status  in  (21,22,23) ");

		sb.append(" group by date_format(o.created,'%Y-%m-%d'),p.num_iid, p.title,p.outer_id ");
		sb.append(" order by date_format(o.created,'%y-%m-%d'); ");

		return sb.toString();
	}

	private void insertProductDetailTable(NodeEvaluateParamObject nodeEvaluateParamObject) {
		StringBuffer sb = new StringBuffer();
		String productDetailScript = generateProductDetailScript(nodeEvaluateParamObject.getPreviousNodeOutput());

		sb.append(" insert into  twf_node_evaluate_product_detail  ");
		sb.append(" " + productDetailScript + " ");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jobId", nodeEvaluateParamObject.getJobId());
		parameters.put("nodeId", nodeEvaluateParamObject.getNodeId());
		parameters.put("startDate", nodeEvaluateParamObject.getStartDate());
		parameters.put("endDate", nodeEvaluateParamObject.getEndDate());
		parameters.put("shopId", nodeEvaluateParamObject.getShopId());

		logger.info("参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId() + " startDate: " + nodeEvaluateParamObject.getStartDate()
				+ " endDate: " + nodeEvaluateParamObject.getEndDate() + " shopId: "
				+ nodeEvaluateParamObject.getShopId());

		nodeSQLExecutor.update(sb.toString(), parameters);
	}

	/**
	 * 删除老的订单明细信息
	 */
	public int deleteOrderDetailTable(NodeEvaluateParamObject nodeEvaluateParamObject) {
		StringBuffer cleanStr = new StringBuffer();
		cleanStr.append(" delete  from  twf_node_evaluate_order_detail  where job_id =:jobId and node_id = :nodeId    ");
		Map<String, Object> cleanParameters = new HashMap<String, Object>();
		cleanParameters.put("jobId", nodeEvaluateParamObject.getJobId());
		cleanParameters.put("nodeId", nodeEvaluateParamObject.getNodeId());

		logger.info("参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId());

		return nodeSQLExecutor.update(cleanStr.toString(), cleanParameters);
	}

	/**
	 * 生成相应的订单信息记录(针对的是订购的而非购买的)
	 */
	private String generateOrderDetailScript(String previousNodeOutput) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select ");
		sb.append(" null , ");
		sb.append(" :jobId as jobId,");
		sb.append(" :nodeId  as nodeId, ");
		sb.append(" date_format(o.created,'%Y-%m-%d') AS time, ");
		sb.append(" o.tid, ");
		sb.append(" o.customerno, ");
		sb.append(" o.created, ");
		sb.append(" o.pay_time, ");
		sb.append(" o.consign_time, ");
		sb.append(" o.total_fee, ");
		sb.append(" o.payment, ");
		sb.append(" CASE WHEN o.ccms_order_status = 10 THEN '已下单未付款'    ");
		sb.append(" WHEN o.ccms_order_status = 21 THEN '已付款未发货'  ");
		sb.append(" WHEN o.ccms_order_status = 22 THEN '已发货待确认'  ");
		sb.append(" WHEN o.ccms_order_status = 23 THEN '交易成功'  ");
		sb.append(" ELSE '交易失败' END  ");

		sb.append(" from  " + previousNodeOutput + "  n, plt_taobao_order o  ");

		sb.append(" where n.uni_id = CONCAT('taobao|', o.customerno) ");

		sb.append(" and   o.created >=  DATE_FORMAT(:startDate,'%Y-%m-%d %H')  "
				+ " and   o.created <  DATE_FORMAT(:endDate,'%Y-%m-%d %H') ");

		sb.append(" and   o.dp_id IN (:shopId) ");

		sb.append(" order by date_format(o.created,'%y-%m-%d'); ");

		return sb.toString();

	}

	private void insertOrderDetailTable(NodeEvaluateParamObject nodeEvaluateParamObject) {
		StringBuffer sb = new StringBuffer();
		String orderDetailScript = generateOrderDetailScript(nodeEvaluateParamObject.getPreviousNodeOutput());

		sb.append(" insert into  twf_node_evaluate_order_detail  ");
		sb.append(" " + orderDetailScript + " ");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jobId", nodeEvaluateParamObject.getJobId());
		parameters.put("nodeId", nodeEvaluateParamObject.getNodeId());
		parameters.put("startDate", nodeEvaluateParamObject.getStartDate());
		parameters.put("endDate", nodeEvaluateParamObject.getEndDate());
		parameters.put("shopId", nodeEvaluateParamObject.getShopId());

		logger.info("参数:" + " jobId: " + nodeEvaluateParamObject.getJobId() + " nodeId: "
				+ nodeEvaluateParamObject.getNodeId() + " startDate: " + nodeEvaluateParamObject.getStartDate()
				+ " endDate: " + nodeEvaluateParamObject.getEndDate() + " shopId: "
				+ nodeEvaluateParamObject.getShopId());

		nodeSQLExecutor.update(sb.toString(), parameters);
	}
}
