package com.yunat.ccms.rule.center.runtime.job;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface RcJobService {

	RcJob save(RcJob job);

	void delete(RcJob job);

	List<RcJob> findByStatus(String status);

	List<RcJob> save(List<RcJob> jobList);

	/**
	 * 保存规则引擎处理结果
	 * 
	 * @param log
	 */
	void saveLog(RcJobLog log);

	/**
	 * 获取结束时间在某个时间段内,某方案处理过的订单的处理记录.
	 * 
	 * @param planId
	 * @param from
	 * @param to
	 * @return
	 */
	Collection<RcJobLog> getJobLogsByShopIdEndTimeBetween(String shopId, Date from, Date to);

}
