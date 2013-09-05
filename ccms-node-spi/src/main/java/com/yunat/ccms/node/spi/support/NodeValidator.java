package com.yunat.ccms.node.spi.support;

import com.yunat.ccms.node.spi.NodeEntity;

/**
 * 节点验证逻辑：
 * 1:验证节点是否配置
 * 2：验证时间相关节点时间是否配置正确
 * 3：验证节点间连接关系
 * 
 * @author xiaojing.qu
 * 
 * @param <T>
 */
public interface NodeValidator<T extends NodeEntity> {

	/**
	 * @param nodeEntity
	 *            节点配置实体
	 * @param validateContext
	 *            验证上下文
	 * @return 如果当前验证没有问题，直接返回null
	 */
	ValidateMessage validate(T nodeEntity, NodeValidationContext validateContext);

}
