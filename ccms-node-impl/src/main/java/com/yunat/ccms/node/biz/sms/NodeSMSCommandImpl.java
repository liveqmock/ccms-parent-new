package com.yunat.ccms.node.biz.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NodeSMSCommandImpl implements NodeSMSCommand {

	@Autowired
	private NodeSMSRepository repository;
	
	@Autowired ExecutionRecordRepository executionRecordRepository;
	
	@Override
	public void saveOrUpdate(NodeSMS newNode) {
		repository.saveAndFlush(newNode);
	}

	@Override
	public void deleteByNodeId(Long nodeId) {
		repository.delete(nodeId);
	}

	@Override
	public void saveExecutionRecord(ExecutionRecord executionRecord) {
		executionRecordRepository.saveAndFlush(executionRecord);
	}

}