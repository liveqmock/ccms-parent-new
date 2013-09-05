package com.yunat.ccms.report.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.report.domain.EvaluateReportDayResult;
import com.yunat.ccms.report.domain.EvaluateReportResult;
import com.yunat.ccms.report.service.EvaluateReportService;

/**
 * 评估节点统计 ©Copyright：yunat Project：ccms-report Module ID：报表模块
 * Comments：评估节点包括(1.效果评估汇总 2.按日效果评估 3.按小时效果评估) used：<JDK1.6>
 * 
 * Author：yinwei Create Date： 2013-5-3 Version:1.0
 * 
 * Modified By： Modified Date： Why & What is modified： Version：
 */

@Controller
public class EvaluateReportStatisticsController {

	private Logger logger = LoggerFactory.getLogger(EvaluateReportStatisticsController.class);

	@Autowired
	EvaluateReportService evaluateReportService;

	/**
	 * 获取评估效果汇总数据
	 */
	@RequestMapping(value = "/node/evaluate/{nodeId}/grid/collect", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getGridCollectData(@RequestParam Long jobId, @PathVariable Long nodeId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = evaluateReportService.getCollectDataByTotal(jobId, nodeId);
		} catch (Exception e) {
			logger.info("获取评估效果汇总数据出现异常:: [节点id {}, info {}]", nodeId, e);
			throw new Exception("获取评估效果汇总数据异常！");
		}
		return map;

	}

	/**
	 * 获取评估效果表格数据-按天
	 * 
	 * @param nodeEvaluate
	 * @throws Exception
	 */
	@RequestMapping(value = "/node/evaluate/{nodeId}/grid/day", method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluateReportDayResult> getGridDataByDay(@RequestParam Long jobId, @PathVariable Long nodeId)
			throws Exception {
		List<EvaluateReportDayResult> resultSet = new ArrayList<EvaluateReportDayResult>();
		try {
			resultSet = evaluateReportService.getCollectDataByDay(jobId, nodeId);
		} catch (Exception e) {
			logger.info("获取评估效果表格按天数据出现异常:: [节点id {}, info {}]", nodeId, e);
			throw new Exception("获取评估报表按天数据异常！");
		}
		return resultSet;
	}

	/**
	 * 获取评估效果表格数据-按小时
	 * 
	 * @param nodeEvaluate
	 * @throws Exception
	 */
	@RequestMapping(value = "/node/evaluate/{nodeId}/grid/hour", method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluateReportResult> getGridDataByHour(@RequestParam Long jobId, @PathVariable Long nodeId,
			@RequestParam String evaluateTime) throws Exception {
		List<EvaluateReportResult> resultSet = new ArrayList<EvaluateReportResult>();
		try {
			resultSet = evaluateReportService.getCollectDataByHour(jobId, nodeId, evaluateTime);
		} catch (Exception e) {
			logger.info("获取评估效果表格按小时数据出现异常:: [节点id {}, info {}]", nodeId, e);
			throw new Exception("获取评估报表按小时数据异常！");
		}
		return resultSet;
	}

}
