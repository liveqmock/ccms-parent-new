package com.yunat.ccms.node.support.service;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluateParamObject;

public interface EvaluateReportDownloadService {

	void builderCustomerCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject);

	void builderProductCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject);

	void builderOrderCsvFile(Long subjobId, NodeEvaluateParamObject nodeEvaluateParamObject);

}
