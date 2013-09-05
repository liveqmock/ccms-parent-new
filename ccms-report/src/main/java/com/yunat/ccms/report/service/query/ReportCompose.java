package com.yunat.ccms.report.service.query;

import java.util.List;

/**
 * 报表基础构成接口
 * 
 * @author yin
 * 
 */
public interface ReportCompose {
	// 定义表格列头
	List<String> findColumnHeaders(String platCode);

	// 定义生成数据的sql脚本
	String builderSQLExpression(Long subjobId, String platCode);
}
