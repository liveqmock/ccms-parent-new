/**
 *
 */
package com.yunat.ccms.tradecenter.urpay.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *调度任务基类
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 上午10:56:19
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class BaseJob implements Job{

	protected Logger logger = LoggerFactory.getLogger(getClass());


	public void execute(final JobExecutionContext context) throws JobExecutionException {
		logger.info(context.getJobDetail().getKey().getName()+" is start!");
		handleBefore(context);
		handle(context);
		logger.info(context.getJobDetail().getKey().getName()+" is end!");
	}

	/**
	 * 子类需要实现改方法
	 *
	 * @param context
	 */
	public abstract void handle(JobExecutionContext context);

	/**
	 * 执行之前要调用的方法
	 *
	 * @param context
	 */
	public void handleBefore(JobExecutionContext context){

	}

}
