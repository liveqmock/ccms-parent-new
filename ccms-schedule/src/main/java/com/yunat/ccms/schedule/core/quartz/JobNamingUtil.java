package com.yunat.ccms.schedule.core.quartz;

import com.yunat.ccms.schedule.support.ScheduleCons;

public class JobNamingUtil {

	public static String getCampJobName(Long campId) {
		return ScheduleCons.PREFIX_CAMPAIGN + campId;
	}

	public static String getNodeJobName(Long jobId, Long nodeId) {
		return jobId + "_" + nodeId;
	}

	public static Long getCampId(String campJobName) {
		Long campIdLong = null;
		boolean isCampTrigger = campJobName.startsWith(ScheduleCons.PREFIX_CAMPAIGN);
		if (isCampTrigger) {
			String[] sa = campJobName.split("_");
			if (sa != null && sa.length == 2) {
				String campIdStr = sa[1];
				campIdLong = Long.valueOf(campIdStr);
			}
		}
		return campIdLong;
	}

	public static Long getJobId(String nodeJobName) {
		Long jobId = null;
		String[] sa = nodeJobName.split("_");
		if (sa != null && sa.length == 2) {
			String jobIdStr = sa[0];
			jobId = Long.valueOf(jobIdStr);
		}
		return jobId;
	}

	public static Long getNodeId(String nodeJobName) {
		Long nodeId = null;
		String[] sa = nodeJobName.split("_");
		if (sa != null && sa.length == 2) {
			String nodeIdStr = sa[1];
			nodeId = Long.valueOf(nodeIdStr);
			;
		}
		return nodeId;
	}

}
