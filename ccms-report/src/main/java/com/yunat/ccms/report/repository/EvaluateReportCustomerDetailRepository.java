package com.yunat.ccms.report.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.yunat.ccms.report.domain.EvaluateReportCustomerDetail;

/**
 * 评估节点-客户明细-存储
 * 
 * @author yin
 * 
 */

public interface EvaluateReportCustomerDetailRepository extends Repository<EvaluateReportCustomerDetail, Long> {

	List<EvaluateReportCustomerDetail> findByJobIdAndNodeIdAndEvaluateTime(Long jobId, Long nodeId, String evaluateTime);

	Page<Map<String, Object>> findByJobIdAndNodeIdAndEvaluateTime(Long jobId, Long nodeId, String evaluateTime,
			Pageable pageable);

}
