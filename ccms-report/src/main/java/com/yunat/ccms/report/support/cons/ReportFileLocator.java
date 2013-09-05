package com.yunat.ccms.report.support.cons;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.io.FileUtils;
import com.yunat.ccms.report.support.cons.ReportFileSpec.Category;
import com.yunat.ccms.report.support.cons.ReportFileSpec.Extension;

/**
 * 文件名特定标识
 */
@Component
public class ReportFileLocator implements InitializingBean {

	private static final String EXPORT_PATH = "export";
	private static final String DOWNLOAD_PATH = "download";
	private static final String DOWNLOAD_FILE_NAME_PREFIX = "dataFile_";

	private String reportFilePath;

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	public String buildFilename(Long subjobId, Category fileCategory, Extension suffix) {
		String fileName = fileCategory.toString().toLowerCase() + "_" + subjobId;
		String name = DOWNLOAD_FILE_NAME_PREFIX + fileName + "." + suffix.toString().toLowerCase();
		return reportFilePath + name;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String fileStorePath = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_UPLOAD_DIR);
		String reportFilePath = FileUtils.concat(fileStorePath, EXPORT_PATH, DOWNLOAD_PATH);
		FileUtils.createFolder(reportFilePath);

	}
}