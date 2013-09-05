package com.yunat.ccms.schedule.service;

import static com.yunat.ccms.schedule.support.NextActionEnum.CONTINUE;
import static com.yunat.ccms.schedule.support.NextActionEnum.RETURN;
import static com.yunat.ccms.schedule.support.NextActionEnum.TO_END;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.statemachine.state.CampaignState;
import com.yunat.ccms.core.support.statemachine.state.JobState;
import com.yunat.ccms.core.support.statemachine.state.SubjobState;
import com.yunat.ccms.schedule.domain.LogJob;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.repository.CampaignDao;
import com.yunat.ccms.schedule.repository.LogJobDao;
import com.yunat.ccms.schedule.repository.LogSubjobDao;
import com.yunat.ccms.schedule.support.NextActionEnum;
import com.yunat.ccms.schedule.support.ScheduleCons;

/**
 * 检查活动/job/subjob状态以决定下个动作
 * 
 * @author xiaojing.qu
 * 
 */
@Component
@Scope("singleton")
public class CheckpointService {

	private static final Logger logger = LoggerFactory.getLogger(CheckpointService.class);

	@Autowired
	private CampaignDao campDao;

	@Autowired
	private LogJobDao jobDao;

	@Autowired
	private LogSubjobDao subjobDao;

	/**
	 * 检查活动状态，判断当前是否继续执行
	 * 
	 * @param campId
	 * @param isTest
	 * @return
	 */
	public NextActionEnum checkCampState(Long campId, boolean isTest) {
		String statusId = campDao.getCampStatusId(campId);
		CampaignState state = CampaignState.fromCode(statusId);
		boolean toContinue = state.isRunning() && (state.isTesting() == isTest);
		if (!toContinue) {
			logger.error("Camp:{}当前状态:{},提交任务:{}", new Object[] { campId, statusId, ScheduleCons.execType(isTest) });
		}
		return toContinue ? CONTINUE : RETURN;
	}

	/**
	 * 检查Job状态，判断当前是否继续执行
	 * 
	 * @param jobId
	 * @param isTest
	 * @return
	 */
	public NextActionEnum checkJobState(Long jobId, boolean isTest) {
		LogJob job = jobDao.get(jobId);
		JobState state = job.getState();
		boolean toContinue = state.isRunning() && (job.isTest() == isTest);
		if (!toContinue) {
			logger.error("Camp:{}中Job:{}当前状态:{},提交任务:{}", new Object[] { job.getCampId(), jobId, job.getStatus(),
					ScheduleCons.execType(isTest) });
		}
		return toContinue ? CONTINUE : RETURN;
	}

	/**
	 * 执行节点前，检查Subjob状态，判断当前是否继续执行
	 * <ol>
	 * </br>
	 * <li>正在执行中，或者初始化状态，返回true </br>
	 * <li>执行完成，返回false </br>
	 * <li>执行结束，未完成处理，返回false，触发完成事件 </br>
	 * </ol>
	 * 
	 * @param jobId
	 * @param nodeId
	 * @return
	 */
	public NextActionEnum checkSubjobState(Long jobId, Long nodeId) {
		LogSubjob subjob = subjobDao.getSubjobByJobidAndNodeId(jobId, nodeId);
		if (subjob == null) {
			return CONTINUE;
		}
		SubjobState state = subjob.getState();
		switch (state) {
		case INITIAL:
		case EXECUTING:
		case WAITING_RESOURCE:
			return CONTINUE;
		case WAITING_TIMER:
			return RETURN;
		case COMPLETE_SUCCESS:
		case COMPLETE_ERROR:
		case COMPLETE_STOP:
		case COMPLETE_SKIP:
		case COMPLETE_EXCEPTION:
			// maybe some differences
		case FINISH_SUCCESS:
		case FINISH_ERROR:
		case FINISH_STOP:
		case FINISH_SKIP:
		case FINISH_EXCEPTION:
			logger.error("Camp:{},Job:{}中Sujob:{},Node:{}当前状态:{}",
					new Object[] { subjob.getCampId(), jobId, subjob.getSubjobId(), nodeId, state.toString() });
			return TO_END;
		default:
			return RETURN;
		}
	}
}
