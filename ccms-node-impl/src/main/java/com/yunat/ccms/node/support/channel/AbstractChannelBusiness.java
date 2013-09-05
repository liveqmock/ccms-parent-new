package com.yunat.ccms.node.support.channel;

import com.yunat.ccms.node.spi.NodeProcessingContext;

public interface AbstractChannelBusiness<T> {
	/**
	 * 生成渠道用户日志数据
	 * @param nodeConfig
	 * @param context
	 * @param schemaName
	 */
	void generateChannelUserRecord(T nodeConfig, NodeProcessingContext context, String schemaName);

	/**
	 * 更新节点执行后的数据集
	 * @param nodeConfig
	 * @param context
	 * @param schemaName
	 */
	void refreshExecutionRecord(T nodeConfig, NodeProcessingContext context, String schemaName);

	/**
	 * 构筑输入数据的sql
	 * @param nodeConfig
	 * @param context
	 * @return
	 */
	String builderSQL(T nodeConfig, NodeProcessingContext context);

	/**
	 * 执行发送
	 * @param nodeConfig
	 * @param context
	 * @param schemaName
	 */
	void executeDelivery(T nodeConfig, NodeProcessingContext context, String schemaName);

}