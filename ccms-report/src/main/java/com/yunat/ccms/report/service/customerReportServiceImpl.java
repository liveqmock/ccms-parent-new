package com.yunat.ccms.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.support.service.CustomerReportService;
import com.yunat.ccms.report.service.command.ReportDownload;

@Component
public class customerReportServiceImpl implements CustomerReportService {

	@Autowired
	@Qualifier("customerReportDownloadImpl")
	private ReportDownload reportDownload;

	@Override
	public void builderJsonFile(Long subjobId, String platCode) {
		reportDownload.builderJsonFile(subjobId, platCode);
	}

	@Override
	public void builderCsvFile(Long subjobId, String platcode) {
		reportDownload.builderCsvFile(subjobId, platcode);

	}

	@Override
	public void builderZipFile(Long subjobId, String platCode) {
		reportDownload.builderZipFile(subjobId, platCode);
	}

}