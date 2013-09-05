package com.yunat.ccms.node.support.io;

import com.yunat.ccms.node.spi.NodeData;

/**
 * 表数据
 * 
 * @author xiaojing.qu
 * 
 */
public class Table implements NodeData {

	private final String tname;

	/**
	 * @param tname
	 *            表名
	 */
	public Table(String tname) {
		this.tname = tname;
	}

	@Override
	public String getCode() {
		return tname;
	}

	@Override
	public String getType() {
		return "Table";
	}

}
