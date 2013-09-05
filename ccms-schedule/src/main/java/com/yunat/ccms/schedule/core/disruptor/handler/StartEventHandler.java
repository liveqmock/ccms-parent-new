package com.yunat.ccms.schedule.core.disruptor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.EventHandler;
import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.service.CheckpointService;
import com.yunat.ccms.schedule.support.NextActionEnum;
import com.yunat.ccms.schedule.support.ScheduleCons;

/**
 * 开始事件处理（主要检查活动状态，看是否继续执行活动）
 * 
 * @author xiaojing.qu
 * 
 */
@Component("startEventHandler")
@Scope("singleton")
public class StartEventHandler implements EventHandler<Event>, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(StartEventHandler.class);

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private CheckpointService checkPointService;

	@Override
	public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
		Map<String, ?> data = event.getData();
		Task task = (Task) data.get(ScheduleCons.KEY_TASK);
		logger.info("处理开始事件:" + task.toString());
		NextActionEnum nextAction = checkPointService.checkCampState(task.getCampId(), task.isTest());
		if (nextAction == NextActionEnum.RETURN) {
			return;
		}
		try {
			if (task instanceof NodeTask) {
				handleNodeTaskStart((NodeTask) task);
			}
			if (task instanceof FlowTask) {
				handleFlowTaskStart((FlowTask) task);
			}
			if (task instanceof CampTask) {
				handleCampaignTaskStart((CampTask) task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleNodeTaskStart(NodeTask task) throws Exception {
		NextActionEnum nextAction1 = checkPointService.checkJobState(task.getJobId(), task.isTest());
		if (nextAction1 == NextActionEnum.RETURN) {
			return;
		}
		NextActionEnum nextAction2 = checkPointService.checkSubjobState(task.getJobId(), task.getNodeId());
		switch (nextAction2) {
		case RETURN:
			return;
		case TO_END:
			disruptor.fireEndEvent(task);
			return;
		case CONTINUE:
			break;
		default:
			;
		}
		disruptor.fireReadyEvent(task);
	}

	private void handleFlowTaskStart(FlowTask task) throws Exception {
		disruptor.fireReadyEvent(task);
	}

	private void handleCampaignTaskStart(CampTask task) throws Exception {
		// 逻辑上不可能出现，因为前面有判断活动是否在执行中
		logger.error("handleCampaignTaskStart will nerver be implemented");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("setting startEventHandler");
		disruptor.handStartEventWith(this);
	}

}
