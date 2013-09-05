package com.yunat.ccms.node.spi.support;

import java.util.Date;

public interface NodeValidationContext {

	/**
	 * 当前节点ID
	 * 
	 * @return
	 */
	Long getNodeId();

	/**
	 * 当前节点的运行参考时间
	 * 
	 * @return
	 */
	Date getReferenceTime();

}
