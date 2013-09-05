package com.yunat.ccms.workflow.repository;

import java.util.List;

import com.yunat.ccms.workflow.domain.Connect;

public interface ConnectRepository {

	Connect findById(Long connectId);

	List<Connect> findByWorkflowId(Long workflowId);

	List<Connect> findByTargetNodeId(Long targetNodeId);

	void saveOrUpdate(Connect newConnect);

	void deleteByList(List<Long> deletedConnects);

}
