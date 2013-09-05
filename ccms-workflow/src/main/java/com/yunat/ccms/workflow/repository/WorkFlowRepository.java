package com.yunat.ccms.workflow.repository;

import com.yunat.ccms.workflow.domain.WorkFlow;

public interface WorkFlowRepository {

	WorkFlow findById(Long workflowId);

	WorkFlow findByCampId(Long campId);

	void saveOrUpdate(WorkFlow workflow);

}
