package com.yunat.ccms.schedule.core.latch;

import java.text.MessageFormat;

/**
 * 分支合并同步器，对应多个输入的节点，countDown到0时才执行该节点
 * @author xiaojing.qu
 *
 */
public class GatewayCountDownLatch extends CountDownLatch  {
	
	private final long jobId;
	private final long nodeId;

	public GatewayCountDownLatch(long jobId, long nodeId, int total) {
		super(toLatchId(jobId,nodeId),total);
		this.jobId = jobId;
		this.nodeId = nodeId;
	}
	
	public static String toLatchId(long jobId, long nodeId){
		return MessageFormat.format(GATEWAY_LATCH_ID_PATTERN, jobId,nodeId);
	}

	public long getJobId() {
		return jobId;
	}

	public long getNodeId() {
		return nodeId;
	}


}