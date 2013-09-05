package com.yunat.ccms.schedule.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yunat.ccms.schedule.core.TaskCountDownLatchRepository;
import com.yunat.ccms.schedule.core.latch.CountDownLatch;
import com.yunat.ccms.schedule.core.latch.FlowCountDownLatch;
import com.yunat.ccms.schedule.core.latch.GatewayCountDownLatch;
import com.yunat.ccms.schedule.core.latch.NodeCountDownLatch;

@Component
@Scope("singleton")
public class DefaultCountDownLatchRepository implements TaskCountDownLatchRepository{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultCountDownLatchRepository.class);

	private static Map<String, CountDownLatch> latcheRegistry = new ConcurrentHashMap<String, CountDownLatch>();
	
	private static Map<Long,List<String>> jobRelatedLatchRegistry = new ConcurrentHashMap<Long,List<String>>();

	public FlowCountDownLatch createNewFlowLatch(Long jobId, int total) {
		FlowCountDownLatch latch = new FlowCountDownLatch(jobId, total);
		register(latch);
		return latch;
	}

	public GatewayCountDownLatch createNewGateWayLatch(Long jobId, Long nodeId, int total) {
		GatewayCountDownLatch latch = new GatewayCountDownLatch(jobId, nodeId, total);
		register(latch);
		return latch;
	}

	public NodeCountDownLatch createNewNodeLatch(Long jobId, Long nodeId) {
		NodeCountDownLatch latch = new NodeCountDownLatch(jobId, nodeId);
		register(latch);
		return latch;
	}

	public FlowCountDownLatch retrieveFlowLatch(Long jobId) {
		FlowCountDownLatch latch = (FlowCountDownLatch) latcheRegistry.get(FlowCountDownLatch.toLatchId(jobId));
		logger.info("get FlowLatch by jobId:{} = {}", jobId,latch);
		Assert.notNull(latch);
		return latch;
	}

	public GatewayCountDownLatch retrieveGateWayLatch(Long jobId, Long nodeId) {
		GatewayCountDownLatch latch = (GatewayCountDownLatch) latcheRegistry.get(GatewayCountDownLatch.toLatchId(jobId, nodeId));
		logger.info("get GateWayLatch by jobId:{},nodeId:{} = {}", new Object[]{jobId,nodeId,latch});
		Assert.notNull(latch);
		return latch;
	}

	public NodeCountDownLatch retrieveNodeLatch(Long jobId, Long nodeId) {
		NodeCountDownLatch latch =(NodeCountDownLatch) latcheRegistry.get(NodeCountDownLatch.toLatchId(jobId, nodeId)); 
		logger.info("get NodeLatch by jobId:{},nodeId:{} = {}", new Object[]{jobId,nodeId,latch});
		Assert.notNull(latch);
		return latch;
	}
	
	private void register(CountDownLatch latch){
		String latchId = latch.getLatchId();
		latcheRegistry.put(latchId, latch);
		Long jobId =  latch.getJobId();
		List<String> latchIds = jobRelatedLatchRegistry.get(latchId);
		if(latchIds==null){
			latchIds = new ArrayList<String>();
			jobRelatedLatchRegistry.put(jobId, latchIds);
		}
		latchIds.add(latchId);
		logger.info("register CountDownLatch:{}", latch.getLatchId());
	}

	@Override
	public void clearAllLatch(Long jobId) {
		String latchId = FlowCountDownLatch.toLatchId(jobId);
		List<String> latchIds = jobRelatedLatchRegistry.get(latchId);
		if(latchIds!=null){
			for(String key:latchIds){
				latcheRegistry.remove(key);
				logger.info("remove CountDownLatch:{}", key);
			}
		}
		jobRelatedLatchRegistry.remove(latchId);
	}


}
