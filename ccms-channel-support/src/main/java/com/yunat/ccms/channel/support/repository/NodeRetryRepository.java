package com.yunat.ccms.channel.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.channel.support.domain.NodeRetry;

public interface NodeRetryRepository extends JpaRepository<NodeRetry, Long>{

	List<NodeRetry> findByJobIdAndNodeIdAndIsTestExecute(Long jobId, Long nodeId, boolean isTestExecute);
	
}
