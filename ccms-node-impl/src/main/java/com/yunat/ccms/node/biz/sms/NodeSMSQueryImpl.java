package com.yunat.ccms.node.biz.sms;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NodeSMSQueryImpl implements NodeSMSQuery {

	@Autowired
	private NodeSMSRepository nodeSMSRepository;
	
	@Autowired
	private ExecutionRecordRepository executionRecordRepository;
	
	@Override
	public NodeSMS findByNodeId(Long nodeId) {
		return nodeSMSRepository.findOne(nodeId);
	}

	@Override
	public ExecutionRecord findByNodeIdAndSubjobId(Long nodeId, Long subjobId) {
		List<ExecutionRecord> recordList = executionRecordRepository.findByNodeIdAndSubjobIdOrderByCreatedTimeAsc(nodeId, subjobId);
		if (CollectionUtils.isEmpty(recordList)) {
			return null;
		}
		return recordList.get(0);
	}

}