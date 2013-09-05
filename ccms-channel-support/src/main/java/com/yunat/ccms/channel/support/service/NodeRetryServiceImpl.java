package com.yunat.ccms.channel.support.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.channel.support.domain.NodeRetry;
import com.yunat.ccms.channel.support.repository.NodeRetryRepository;

@Component
public class NodeRetryServiceImpl implements NodeRetryService {

	@Autowired
	private NodeRetryRepository nodeRetryRespository;

	public void saveNodeRetry(NodeRetry nodeRetry) {
		nodeRetryRespository.saveAndFlush(nodeRetry);
	}

	public NodeRetry getNodeRetry(Long jobId, Long nodeId, boolean isTestExecute) {
		List<NodeRetry> list = nodeRetryRespository.findByJobIdAndNodeIdAndIsTestExecute(jobId, nodeId, isTestExecute);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
}
