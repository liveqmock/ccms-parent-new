package com.yunat.ccms.node.support.io;

import com.yunat.ccms.node.spi.NodeData;

/**
 * 视图数据
 * 
 * @author xiaojing.qu
 * 
 */
public class View implements NodeData {

	private final String vname;

	/**
	 * @param vname
	 *            视图名
	 */
	public View(String vname) {
		this.vname = vname;
	}

	@Override
	public String getCode() {
		return vname;
	}

	@Override
	public String getType() {
		return "View";
	}

}
