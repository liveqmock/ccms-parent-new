package com.yunat.ccms.report.service.command.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yunat.ccms.report.service.command.ReportDownload;
import com.yunat.ccms.report.service.query.ReportCompose;
import com.yunat.ccms.report.support.cons.ReportFileLocator;
import com.yunat.ccms.report.support.cons.ReportFileSpec;
import com.yunat.ccms.report.support.utils.CopyAndWriteStorageData;

/**
 * 客户组下载-入口
 * 
 * @author yin
 * 
 */

@Component("customerReportDownloadImpl")
public class CustomerReportDownloadImpl implements ReportDownload {

	@Autowired
	@Qualifier("customerReportComposeImpl")
	private ReportCompose reportCompose;

	@Autowired
	private CopyAndWriteStorageData copyAndWriteStorageData;

	@Autowired
	private ReportFileLocator reportFileLocator;

	@Override
	public void builderCsvFile(Long subjobId, String platCode) {
		StringBuilder sql = new StringBuilder(reportCompose.builderSQLExpression(subjobId, platCode))
				.append(" limit 100 offset 0 ");
		buildCsvFileHelper(subjobId, platCode, sql);
	}

	/**
	 * @param subjobId
	 * @param platCode
	 * @param sql
	 */
	private void buildCsvFileHelper(Long subjobId, String platCode, StringBuilder sql) {
		String filename = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.CUSTOMER,
				ReportFileSpec.Extension.CSV);
		List<String> columnHeaders = reportCompose.findColumnHeaders(platCode);
		copyAndWriteStorageData.generate(filename, sql, columnHeaders);
	}

	@Override
	public void builderZipFile(Long subjobId, String platCode) {
		StringBuilder sql = new StringBuilder(reportCompose.builderSQLExpression(subjobId, platCode));
		buildCsvFileHelper(subjobId, platCode, sql);
		String filename = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.CUSTOMER,
				ReportFileSpec.Extension.CSV);
		copyAndWriteStorageData.compressToZip(filename);
	}

	@Override
	public void builderJsonFile(Long subjobId, String platCode) {
		String filename = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.CUSTOMER,
				ReportFileSpec.Extension.JSON);
		StringBuilder sql = new StringBuilder(reportCompose.builderSQLExpression(subjobId, platCode));
		List<String> columnHeaders = reportCompose.findColumnHeaders(platCode);
		copyAndWriteStorageData.generate(filename, sql, columnHeaders);
	}

}