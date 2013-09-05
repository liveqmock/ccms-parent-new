package com.yunat.ccms.schedule.core;

import com.yunat.ccms.schedule.support.ParamHolder;

public interface TaskTriggerRepository {
	
	
	/**
	 * 立刻触发的trigger
	 * @return
	 */
	TaskTrigger instantTrigger();
	
	/**
	 * @param campId
	 * @param isTest 是否测试执行
	 * @return
	 */
	TaskTrigger createCampTaskTrigger(Long campId, boolean isTest,ParamHolder extra);
	
	/**
	 * @param jobId
	 * @param isTest 是否测试执行
	 * @return
	 */
	TaskTrigger createFlowTaskTrigger(Long jobId, boolean isTest,ParamHolder extra);
	
	/**
	 * @param jobId
	 * @param nodeId
	 * @param nodeType
	 * @param isTest 是否测试执行
	 * @param extra
	 * @return
	 */
	TaskTrigger createNodeTaskTrigger(Long jobId,Long nodeId,String nodeType,boolean isTest,ParamHolder extra);
	
}
