package com.yunat.ccms.schedule.core;


/**
 * 任务调度器,负责调度任务
 * @author xiaojing.qu
 *
 */
public abstract class TaskScheduler {
	
	/**
	 * 提交一个Task
	 * <br>在负载超过处理能力时，不保证马上处理
	 * 
	 * @param task
	 * @throws Exception
	 */
	public abstract void submit(Task task) throws Exception;

	/**
	 * 请求取消一个已经放入调度器中Task
	 * @param task
	 */
	public abstract void cancel(Task task) throws Exception;
	
	/**
	 * 得到当前的Scheduler状态信息
	 * @return
	 */
	public abstract Object statistics();

}


