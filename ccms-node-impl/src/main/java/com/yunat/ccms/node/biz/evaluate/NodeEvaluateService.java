package com.yunat.ccms.node.biz.evaluate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评估节点(1.打开 2.保存 业务层)
 * 
 * @author yin
 * 
 */
@Service
public class NodeEvaluateService {

	@Autowired
	NodeEvaluateRepository nodeEvaluateRepository;

	@Transactional(readOnly = true)
	NodeEvaluate findByNodeId(Long nodeId) throws Exception {
		return nodeEvaluateRepository.findByNodeId(nodeId);
	}

	@Transactional
	void saveNode(NodeEvaluate nodeEvaluate) throws Exception {
		nodeEvaluateRepository.save(nodeEvaluate);
	}
}
