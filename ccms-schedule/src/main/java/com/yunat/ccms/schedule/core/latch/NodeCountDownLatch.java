package com.yunat.ccms.schedule.core.latch;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

/**
 * 节点同步器，countDown到0时该节点执行结束
 * 对重复countDown友好，以支持节点恢复 和 节点正式流程并发进行
 * @author xiaojing.qu
 *
 */
public class NodeCountDownLatch extends CountDownLatch  {
	
	public static  Logger logger = LoggerFactory.getLogger(NodeCountDownLatch.class);
	
	private final long jobId;
	private final long nodeId;
	private FlowCountDownLatch flowCountDownLatch;
	private List<GatewayCountDownLatch> gatewayCountDownLatchs;
	

	public NodeCountDownLatch(long jobId, long nodeId) {
		super(toLatchId(jobId,nodeId), 1);
		this.jobId = jobId;
		this.nodeId = nodeId;
		gatewayCountDownLatchs = Lists.newArrayList();
	}
	
	public static String toLatchId(long jobId, long nodeId){
		return MessageFormat.format(NODE_LATCH_ID_PATTERN, jobId,nodeId);
	}

	@Override
	public boolean countDown() {
		boolean isCountDowned = super.countDown();
		logger.info("{} isCountDowned?:{}", this.getLatchId(),isCountDowned);
		if(isCountDowned){
			flowCountDownLatch.countDown();
			for(GatewayCountDownLatch getewaylatch:gatewayCountDownLatchs){
				getewaylatch.countDown();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean countUp(){
		boolean isCountUped = super.countUp();
		logger.info("{} isCountUped?:{}", this.getLatchId(),isCountUped);
		if(isCountUped){
			flowCountDownLatch.countUp();
			for(GatewayCountDownLatch getewaylatch:gatewayCountDownLatchs){
				getewaylatch.countUp();
			}
			return true;
		}
		return false;
	}

	public void setFlowCountDownLatch(FlowCountDownLatch flowCountDownLatch) {
		Assert.notNull(flowCountDownLatch);
		this.flowCountDownLatch = flowCountDownLatch;
	}

	public void addGatewayCountDownLatch(
			GatewayCountDownLatch gatewayCountDownLatch) {
		Assert.notNull(gatewayCountDownLatch);
		this.gatewayCountDownLatchs.add(gatewayCountDownLatch);
		
	}

	public FlowCountDownLatch getFlowCountDownLatch() {
		return flowCountDownLatch;
	}

	public List<GatewayCountDownLatch> getGatewayCountDownLatch() {
		return gatewayCountDownLatchs;
	}
	
	public long getJobId() {
		return jobId;
	}

	public long getNodeId() {
		return nodeId;
	}
}
