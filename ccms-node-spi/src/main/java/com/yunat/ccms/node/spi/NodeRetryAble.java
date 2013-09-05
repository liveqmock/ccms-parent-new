package com.yunat.ccms.node.spi;

/**
 * 节点抛出异常后可否重试
 */
public interface NodeRetryAble {
	
	/**
	 * 当前是否可以重试，需要在节点实现中控制重试次数，特别是渠道节点
	 *
	 * @param context 节点执行上下文
	 * @return true 如果当前节点可以重试时返回</br>false 为默认值，不可重试时或者判断时发生异常时返回
	 */
	public abstract boolean canRetry(NodeProcessingContext context);

}
