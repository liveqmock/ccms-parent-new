package com.yunat.ccms.schedule.core;

/**
 * 任务路由，让任务找到合适的执行器
 * 
 * @author xiaojing.qu
 * 
 */
public interface TaskRouter {

	/**
	 * 为给定的任务找到合适的执行器
	 * 
	 * @param task
	 * @return
	 */
	TaskExecutor route(Task task);

	/**
	 * 得到当前的TaskRouter状态信息
	 * 
	 * @return
	 */
	Object statistics();

}
