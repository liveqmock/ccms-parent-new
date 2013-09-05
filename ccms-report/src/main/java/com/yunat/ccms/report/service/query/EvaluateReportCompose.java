package com.yunat.ccms.report.service.query;

import java.util.List;

public interface EvaluateReportCompose {

	// 定义表格列头
	List<String> findColumnHeaders();

	// 生成数据结果集
	StringBuilder builderSQLExpression(String previousNodeOutput);

}
