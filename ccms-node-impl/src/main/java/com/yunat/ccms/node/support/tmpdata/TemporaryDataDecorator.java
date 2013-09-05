package com.yunat.ccms.node.support.tmpdata;

import com.yunat.ccms.node.spi.NodeTemporaryDataHandler;

/**
 * role: Decorator 装饰者抽象类
 * @author yinwei
 */
public abstract class TemporaryDataDecorator implements NodeTemporaryDataHandler {
	public abstract String generate(Long jobId, Long subjobId, String suffix, String sql);

	public abstract void destroy(String name);

	public abstract String convert(Long subjobId, String suffix, String name);
}