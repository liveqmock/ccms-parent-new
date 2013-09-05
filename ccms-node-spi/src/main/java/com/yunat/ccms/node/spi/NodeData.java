package com.yunat.ccms.node.spi;

/**
 * 节点之间流转的数据
 * 
 * @author xiaojing.qu
 * 
 */
public interface NodeData {

	/**
	 * 数据的代码 <br>
	 * <li>对于表数据是表名 <li>对视图数据是视图名
	 * 
	 * @return
	 */
	public String getCode();

	/**
	 * 数据的类型
	 * 
	 * @return
	 */
	public String getType();

}