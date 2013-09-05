package com.yunat.ccms.schedule.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.schedule.controler.vo.ScheduleResult;
import com.yunat.ccms.schedule.core.Task;
import com.yunat.ccms.schedule.core.TaskRepository;
import com.yunat.ccms.schedule.core.TaskScheduler;
import com.yunat.ccms.schedule.core.task.CampTask;
import com.yunat.ccms.schedule.core.task.TaskNamingUtil;
import com.yunat.ccms.schedule.core.task.TaskRegistry;
import com.yunat.ccms.schedule.repository.CampaignDao;
import com.yunat.ccms.schedule.support.ParamHolder;

/**
 * 
 * @author xiaojing.qu
 * 
 */
@Component
@Scope("singleton")
public class ScheduleService {

	private static Logger logger = LoggerFactory.getLogger(ScheduleService.class);

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private CampaignDao campaignDao;

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private TaskRegistry tastRegistry;

	@Autowired
	private RecoverService recoverService;

	@Autowired
	private JobLogService jobLogService;

	/**
	 * 执行活动
	 * 
	 * @param campid
	 *            活动ID
	 * @param isTest
	 *            是否是测试执行
	 * @throws Exception
	 */
	public ControlerResult execute(Long campaignId, boolean isTest) {
		if (!checkPermission()) {
			logger.info("没有执行权限");
			return ControlerResult.newError("没有执行权限");
		}
		// check and change status
		Campaign campaign = campaignDao.get(campaignId);
		if (campaign == null) {
			logger.info("活动不存在");
			return ControlerResult.newError("活动不存在");
		}
		CampaignState state = CampaignState.fromCode(campaign.getCampStatus().getStatusId());
		if (state.isRunning()) {
			logger.info("活动当前正在执行中");
			return ControlerResult.newError("活动当前正在执行中");
		}
		CampaignState newState = isTest ? state.handleTest() : state.handlExecute();
		CampaignStatus campStatus = new CampaignStatus(newState.getCode());
		campaign.setCampStatus(campStatus);
		campaignDao.update(campaign);
		ParamHolder params = new ParamHolder();
		jobLogService.cleanJobBeforeExecute(campaignId, isTest);
		CampTask task = taskRepository.createCampTask(campaignId, isTest, params);
		try {
			taskScheduler.submit(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ScheduleResult scheduleVo = new ScheduleResult();
		scheduleVo.setCampaignId(campaignId);
		scheduleVo.setCampaignStatus(newState.getCode());
		scheduleVo.setPrevStatus(state.getCode());
		return ControlerResult.newSuccess(scheduleVo);
	}

	/**
	 * 停止活动
	 * 
	 * @param campid
	 * @param isTest
	 * @throws Exception
	 */
	public ControlerResult stop(Long campaignId, boolean isTest) {
		if (!checkPermission()) {
			logger.info("没有执行权限");
			return ControlerResult.newError("没有执行权限");
		}
		Campaign campaign = campaignDao.get(campaignId);
		if (campaign == null) {
			logger.info("活动不存在");
			return ControlerResult.newError("活动不存在");
		}
		CampaignState state = CampaignState.fromCode(campaign.getCampStatus().getStatusId());
		if (!state.isRunning()) {
			logger.info("stop操作错误,活动当前不在执行中");
			return ControlerResult.newError("中止操作错误,活动当前不在执行中");
		}
		if (isTest != state.isTesting()) {
			logger.info("stop操作错误,活动当前正在" + (state.isTesting() ? "测试" : "正式") + "执行中");
			return ControlerResult.newError("中止操作错误,活动当前不在执行中");
		}
		CampaignState newState = state.handleStop();
		CampaignStatus campStatus = new CampaignStatus(newState.getCode());
		campaign.setCampStatus(campStatus);
		campaignDao.update(campaign);
		jobLogService.resetJobAfterStop(campaignId, isTest);
		String taskId = TaskNamingUtil.getCampTaskId(campaignId, isTest);
		Task task = tastRegistry.getTaskById(taskId);
		if (task == null) {
			return ControlerResult.newError("活动任务不存在或者不在执行中");
		}
		try {
			taskScheduler.cancel(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ScheduleResult scheduleVo = new ScheduleResult();
		scheduleVo.setCampaignId(campaignId);
		scheduleVo.setCampaignStatus(newState.getCode());
		scheduleVo.setPrevStatus(state.getCode());
		return ControlerResult.newSuccess(scheduleVo);
	}

	/**
	 * 恢复节点
	 * 
	 * @param jobId
	 * @param nodeId
	 * @throws Exception
	 */
	public ControlerResult recover(Long jobId, Long nodeId) {
		if (!checkPermission()) {
			logger.info("没有执行权限");
			return ControlerResult.newError("没有执行权限");
		}
		try {
			boolean result = recoverService.recoverNode(jobId, nodeId);
			Map<String, Object> map = Maps.newHashMap();
			map.put("recovered", result);
			return ControlerResult.newSuccess(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ControlerResult.newError("出现未知错误");
	}

	private boolean checkPermission() {
		return true;
	}

}
