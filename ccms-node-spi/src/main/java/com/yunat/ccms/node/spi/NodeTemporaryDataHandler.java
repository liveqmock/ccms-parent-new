package com.yunat.ccms.node.spi;

/**
 * role: Component 节点临时产出(表或者视图)的操作
 * @author yinwei
 */
public interface NodeTemporaryDataHandler {

	// 创建临时产出
	String generate(Long jobId, Long subjobId, String suffix, String sql);

	// 删除临时产出
	void destroy(String name);

	// 得到临时产出
	String convert(Long subjobId, String suffix, String name);

}
