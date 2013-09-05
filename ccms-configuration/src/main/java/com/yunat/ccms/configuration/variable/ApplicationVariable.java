package com.yunat.ccms.configuration.variable;

/**
 * 程序系统后不会再变化的参数
 * 
 * @author xiaojing.qu
 * 
 */
public interface ApplicationVariable {

	/**
	 * 获取租户ID
	 * 
	 * @return
	 */
	public String getTenentId();

	/**
	 * 获取租户密码
	 * 
	 * @return
	 */
	public String getTenentPassword();

	/**
	 * 获取保存数据文件的目录
	 * 
	 * @return
	 */
	public String getFileStorePath();

}
