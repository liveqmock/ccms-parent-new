package com.yunat.ccms.node.spi;

import org.springframework.transaction.annotation.Transactional;

/**
 * 节点处理接口
 * 节点实现须保证是单例的(singleton)
 * 
 * @author xiaojing.qu
 * 
 */
public interface NodeProcessor<T extends NodeEntity> {

	/**
	 * 节点业务处理.
	 * 
	 * @param nodeConfig
	 *            节点配置
	 * @param context
	 *            节点处理上下文
	 * @return
	 * @throws NodeProcessingException
	 *             节点处理异常
	 */
	@Transactional
	public NodeOutput process(T nodeConfig, NodeProcessingContext context) throws NodeProcessingException;

}
