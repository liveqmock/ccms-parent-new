package com.yunat.ccms.schedule.core;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务触发器
 * 
 * @author xiaojing.qu
 * 
 */
public interface TaskTrigger extends Serializable {

	/**
	 * Trigger id
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 下次触发的时间
	 * 
	 * @return
	 */
	public Date getNextFireTime();

}
