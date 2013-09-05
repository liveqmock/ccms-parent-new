package com.yunat.ccms.dashboard.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.dashboard.campaign.helper.CommonExecuteCampStrategy;
import com.yunat.ccms.dashboard.campaign.helper.DashboardCalculate;
import com.yunat.ccms.dashboard.campaign.helper.PreExecuteCampStrategy;
import com.yunat.ccms.dashboard.model.Notice;
import com.yunat.ccms.dashboard.service.NoticeService;

/**
 * ©Copyright：yunat Project：ccms-dashboard Module ID：首页模块 Comments：系统URI JDK
 * version used：<JDK1.6>
 * 
 * Author：yinwei Create Date： 2013-1-19 Version:1.0
 * 
 * Modified By： Modified Date： Why & What is modified： Version：
 */

@Controller
@RequestMapping(value = "/dashboard/system/*")
public class SystemController {

	final String[] executeCampStatus = new String[] { "B3" };

	final String[] preExecuteCampStatus = new String[] { "B1", "B2" };

	final String[] executeingSubJobStatus = new String[] { "11" };

	@Autowired
	CommonExecuteCampStrategy commonExecuteCampStrategy;

	@Autowired
	PreExecuteCampStrategy preExecuteCampStrategy;

	@Autowired
	TaobaoShopService shopService;

	@Autowired
	NoticeService noticeService;

	@Autowired
	DashBoardChannelController channelController;

	/**
	 * 系统监控
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/monitor", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ModelMap systemMonitor(ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws ClassCastException {
		return systemMonitorCommon(model);
	}

	/**
	 * @param request
	 * @return DashboardCalculate(接口)
	 */
	private ModelMap systemMonitorCommon(ModelMap model) {
		DashboardCalculate dashboardExecuteCalculate = commonExecuteCampStrategy;
		Integer executingNodeCount = executeingNodeCount(dashboardExecuteCalculate);

		model.put("executingNodeCount", executingNodeCount);

		DashboardCalculate dashboardPreExecuteCalculate = preExecuteCampStrategy;
		Integer preExecuteNodeCount = preExecutingNodeCount(dashboardPreExecuteCalculate);

		model.put("preExecuteNodeCount", preExecuteNodeCount);

		// 计算系统负载
		double thresholdLevel = systemLoadCalculate(executingNodeCount, preExecuteNodeCount);
		model.put("thresholdLevel", thresholdLevel);
		return model;
	}

	private double systemLoadCalculate(Integer executingNodeCount, Integer preExecuteNodeCount) {
		return ((double) (executingNodeCount) / 70 * 0.7) + ((double) (preExecuteNodeCount) / 15 * 0.3);
	}

	private Integer preExecutingNodeCount(DashboardCalculate dashboardPreExecuteCalculate) {
		Integer preExecuteNodeCount = dashboardPreExecuteCalculate.nodeInfoByComposite(preExecuteCampStatus,
				executeingSubJobStatus).size();
		return preExecuteNodeCount;
	}

	private Integer executeingNodeCount(DashboardCalculate dashboardExecuteCalculate) {
		Integer executingNodeCount = dashboardExecuteCalculate.nodeInfoByComposite(executeCampStatus,
				executeingSubJobStatus).size();
		return executingNodeCount;
	}

	/**
	 * 系统店铺订单更新
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shop/order/lastupdate", method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	public @ResponseBody
	List<TaobaoShop> shopOrderInfo(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		List<TaobaoShop> smList = shopService.list();
		return smList;
	}

	/**
	 * 系统公告
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return example :
	 *         "callback([{'content':'test', 'created':'2012-08-06 17:59:05.0'}])"
	 * @throws Exception
	 * 
	 *             resolve messy code
	 * 
	 * 
	 *             1. 直接写入输出流 remove @ResponseBody
	 *             response.setContentType("text/html;charset=UTF-8");
	 *             response.getWriter
	 *             ().print(jsonpDataFormatWrap(noticeService.noticeInfo()));
	 *             return null;
	 * 
	 * 
	 *             2. user ResponseEntity<String> HttpHeaders responseHeaders =
	 *             new HttpHeaders(); responseHeaders.add("Content-Type",
	 *             "text/html; charset=utf-8"); return new
	 *             ResponseEntity<String>
	 *             (jsonpDataFormatWrap(noticeService.noticeInfo()),
	 *             responseHeaders, HttpStatus.CREATED);
	 * 
	 * 
	 *             3. springmvc 3.1.0 config produces =
	 *             "application/json; charset=utf-8"
	 **/

	@RequestMapping(value = "/notice", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<Notice> systemNotice() throws Exception {
		return noticeService.noticeInfo();
	}

	/**
	 * 信息摘要
	 */

	@RequestMapping(value = "/information", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ModelMap systemInformation(ModelMap model) {

		int MoneyLessTenRMBAccountNum = 0;
		// 得到日期
		Calendar calendar = Calendar.getInstance();
		model.addAttribute("getDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		model.addAttribute("getWeek", calendar.get(Calendar.DAY_OF_WEEK) - 1);

		this.systemMonitorCommon(model);

		List<Map<String, Object>> channelBalanceList = channelController.channelBalanceCommon();
		for (Map<String, Object> balanceMap : channelBalanceList) {
			if ((Double) (balanceMap.get("balance")) < 10) {
				MoneyLessTenRMBAccountNum++;
			}
		}
		model.addAttribute("MoneyLessTenRMBAccountNum", MoneyLessTenRMBAccountNum);
		return model;
	}
}
