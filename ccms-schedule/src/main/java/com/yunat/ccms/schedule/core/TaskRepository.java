package com.yunat.ccms.schedule.core;

import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.support.ParamHolder;

public interface TaskRepository {

	/**
	 * 创建一个CampTask
	 * 
	 * @param campId
	 * @param isTest
	 * @param extra
	 * @return
	 */
	CampTask createCampTask(Long campId, boolean isTest, ParamHolder extra);

	/**
	 * 创建一个FlowTask
	 * 
	 * @param campId
	 * @param jobId
	 * @param isTest
	 * @param extra
	 * @return
	 */
	FlowTask createFlowTask(Long campId, Long jobId, boolean isTest, ParamHolder extra);

	/**
	 * 创建一个NodeTask(同时创建subjob)
	 * 
	 * @param campId
	 * @param jobId
	 * @param nodeId
	 * @param nodeType
	 * @param isTest
	 * @param extra
	 * @return
	 */
	NodeTask createNodeTask(Long campId, Long jobId, Long nodeId, String nodeType, boolean isTest, ParamHolder extra);

}
