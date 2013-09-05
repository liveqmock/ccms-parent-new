package com.yunat.ccms.configuration.variable;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

/**
 * 系统初始化后可以获得的全局变量
 * 
 * @author xiaojing.qu
 * 
 */
public interface SystemVariable {

	/**
	 * 获取的当前的ApplicationContext
	 * 
	 * @return
	 */
	ApplicationContext getApplicationContext();

	/**
	 * 获取容器的ServletContext
	 * 
	 * @return
	 */
	ServletContext getServletContext();

	/**
	 * 获取web应用根路径对应的部署路径
	 * 
	 * @return
	 */
	String getWebRootFullPath();

}
