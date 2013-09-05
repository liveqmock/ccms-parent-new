package com.yunat.ccms.schedule.core;

import com.yunat.ccms.schedule.core.latch.FlowCountDownLatch;
import com.yunat.ccms.schedule.core.latch.GatewayCountDownLatch;
import com.yunat.ccms.schedule.core.latch.NodeCountDownLatch;

public interface TaskCountDownLatchRepository {
	
	/**
	 * 创建新的 流程同步计数器
	 * @param jobId
	 * @param total
	 * @return
	 */
	public FlowCountDownLatch createNewFlowLatch(Long jobId, int total);
	
	/**
	 * 创建新的 分支合并同步计数器
	 * @param jobId
	 * @param nodeId
	 * @param total
	 * @return
	 */
	public GatewayCountDownLatch createNewGateWayLatch(Long jobId, Long nodeId,int total) ;
	
	/**
	 * 创建新的 节点同步计数器
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public NodeCountDownLatch createNewNodeLatch(Long jobId, Long nodeId) ;
	
	/**
	 * 返回 流程同步计数器
	 * @param jobId
	 * @return
	 */
	public FlowCountDownLatch retrieveFlowLatch(Long jobId) ;
	
	/**
	 * 返回 分支合并同步计数器
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public GatewayCountDownLatch retrieveGateWayLatch(Long jobId, Long nodeId) ;
	
	/**
	 * 返回 节点同步计数器
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public NodeCountDownLatch retrieveNodeLatch(Long jobId, Long nodeId);
	
	/**
	 * job结束时调用，清除内存中的CountDownLatch
	 * @param jobId
	 */
	public void clearAllLatch(Long jobId);

}
