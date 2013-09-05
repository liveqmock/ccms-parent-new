package com.yunat.ccms.report.service.command.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluateParamObject;
import com.yunat.ccms.report.service.command.EvaluateReportDownload;
import com.yunat.ccms.report.service.query.EvaluateReportCompose;
import com.yunat.ccms.report.support.cons.ReportFileLocator;
import com.yunat.ccms.report.support.cons.ReportFileSpec;
import com.yunat.ccms.report.support.utils.EvaluateReportCopyAndWriteStorageData;

/**
 * 评估报表-客户明细下载-入口
 * 
 * @author yin
 * 
 */
@Component("evaluateReportCustomerDownloadImpl")
public class EvaluateReportCustomerDownloadImpl implements EvaluateReportDownload {

	@Autowired
	@Qualifier("evaluateReportCustomerComposeImpl")
	private EvaluateReportCompose evaluateReportCompose;

	@Autowired
	private EvaluateReportCopyAndWriteStorageData evaluateReportCopyAndWriteStorageData;

	@Autowired
	private ReportFileLocator reportFileLocator;

	/**
	 * @param subjobId
	 * @param platCode
	 * @param sql
	 */
	private void buildCsvFileHelper(Long subjobId, StringBuilder sql, NodeEvaluateParamObject nodeEvaluateParamObject) {

		String filename = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.EVALUATE_REPORT_CUSTOMER,
				ReportFileSpec.Extension.CSV);
		List<String> columnHeaders = evaluateReportCompose.findColumnHeaders();
		evaluateReportCopyAndWriteStorageData.generate(filename, sql, nodeEvaluateParamObject, columnHeaders);
	}

	@Override
	public void builderCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject) {

		StringBuilder sql = evaluateReportCompose.builderSQLExpression(nodeEvaluateParamObject.getPreviousNodeOutput());
		buildCsvFileHelper(subjobId, sql, nodeEvaluateParamObject);
	}
}
