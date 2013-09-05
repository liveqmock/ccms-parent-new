package com.yunat.ccms.report.service.command;

/**
 * 生成下载的接口
 * 
 * @author yin
 * 
 */

public interface ReportDownload {
	public void builderJsonFile(Long subjobId, String platCode);

	public void builderCsvFile(Long subjobId, String platCode);

	public void builderZipFile(Long subjobId, String platCode);
}
