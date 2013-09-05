package com.yunat.ccms.dashboard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.dashboard.campaign.helper.CommonExecuteCampStrategy;
import com.yunat.ccms.dashboard.campaign.helper.DashboardCalculate;
import com.yunat.ccms.dashboard.campaign.helper.ErrorExecuteCampStrategy;
import com.yunat.ccms.dashboard.service.ChartService;

/**
 * ©Copyright：yunat Project：ccms-dashboard Module ID：首页模块 Comments：活动URI JDK
 * version used：<JDK1.6>
 * 
 * Author：yinwei Create Date： 2013-1-19 Version:1.0
 * 
 * Modified By： Modified Date： Why & What is modified： Version：
 */

@Controller
@RequestMapping(value = "/dashboard/campaign/*")
public class DashBoardCampaignController {
	static final String campaignExecuteCompleteStatusCode = "A5";

	static final String campaignExecuteErrorStatusCode = "A6";

	@Autowired
	private ChartService chartService;

	@Autowired
	private CommonExecuteCampStrategy commonExecuteCampStrategy;

	@Autowired
	private ErrorExecuteCampStrategy errorExecuteCampStrategy;

	/**
	 * 活动监控
	 * 
	 * @param campaignState
	 *            :活动状态
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/monitor", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	List<Map<String, Object>> campaignMonitor(@RequestParam String campaignStatus, ModelMap mode) throws Exception {
		List<Map<String, Object>> resultCampList = new ArrayList<Map<String, Object>>();
		DashboardCalculate dashboardExecuteCalculate;
		dashboardExecuteCalculate = campaignExecuteErrorStatusCode.equals(campaignStatus) ? errorExecuteCampStrategy
				: commonExecuteCampStrategy;
		resultCampList = dashboardExecuteCalculate.campInfoByStatus(campaignStatus);
		return resultCampList;
	}

	// 活动状态
	@RequestMapping(value = "/states", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public String campaignStates() throws Exception {
		// TODO
		return null;
	}

	/**
	 * 活动趋势图表数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/trend/chartdata", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String campaignTrendChartData() throws Exception {
		// the finall JSONArray
		JSONArray jsonArrayResult = new JSONArray();
		jsonArrayResult.add(chartCommonUtil(executeCompleteCampaign(campaignExecuteCompleteStatusCode)));
		// 新建活动
		jsonArrayResult.add(chartCommonUtil(executeCompleteCampaign(null)));
		return jsonArrayResult.toString();
	}

	private List<Map<String, Object>> executeCompleteCampaign(String campaignStatus) {
		List<Map<String, Object>> executeCompleteCampResult = chartService.chartDataByCampaignStatus(campaignStatus);
		return executeCompleteCampResult;
	}

	private JSONArray chartCommonUtil(List<Map<String, Object>> compositeList) {
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : compositeList) {
			JSONArray json = new JSONArray();
			json.add(map.get("fullTime"));
			json.add(map.get("CampCount"));
			jsonArray.add(json);
		}
		return jsonArray;
	}

}
