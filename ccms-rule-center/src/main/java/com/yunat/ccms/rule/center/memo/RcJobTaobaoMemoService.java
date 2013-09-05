package com.yunat.ccms.rule.center.memo;

import java.util.List;

public interface RcJobTaobaoMemoService {

	/**
	 * 根据状态查询
	 * 
	 * @param status
	 * @return
	 */
	List<RcJobTaobaoMemo> findByStatus(String status);

	/**
	 * 保存
	 * 
	 * @param job
	 * @return
	 */
	RcJobTaobaoMemo saveOrUpdate(RcJobTaobaoMemo job);

	/**
	 * 将该笔订单计入统计
	 * 
	 * @param shopId
	 * @param tid
	 */
	void enableJobCountFlag(String shopId, String tid);
}
