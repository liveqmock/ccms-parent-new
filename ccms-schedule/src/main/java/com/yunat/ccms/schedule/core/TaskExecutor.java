package com.yunat.ccms.schedule.core;

public interface TaskExecutor {

	String getName();

	/**
	 * 提交任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	void execute(Task task) throws Exception;

	/**
	 * 中止任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	void cancel(Task task) throws Exception;

	/**
	 * 当前状态
	 * 
	 * @return
	 */
	Object statistics();

}
