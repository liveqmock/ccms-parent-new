/**
 *
 */
package com.yunat.ccms.tradecenter.controller;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.QuartzTaskRequest;
import com.yunat.ccms.tradecenter.domain.QuartzTaskInitDomain;
import com.yunat.ccms.tradecenter.service.QuartzTaskInitService;
import com.yunat.ccms.tradecenter.urpay.task.BaseJob;

/**
 *quartz控制请求
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-4 下午05:54:30
 */
@Controller
@RequestMapping(value = "/urpay/quartzTask/*")
public class QuartzTaskController {

	private static Logger logger = LoggerFactory.getLogger(QuartzTaskController.class);

	@Autowired
	private QuartzTaskInitService quartzTaskInitService;

	@Autowired
	@Qualifier("quartz-tc")
	private Scheduler sched;

	@ResponseBody
	@RequestMapping(value = "/addTask", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult addTask(@ModelAttribute final QuartzTaskRequest progRequest) {
		try {
			if(progRequest.getJobName()!=null&&progRequest.getJobGroup()!=null){
				QuartzTaskInitDomain task = quartzTaskInitService.getQuartzTask(progRequest.getJobName(), progRequest.getJobGroup());
				if(task!=null){
					logger.info("添加任务-任务组【"+task.getJob_group()+"】任务名【"+task.getJob_name()+"】任务类【"+task.getJob_class_name()+"】表达式【"+task.getCron_expression()+"】");
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
		        			}else{
		        				return ControlerResult.newError("任务初始化表类全名错误");
		        			}
		        		}else{
		        			return ControlerResult.newError("任务初始化表类全名未配置");
		        		}
		        	}else{
		        		return ControlerResult.newError("任务初始化表任务名或组名未填写");
		        	}
				}else{
					return ControlerResult.newError("任务初始化表未找到相关数据");
				}
			}else{
				return ControlerResult.newError("参数缺失");
			}
			return ControlerResult.newSuccess("操作成功");
		} catch (final Exception e) {
			logger.info("quartz操作异常：", e);
			return ControlerResult.newError("quartz操作异常");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteTask", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult deleteTask(@ModelAttribute final QuartzTaskRequest progRequest) {
		try {
	        if(progRequest.getJobName()!=null&&progRequest.getJobGroup()!=null){
	        	logger.info("删除任务-任务组【"+progRequest.getJobGroup()+"】任务名【"+progRequest.getJobName()+"】");
	        	JobKey jobKey = new JobKey(progRequest.getJobName(), progRequest.getJobGroup());
		        sched.deleteJob(jobKey);
	        }else{
	        	return ControlerResult.newError("任务初始化表任务名或组名未填写");
			}
			return ControlerResult.newSuccess("操作成功");
		} catch (final Exception e) {
			logger.info("quartz操作异常：", e);
			return ControlerResult.newError("quartz操作异常");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/startTask", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult startTask() {
		try {
			logger.info("开始所有任务");
	        sched.start();
			return ControlerResult.newSuccess("操作成功");
		} catch (final Exception e) {
			logger.info("quartz操作异常：", e);
			return ControlerResult.newError("quartz操作异常");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/standbyTask", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult standbyTask() {
		try {
			logger.info("暂停所有任务");
	        sched.standby();
			return ControlerResult.newSuccess("操作成功");
		} catch (final Exception e) {
			logger.info("quartz操作异常：", e);
			return ControlerResult.newError("quartz操作异常");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteAllTask", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult deleteAllTask() {
		try {
			List<QuartzTaskInitDomain> taskList = quartzTaskInitService.queryQuartzTaskList();
	        logger.info("删除任务数：【"+taskList.size()+"】");
	        for(QuartzTaskInitDomain task : taskList){
	        	logger.info("删除任务-任务组【"+task.getJob_group()+"】任务名【"+task.getJob_name()+"】");
	        	if(task.getJob_group()!=null&&!"".equals(task.getJob_group())&&task.getJob_name()!=null&&!"".equals(task.getJob_name())){
	        		JobKey jobKey = new JobKey(task.getJob_name(), task.getJob_group());
		            sched.deleteJob(jobKey);
	        	}
	        }
			return ControlerResult.newSuccess("操作成功");
		} catch (final Exception e) {
			logger.info("quartz操作异常：", e);
			return ControlerResult.newError("quartz操作异常");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/addAllTask", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ControlerResult addAllTask() {
		try {
	        List<QuartzTaskInitDomain> taskList = quartzTaskInitService.queryQuartzTaskList();
	        logger.info("增加任务数：【"+taskList.size()+"】");
	        for(QuartzTaskInitDomain task : taskList){
	        	logger.info("增加任务-任务组【"+task.getJob_group()+"】任务名【"+task.getJob_name()+"】任务类【"+task.getJob_class_name()+"】表达式【"+task.getCron_expression()+"】");
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
			return ControlerResult.newSuccess("操作成功");
		} catch (final Exception e) {
			logger.info("quartz操作异常：", e);
			return ControlerResult.newError("quartz操作异常");
		}
	}

}
