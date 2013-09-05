package com.yunat.ccms.report.service.command;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluateParamObject;

public interface EvaluateReportDownload {

	public void builderCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject);
}
