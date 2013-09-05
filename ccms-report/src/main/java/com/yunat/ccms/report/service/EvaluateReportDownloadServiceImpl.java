package com.yunat.ccms.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluateParamObject;
import com.yunat.ccms.node.support.service.EvaluateReportDownloadService;
import com.yunat.ccms.report.service.command.EvaluateReportDownload;

@Component
public class EvaluateReportDownloadServiceImpl implements EvaluateReportDownloadService {

	@Autowired
	@Qualifier("evaluateReportCustomerDownloadImpl")
	private EvaluateReportDownload evaluateReportCustomerDownloadImpl;

	@Autowired
	@Qualifier("evaluateReportProductDownloadImpl")
	private EvaluateReportDownload evaluateReportProductDownloadImpl;

	@Autowired
	@Qualifier("evaluateReportOrderDownloadImpl")
	private EvaluateReportDownload evaluateReportOrderDownloadImpl;

	@Override
	public void builderCustomerCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject) {

		evaluateReportCustomerDownloadImpl.builderCsvFile(subjobId, nodeEvaluateParamObject);

	}

	@Override
	public void builderProductCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject) {

		evaluateReportProductDownloadImpl.builderCsvFile(subjobId, nodeEvaluateParamObject);

	}

	@Override
	public void builderOrderCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject) {

		evaluateReportOrderDownloadImpl.builderCsvFile(subjobId, nodeEvaluateParamObject);

	}

}
