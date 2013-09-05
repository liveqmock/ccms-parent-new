package com.yunat.ccms.schedule.core.disruptor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.EventHandler;
import com.yunat.ccms.core.support.statemachine.state.SubjobState;
import com.yunat.ccms.schedule.core.TaskScheduler;
import com.yunat.ccms.schedule.core.TaskTrigger;
import com.yunat.ccms.schedule.core.disruptor.DisruptorDelegate;
import com.yunat.ccms.schedule.core.disruptor.Event;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.FlowTask;
import com.yunat.ccms.schedule.core.task.NodeTask;
import com.yunat.ccms.schedule.core.trigger.DurableTrigger;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.service.JobLogService;
import com.yunat.ccms.schedule.support.ScheduleCons;

/**
 * 准备就绪事件处理
 * 
 * @author xiaojing.qu
 * 
 */
@Component("readyEventHandler")
@Scope("singleton")
public class ReadyEventHandler implements EventHandler<Event>, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(ReadyEventHandler.class);

	@Autowired
	private DisruptorDelegate<Event> disruptor;

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private JobLogService jobLogService;

	@Override
	public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
		Map<String, ?> data = event.getData();
		Object task = data.get(ScheduleCons.KEY_TASK);
		logger.info("处理就绪事件:" + task.toString());
		try {
			if (task instanceof NodeTask) {
				handleNodeTaskReady((NodeTask) task);
			}
			if (task instanceof FlowTask) {
				handleFlowTaskReady((FlowTask) task);
			}
			if (task instanceof CampTask) {
				handleCampaignTaskReady((CampTask) task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleNodeTaskReady(NodeTask task) throws Exception {
		Long jobId = task.getJobId();
		Long nodeId = task.getNodeId();
		LogSubjob logSubjob = jobLogService.getSubjob(jobId, nodeId);
		SubjobState state = logSubjob.getState();
		TaskTrigger trigger = task.getTrigger();
		if (trigger instanceof DurableTrigger) {
			logSubjob.setStatus(state.handleWaitTimer().getCode());
		} else {
			logSubjob.setStatus(state.handleWaitResource().getCode());
		}
		jobLogService.updateSubjob(logSubjob);
		taskScheduler.submit(task);
	}

	private void handleFlowTaskReady(FlowTask task) throws Exception {
		taskScheduler.submit(task);
	}

	private void handleCampaignTaskReady(CampTask task) throws Exception {
		logger.error("handleCampaignTaskReady will nerver be implemented");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("setting readyEventHandler");
		disruptor.handReadyEventWith(this);
	}

}
