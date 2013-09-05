package com.yunat.ccms.schedule.core.task;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.core.trigger.DurableTrigger;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.support.ParamHolder;
import com.yunat.ccms.schedule.support.ScheduleCons;

public class CampTask extends Task {

	private static final Logger logger = LoggerFactory.getLogger(CampTask.class);

	public CampTask(long campId, boolean isTest, TaskTrigger trigger, ParamHolder extra) {
		super(campId, isTest, trigger, extra);
	}

	@Override
	public String getTaskId() {
		return TaskNamingUtil.getCampTaskId(campId, isTest);
	}

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private JobLogService jobLogService;

	@Override
	public void run() {
		try {
			action();
		} catch (Exception e) {// TO prevent Thread leak
			e.printStackTrace();
		}
	}

	public void action() {
		logger.info("活动执行开始：" + this.toString());
		TaskTrigger trigger = this.getTrigger();
		if (trigger instanceof DurableTrigger) {
			logger.info("等待持久化trigger触发：" + this.toString());
			return;
		}
		// 单次立即执行的活动
		LogJob logjob = new LogJob(campId, isTest, trigger.getNextFireTime(), true);
		logjob = jobLogService.saveJob(logjob);
		FlowTask flowTask = taskRepository.createFlowTask(campId, logjob.getJobId(), logjob.isTest(), null);
		disruptor.fireStartEvent(flowTask);
	}

	@Override
	public String toString() {
		return MessageFormat.format("CampTask[camp_id={0},{1}]", String.valueOf(campId), ScheduleCons.execType(isTest));
	}
}
