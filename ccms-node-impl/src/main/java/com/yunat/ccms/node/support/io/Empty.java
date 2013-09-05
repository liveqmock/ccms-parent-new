package com.yunat.ccms.node.support.io;

import com.yunat.ccms.node.spi.NodeData;

/**
 * 空数据(某些节点没有输入 或者 输出)
 * 
 * @author xiaojing.qu
 * 
 */
public class Empty implements NodeData {

	@Override
	public String getCode() {
		return null;
	}

	@Override
	public String getType() {
		return "Empty";
	}

}
