package com.yunat.ccms.schedule.support;

public class ScheduleCons {

	public static final String WORKFLOW_VERSION = "2.1.0";

	public static final String KEY_TASK = "task";

	public static final String PREFIX_CAMPAIGN = "CAMPAIGN_";

	/*** 是否由Quartz触发 */
	public static final String KEY_TRIGGER_FIRE_BY_QUARTZ = "triggerFireByQuartz";

	/*** trigger计划触发时间 */
	public static final String KEY_TRIGGER_SCHEDULE_TIME = "triggerScheduleTime";

	/*** 是否会再次触发 */
	public static final String KEY_TRIGGER_FIRE_AGAIG = "triggerFireAgain";

	public static final String KEY_CAMP_ID = "campId";
	public static final String KEY_JOB_ID = "jobId";
	public static final String KEY_NODE_ID = "nodeId";
	public static final String KEY_NODE_TYPE = "nodeType";

	public static final String KEY_PRE_NODE_ID = "preNodeId";
	public static final String KEY_PRE_NODE_TYPE = "preNodeType";

	/**
	 * 执行类型
	 * 
	 * @param isTest
	 * @return
	 */
	public static final String execType(boolean isTest) {
		return isTest ? "测试执行" : "正式执行";
	}

}
