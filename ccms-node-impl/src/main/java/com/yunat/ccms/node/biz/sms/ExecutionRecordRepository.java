package com.yunat.ccms.node.biz.sms;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRecordRepository extends JpaRepository<ExecutionRecord, Serializable> {
	List<ExecutionRecord> findByNodeIdAndSubjobIdOrderByCreatedTimeAsc(Long nodeId, Long subjobId);
}