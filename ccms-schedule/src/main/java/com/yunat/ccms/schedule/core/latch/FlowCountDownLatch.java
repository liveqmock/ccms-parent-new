package com.yunat.ccms.schedule.core.latch;

import java.text.MessageFormat;


/**
 * 流程同步器，与Job相对应，countDown到0时该Job执行结束
 * @author xiaojing.qu
 *
 */
public class FlowCountDownLatch extends CountDownLatch {

	private final long jobId;
	
	public FlowCountDownLatch(long jobId, int total) {
		super(toLatchId(jobId), total);
		this.jobId = jobId;
	}
	
	public static String toLatchId(long jobId){
		return MessageFormat.format(FLOW_LATCH_ID_PATTERN, jobId);
	}

	public long getJobId() {
		return jobId;
	}
	

}

