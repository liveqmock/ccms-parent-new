package com.yunat.ccms.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.NodeEntity;
import com.yunat.ccms.node.spi.NodeInput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessor;
import com.yunat.ccms.node.spi.repository.NodeEntityRepository;
import com.yunat.ccms.node.spi.repository.NodeProcessorRepository;
import com.yunat.ccms.schedule.core.impl.DefaultNodeProcessingContext;
import com.yunat.ccms.schedule.domain.LogSubjob;

/**
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class NodeProcessService {

	@Autowired
	private NodeProcessorRepository processorRepository;

	@Autowired
	private JobDataService jobDataService;

	@Autowired
	private NodeEntityRepository nodeConfigRepository;

	public NodeProcessor getNodeProcessor(String nodeType) {
		return processorRepository.getNodeProcessor(nodeType);
	}

	public NodeProcessingContext createNodeProcessingContext(LogSubjob subjob) {
		NodeInput nodeInput = jobDataService.getNodeInput(subjob);
		return new DefaultNodeProcessingContext(subjob, nodeInput);
	}

	public NodeEntity getNodeEntity(String nodeType, Long nodeId) {
		return nodeConfigRepository.getNodeEntity(nodeType, nodeId);
	}

}
