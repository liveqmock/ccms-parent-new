package com.yunat.ccms.rule.center.runtime.mq;

import java.util.List;

public interface RcOrderService {

	void save(long batchId, List<String> allTids);

	void updateStatus(long batchId);

	void toRcJob(long batchId);

}
