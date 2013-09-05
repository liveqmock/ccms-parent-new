package com.yunat.ccms.schedule.core.trigger;

import java.util.List;

import org.quartz.Trigger;

import com.yunat.ccms.schedule.core.TaskTrigger;

/**
 * 
 * 持久化的Trigger，由quartz实现
 * 
 * @author xiaojing.qu
 * 
 */
public abstract class DurableTrigger implements TaskTrigger {

	/***  */
	private static final long serialVersionUID = -7226445289564657627L;

	public abstract List<? extends Trigger> getTriggers();

}
