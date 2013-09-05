package com.yunat.ccms.node.support.service;

public interface CustomerReportService {

	void builderJsonFile(Long subjobId, String platcode);

	void builderCsvFile(Long subjobId, String platcode);

	void builderZipFile(Long subjobId, String platcode);

}
