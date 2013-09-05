package com.yunat.ccms.node.biz.time;

public interface NodeTimeCommand {
	
	void saveTimeNode(NodeTime nodeTime);

	void deleteById(Long nodeId);

}