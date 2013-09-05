package com.yunat.ccms.schedule.core.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * job 执行的监控日志
 * 
 * @author ruiming.lu
 * 
 */
public class JobExecuteLogListener implements JobListener {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * getName() 方法返回一个字符串用以说明 JobListener 的名称。 对于注册为全局的监听器，getName()
	 * 主要用于记录日志，对于由特定 Job 引用的 JobListener， 注册在 JobDetail 上的监听器名称必须匹配从监听器上
	 * getName() 方法的返回值。
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * Scheduler 在 JobDetail 即将被执行，但又被 TriggerListener 否决了时调用这个方法。
	 * 
	 * @param arg0
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().toString();
		log.info(jobName + " was vetoed and not executed()");

	}

	/**
	 * Scheduler 在 JobDetail 将要被执行时调用这个方法
	 * 
	 * @param arg0
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().toString();
		log.info(jobName + " is about to be executed");

	}

	/**
	 * Scheduler 在 JobDetail 被执行之后调用这个方法。
	 * 
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String jobName = context.getJobDetail().toString();
		log.info(jobName + " was executed");

	}

}
