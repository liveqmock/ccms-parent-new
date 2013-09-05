package com.yunat.ccms.channel.support;

import com.yunat.ccms.node.spi.NodeEntity;

public interface ChannelEntity extends NodeEntity {
	/**
	 * 渠道id,譬如"亿美软通"为3
	 * 
	 * @return
	 */
	Long getChannelId();

	/**
	 * 渠道类型,譬如"SMS"为1
	 * 
	 * @return
	 */
	Long getChannelType();
}
