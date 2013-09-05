package com.yunat.ccms.workflow.service.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.repository.ConnectRepository;
import com.yunat.ccms.workflow.service.query.ConnectQuery;

@Service
public class ConnectQueryImpl implements ConnectQuery {

	@Autowired
	private ConnectRepository repository;
	
	@Override
	public Connect findById(Long connectId) {
		return repository.findById(connectId);
	}

	@Override
	public List<Connect> findByWorkflowId(Long workflowId) {
		return repository.findByWorkflowId(workflowId);
	}

	@Override
	public List<Connect> findByTargetNodeId(Long targetNodeId) {
		return repository.findByTargetNodeId(targetNodeId);
	}

}