package com.yunat.ccms.report.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.node.biz.evaluate.NodeEvaluate;
import com.yunat.ccms.node.biz.evaluate.NodeEvaluateRepository;
import com.yunat.ccms.node.biz.sms.ExecutionRecord;
import com.yunat.ccms.node.biz.sms.NodeSMSQuery;
import com.yunat.ccms.report.domain.EvaluateReportDayResult;
import com.yunat.ccms.report.domain.EvaluateReportResult;
import com.yunat.ccms.report.repository.EvaluateResultSetRepository;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.repository.LogSubjobDao;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.repository.ConnectRepository;

/**
 * 1.效果评估汇总 2.按日效果评估 3.按小时效果评估
 * 
 * @author yin
 * 
 */

@Service
public class EvaluateReportService {

	private Logger logger = LoggerFactory.getLogger(EvaluateReportService.class);

	@Autowired
	LogSubjobDao logSubjobDao;

	@Autowired
	NodeSMSQuery nodeSMSQuery;

	@Autowired
	ConnectRepository connectRepository;

	@Autowired
	NodeEvaluateRepository nodeEvaluateRepository;

	@Autowired
	EvaluateResultSetRepository evaluateResultSetRepository;

	@Transactional(readOnly = true)
	public Map<String, Object> getCollectDataByTotal(Long jobId, Long nodeId) throws Exception {
		Map<String, Object> resultSet = new HashMap<String, Object>();

		LogSubjob logSubjob = getPreChannelNodeEntity(jobId, nodeId);

		ExecutionRecord executionRecord = nodeSMSQuery.findByNodeIdAndSubjobId(logSubjob.getNode().getId(),
				logSubjob.getSubjobId());

		// 评估开始时间(前一个渠道节点的发送时间)
		Date evaluateStartTime = executionRecord.getCreatedTime();
		// 评估结束时间
		Date evaluateEndTime = getEvaluateEndTime(nodeId, evaluateStartTime);

		Long payCustomerCount = evaluateResultSetRepository.findCollectByTotal(jobId, nodeId);

		List<EvaluateReportDayResult> record = evaluateResultSetRepository.findCollectByDay(jobId, nodeId,
				evaluateStartTime, evaluateEndTime);

		// 渠道发送人数
		Long channelSendCount = executionRecord.getValidPhoneAmount();
		resultSet.put("channelSendCount", channelSendCount);
		// 费用 (单价*发送条数)
		Double channelTotalFee = executionRecord.getSendingPrice() * executionRecord.getSendingTotalNum();
		resultSet.put("totalFee", new DecimalFormat("#0.00").format(channelTotalFee));

		Long productCount = 0L;

		Double payPaymentSum = 0.0;

		for (EvaluateReportDayResult evaluateReportDayResult : record) {
			productCount += evaluateReportDayResult.getProductCount();
			payPaymentSum += evaluateReportDayResult.getPayPaymentSum();
		}
		// 购买客户数
		resultSet.put("payCustomerCount", payCustomerCount);
		// 出售商品
		resultSet.put("productCount", productCount);
		// 销售额
		resultSet.put("payPaymentSum", new DecimalFormat("#0.00").format(payPaymentSum));
		// 响应率(如果渠道发送人数为0 响应率应该没有意义)
		BigDecimal b1 = new BigDecimal(Double.toString(payCustomerCount));
		BigDecimal b2 = new BigDecimal(Double.toString(channelSendCount));

		resultSet.put("responseRate",
				channelSendCount != 0L ? new DecimalFormat("#0.00").format(b1.doubleValue() / b2.doubleValue() * 100)
						: null);
		// ROI
		resultSet.put("roi",
				channelTotalFee != 0 ? (1 + ":" + new DecimalFormat("#0.00").format(payPaymentSum / channelTotalFee))
						: (0 + ":" + new DecimalFormat("#0.00").format(payPaymentSum)));
		return resultSet;
	}

	// TODO 没有记录的时间点需要补全表格数据为0
	@Transactional(readOnly = true)
	public List<EvaluateReportDayResult> getCollectDataByDay(Long jobId, Long nodeId) throws Exception {

		LogSubjob logSubjob = getPreChannelNodeEntity(jobId, nodeId);

		ExecutionRecord executionRecord = nodeSMSQuery.findByNodeIdAndSubjobId(logSubjob.getNode().getId(),
				logSubjob.getSubjobId());
		// 费用 (单价*发送条数)
		Double channelTotalFee = executionRecord.getSendingPrice() * executionRecord.getSendingTotalNum();

		// 评估开始时间(前一个渠道节点的发送时间)
		Date evaluateStartTime = executionRecord.getCreatedTime();
		// 评估结束时间
		Date evaluateEndTime = getEvaluateEndTime(nodeId, evaluateStartTime);

		logger.info("查找评估节点按天汇总的表格数据开始");
		List<EvaluateReportDayResult> record = evaluateResultSetRepository.findCollectByDay(jobId, nodeId,
				evaluateStartTime, evaluateEndTime);
		logger.info("查找评估节点按天汇总的表格数据结束");

		for (EvaluateReportDayResult evaluateReportDayResult : record) {

			evaluateReportDayResult.setBuyPaymentSum(Double.valueOf(new DecimalFormat("#0.00")
					.format(evaluateReportDayResult.getBuyPaymentSum())));

			evaluateReportDayResult.setPayPaymentSum(Double.valueOf(new DecimalFormat("#0.00")
					.format(evaluateReportDayResult.getPayPaymentSum())));

			evaluateReportDayResult.setCustomerAvgFee(evaluateReportDayResult.getPayCustomerCount() != 0L ? Double
					.valueOf(new DecimalFormat("#0.00").format(evaluateReportDayResult.getPayPaymentSum()
							/ evaluateReportDayResult.getPayCustomerCount())) : 0);

			evaluateReportDayResult.setRoi(channelTotalFee != 0 ? (1 + ":" + new DecimalFormat("#0.00")
					.format(evaluateReportDayResult.getPayPaymentSum() / channelTotalFee))
					: (0 + ":" + new DecimalFormat("#0.00").format(evaluateReportDayResult.getPayPaymentSum())));
		}

		return record;

	}

	@Transactional(readOnly = true)
	public List<EvaluateReportResult> getCollectDataByHour(Long jobId, Long nodeId, String evaluateTime)
			throws Exception {

		logger.info("查找评估节点按小时汇总的表格数据开始");
		List<EvaluateReportResult> resultSet = evaluateResultSetRepository.findCollectByHour(jobId, nodeId,
				evaluateTime);
		for (EvaluateReportResult evaluateReportResult : resultSet) {
			evaluateReportResult.setBuyPaymentSum(Double.valueOf(new DecimalFormat("#0.00").format(evaluateReportResult
					.getBuyPaymentSum())));

			evaluateReportResult.setPayPaymentSum(Double.valueOf(new DecimalFormat("#0.00").format(evaluateReportResult
					.getPayPaymentSum())));

		}
		logger.info("查找评估节点按小时汇总的表格数据结束");
		return resultSet;
	}

	/**
	 * @param jobId
	 *            :批次jobID
	 * @param nodeId
	 *            :评估节点ID
	 * @return
	 */
	private LogSubjob getPreChannelNodeEntity(Long jobId, Long nodeId) {
		List<Connect> listConnect = connectRepository.findByTargetNodeId(nodeId);
		// 业务上评估节点 前面有且仅有一个渠道节点
		Long preChannelNodeId = listConnect.get(0).getSource();
		LogSubjob logSubjob = logSubjobDao.getSubjobByJobidAndNodeId(jobId, preChannelNodeId);
		return logSubjob;
	}

	/**
	 * @param nodeId
	 *            评估节点ID
	 * @param evaluateStartTime
	 *            前面渠道节点发送时间
	 * @return
	 * @throws Exception
	 */
	private Date getEvaluateEndTime(Long nodeId, Date evaluateStartTime) throws Exception {
		NodeEvaluate nodeEvaluate = nodeEvaluateRepository.findByNodeId(nodeId);
		Integer evaluateCycle = nodeEvaluate.getEvaluateCycle();
		// 评估结束时间
		Date evaluateEndTime = DateUtils.addDay(evaluateStartTime, evaluateCycle);
		return evaluateEndTime;
	}
}
