package com.yunat.ccms.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.node.support.service.NodeJobService;
import com.yunat.ccms.report.controller.vo.CouponSendReport;
import com.yunat.ccms.report.support.utils.NodeReportUrlBuilder;

@Service
public class CouponSendReportService {

	@Autowired
	private NodeJobService nodeJobService;

	@Autowired
	private NodeReportUrlBuilder nodeReportUrlBuilder;

	public CouponSendReport getSendReport(Long nodeId, Long jobId) {
		Long subjobId = nodeJobService.getSubjobId(jobId, nodeId);
		Long campaignId = nodeJobService.getCampIdByJobId(jobId);
		CouponSendReport sendReport = new CouponSendReport();
		if (subjobId == null) {
			sendReport.setShowReport(false);
		} else {
			sendReport.setShowReport(true);
			sendReport.setReportUrl(nodeReportUrlBuilder.reportUrl(campaignId, nodeId));
		}
		return sendReport;
	}
}
