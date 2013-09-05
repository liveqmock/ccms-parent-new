package com.yunat.ccms.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.report.controller.vo.CouponSendReport;
import com.yunat.ccms.report.service.CouponSendReportService;

/**
 * 优惠券节点发送报告
 * 
 * @author xiaojing.qu
 * 
 */
@Controller
@RequestMapping(value = "/node/coupon/report/*")
public class CouponReportController {

	@Autowired
	private CouponSendReportService couponSendReportService;

	@ResponseBody
	@RequestMapping(value = "/{nodeId}", method = RequestMethod.GET)
	public ControlerResult getReportUrl(@PathVariable Long nodeId, @RequestParam("jobId") Long jobId) {
		CouponSendReport report = couponSendReportService.getSendReport(nodeId, jobId);
		return ControlerResult.newSuccess(report);
	}

}
