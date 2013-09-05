package com.yunat.ccms.report.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.report.domain.EvaluateReportCustomerDetail;
import com.yunat.ccms.report.domain.EvaluateReportOrderDetail;
import com.yunat.ccms.report.domain.EvaluateReportProductDetail;
import com.yunat.ccms.report.repository.EvaluateReportCustomerDetailRepository;
import com.yunat.ccms.report.repository.EvaluateReportOrderDetailRepository;
import com.yunat.ccms.report.repository.EvaluateReportProductDetailRepository;
import com.yunat.ccms.report.repository.EvaluateResultSetRepository;

/**
 * 评估报告对应明细(客户,订单,商品 服务层)
 * 
 * @author yin
 * 
 */
@Service
public class EvaluateReportCorrespondDetailService {

	@Autowired
	EvaluateResultSetRepository evaluateResultSetRepository;

	@Autowired
	EvaluateReportCustomerDetailRepository nodeEvaluateCustomerDetailRepository;

	@Autowired
	EvaluateReportOrderDetailRepository nodeEvaluateOrderDetailRepository;

	@Autowired
	EvaluateReportProductDetailRepository nodeEvaluateProductDetailRepository;

	@Transactional(readOnly = true)
	public List<EvaluateReportCustomerDetail> getCustomerDetailList(Long jobId, Long nodeId, String evaluateTime) {

		List<EvaluateReportCustomerDetail> list = nodeEvaluateCustomerDetailRepository
				.findByJobIdAndNodeIdAndEvaluateTime(jobId, nodeId, evaluateTime);
		for (EvaluateReportCustomerDetail evaluateReportCustomerDetail : list) {
			this.convertCustomerDate(evaluateReportCustomerDetail);
		}

		return list;

	}

	private EvaluateReportCustomerDetail convertCustomerDate(EvaluateReportCustomerDetail evaluateReportCustomerDetail) {

		evaluateReportCustomerDetail.setGoodRate((new DecimalFormat("#0.00").format(Double
				.valueOf(evaluateReportCustomerDetail.getGoodRate()) * 100)) + "%");

		return evaluateReportCustomerDetail;
	}

	@Transactional(readOnly = true)
	public List<EvaluateReportOrderDetail> getOrderDetailList(Long jobId, Long nodeId, String evaluateTime) {

		List<EvaluateReportOrderDetail> resultSet = nodeEvaluateOrderDetailRepository
				.findByJobIdAndNodeIdAndEvaluateTime(jobId, nodeId, evaluateTime);
		return resultSet;

	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> getOrderItemDetailList(String tid) {
		return evaluateResultSetRepository.findOrderItemByTid(tid);
	}

	@Transactional(readOnly = true)
	public List<EvaluateReportProductDetail> getProductDetailList(Long jobId, Long nodeId, String evaluateTime) {

		return nodeEvaluateProductDetailRepository.findByJobIdAndNodeIdAndEvaluateTime(jobId, nodeId, evaluateTime);
	}
}
