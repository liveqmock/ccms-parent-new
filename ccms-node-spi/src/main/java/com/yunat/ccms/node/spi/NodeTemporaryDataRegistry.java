package com.yunat.ccms.node.spi;

/**
 * 
 * 日志的处理接口
 * 
 */
public interface NodeTemporaryDataRegistry {

	void register(Long jobId, String name, String type);

	void destory(String name);
	
	void destoryNodeMidsLogger(Long jobId, String name);
}