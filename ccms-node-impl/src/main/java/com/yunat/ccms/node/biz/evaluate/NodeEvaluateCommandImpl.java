package com.yunat.ccms.node.biz.evaluate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NodeEvaluateCommandImpl implements NodeEvaluateCommand {

	@Autowired
	private NodeEvaluateRepository nodeEvaluateRepository;

	@Override
	public void saveEvaluateNode(NodeEvaluate nodeEvaluate) {
		nodeEvaluateRepository.save(nodeEvaluate);

	}

	@Override
	public void deleteById(Long nodeId) {
		nodeEvaluateRepository.delete(nodeId);

	}

}
