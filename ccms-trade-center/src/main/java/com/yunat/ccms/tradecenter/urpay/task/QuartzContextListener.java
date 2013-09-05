/**
 *
 */
package com.yunat.ccms.tradecenter.urpay.task;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.yunat.ccms.tradecenter.domain.QuartzTaskInitDomain;
import com.yunat.ccms.tradecenter.service.QuartzTaskInitService;



/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 下午01:14:45
 */
@Component
@Scope("singleton")
public class QuartzContextListener implements ApplicationListener<ApplicationEvent>{

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private QuartzTaskInitService quartzTaskInitService;

	@Autowired
	@Qualifier("quartz-tc")
	private Scheduler sched;


	@Override
	public void onApplicationEvent(ApplicationEvent paramE) {
		if (paramE instanceof ContextRefreshedEvent) {
			boolean isRoot = ((ContextRefreshedEvent) paramE).getApplicationContext().getParent() == null;
			if (isRoot) {
				try {
					logger.info("-----Quartz任务初始化开始------");
			        List<QuartzTaskInitDomain> taskList = quartzTaskInitService.queryQuartzTaskList();
			        logger.info("启动任务数：【"+taskList.size()+"】");
			        for(QuartzTaskInitDomain task : taskList){
			        	logger.info("启动任务-任务组【"+task.getJob_group()+"】任务名【"+task.getJob_name()+"】任务类【"+task.getJob_class_name()+"】表达式【"+task.getCron_expression()+"】");
			        	if(task.getJob_group()!=null&&!"".equals(task.getJob_group())&&task.getJob_name()!=null&&!"".equals(task.getJob_name())){
			        		Class<? extends BaseJob> cls = null;
			        		if(task.getJob_class_name()!=null){
			        			try {
			        				cls = (Class<? extends BaseJob>) Class.forName(task.getJob_class_name());
			        			} catch (ClassNotFoundException e1) {
			        			}
			        			//设置任务执行类
			        			if(cls!=null){
			        				//首先删除这个任务
						        	JobKey jobKey = new JobKey(task.getJob_name(), task.getJob_group());
						            sched.deleteJob(jobKey);
						        	//新加一个任务调度
						        	JobDetail job = newJob(cls).withIdentity(task.getJob_name(), task.getJob_group()).build();
						            CronTrigger trigger = newTrigger().withIdentity("trigger-"+task.getJob_name(), task.getJob_group()).withSchedule(cronSchedule(task.getCron_expression())).build();
						            sched.scheduleJob(job, trigger);
			        			}
			        		}else{
			        		}
			        	}

			        }
			        sched.start();
			        logger.info("-----启动任务完成------");
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
