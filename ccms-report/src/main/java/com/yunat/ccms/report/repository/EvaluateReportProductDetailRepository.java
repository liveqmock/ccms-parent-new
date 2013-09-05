package com.yunat.ccms.report.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.yunat.ccms.report.domain.EvaluateReportProductDetail;

/**
 * 评估节点-商品明细-存储
 * 
 * @author yin
 * 
 */

public interface EvaluateReportProductDetailRepository extends Repository<EvaluateReportProductDetail, Long> {

	List<EvaluateReportProductDetail> findByJobIdAndNodeIdAndEvaluateTime(Long jobId, Long nodeId, String evaluateTime);

	Page<Map<String, Object>> findByJobIdAndNodeIdAndEvaluateTime(Long jobId, Long nodeId, String evaluateTime,
			Pageable pageable);

}
