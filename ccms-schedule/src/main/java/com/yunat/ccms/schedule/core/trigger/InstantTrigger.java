package com.yunat.ccms.schedule.core.trigger;

import java.util.Date;

import org.joda.time.DateTime;

import com.yunat.ccms.schedule.core.TaskTrigger;

/**
 * 立即触发
 * 
 * @author xiaojing.qu
 * 
 */
public class InstantTrigger implements TaskTrigger {

	private static final long serialVersionUID = 5542103085142230416L;

	private DateTime scheduledTime;

	public InstantTrigger() {
		scheduledTime = DateTime.now();
	}

	@Override
	public String toString() {
		return DateTime.now().toString();
	}

	@Override
	public String getId() {
		return scheduledTime.toString();
	}

	@Override
	public Date getNextFireTime() {
		return scheduledTime.toDate();
	}

}
