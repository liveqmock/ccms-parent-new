package com.yunat.ccms.node.biz.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NodeTimeCommandImpl implements NodeTimeCommand {
	
	@Autowired
	private NodeTimeRepository nodeTimeRepository;
	
	@Override
	public void saveTimeNode(NodeTime nodeTime) {
		nodeTimeRepository.saveOrUpdate(nodeTime);
	}

	@Override
	public void deleteById(Long nodeId) {
		nodeTimeRepository.deleteById(nodeId);
	}

}
