package com.yunat.ccms.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.schedule.core.TaskScheduler;

@Component
public class ScheduleStatusService {

	@Autowired
	private TaskScheduler scheduler;

	public Object getStatus() {
		return scheduler.statistics();
	}

}
