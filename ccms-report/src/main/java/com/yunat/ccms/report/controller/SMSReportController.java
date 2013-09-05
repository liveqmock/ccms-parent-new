package com.yunat.ccms.report.controller;

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

import com.google.common.collect.Maps;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.node.biz.sms.ExecutionRecord;
import com.yunat.ccms.node.biz.sms.NodeSMSQuery;
import com.yunat.ccms.node.support.service.NodeJobService;
import com.yunat.ccms.report.support.utils.NodeReportUrlBuilder;

@Controller
public class SMSReportController {
	private static Logger logger = LoggerFactory.getLogger(SMSReportController.class);

	@Autowired
	private NodeJobService nodeJobService;

	@Autowired
	private NodeSMSQuery nodeSMSQuery;

	@Autowired
	private NodeReportUrlBuilder nodeReportUrlBuilder;

	@ResponseBody
	@RequestMapping(value = "/node/sms/report/{nodeId}", method = RequestMethod.GET)
	public Map<String, Object> showReport(@PathVariable Long nodeId, @RequestParam Long campaignId,
			@RequestParam Long jobId) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Long subjobId = nodeJobService.getSubjobId(jobId, nodeId);
			ExecutionRecord executionRecord = nodeSMSQuery.findByNodeIdAndSubjobId(nodeId, subjobId);
			result.put("targetGroupCustomers", executionRecord.getTargetGroupCustomers());
			result.put("controlGroupCustomers", executionRecord.getControlGroupCustomers());
			result.put("validPhoneAmount", executionRecord.getValidPhoneAmount());
			result.put("invalidPhoneAmount", executionRecord.getInvalidPhoneAmount());
			result.put("reportUrl", nodeReportUrlBuilder.reportUrl(campaignId, nodeId));
			return result;
		} catch(Exception ex) {
			logger.info("show report happend Exception : {}", ex);
			throw new CcmsBusinessException("show report happend Exception");
		}
	}
}