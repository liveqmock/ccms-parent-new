package com.yunat.ccms.schedule.core.task;

import java.text.MessageFormat;

public class TaskNamingUtil {

	/** CampTask ID的格式{@value} */
	protected static final String CAMP_TASK_ID_PATTERN = "CAMPAIGN_{0}_{1}";
	/** FlowTask ID的格式{@value} */
	protected static final String FLOW_TASK_ID_PATTERN = "CAMPAIGN_{0}_{1}_JOB_{2}";
	/** NodeTask ID的格式{@value} */
	protected static final String NODE_TASK_ID_PATTERN = "CAMPAIGN_{0}_{1}_JOB_{2}_NODE_{3}";

	/** NodeTask 调试信息的格式{@value} */
	protected static final String NODE_TASK_DEBUG_INFO = " DEBUG[camp_id={0},job_id={1},node_id={2},subjob_id={3},is_test={4}] ";

	public static String getCampTaskId(long campId, boolean isTest) {
		return MessageFormat.format(CAMP_TASK_ID_PATTERN, String.valueOf(campId), (isTest ? "t" : "f"));
	}

	public static String getFlowTaskId(long campId, boolean isTest, long jobId) {
		return MessageFormat.format(FLOW_TASK_ID_PATTERN, String.valueOf(campId), (isTest ? "t" : "f"),
				String.valueOf(jobId));
	}

	public static String getNodeTaskId(long campId, boolean isTest, long jobId, long nodeId) {
		return MessageFormat.format(NODE_TASK_ID_PATTERN, String.valueOf(campId), (isTest ? "t" : "f"),
				String.valueOf(jobId), String.valueOf(nodeId));
	}

	public static String getNodeDebugInfo(long campId, long jobId, long nodeId, long subjobId, boolean isTest) {
		return MessageFormat.format(NODE_TASK_DEBUG_INFO, String.valueOf(campId), String.valueOf(jobId),
				String.valueOf(nodeId), String.valueOf(subjobId), String.valueOf(isTest));
	}
}
