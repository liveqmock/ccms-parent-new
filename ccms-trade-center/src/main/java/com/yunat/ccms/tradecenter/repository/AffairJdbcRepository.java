package com.yunat.ccms.tradecenter.repository;

import java.util.List;
import java.util.Map;

/**
 * 我的事务接口
 * @author Administrator
 *
 */
public interface AffairJdbcRepository{

    /**
     * 通过tid查询子订单信息，包括子订单的所有信息，及评价表的评价result
     * @param tid
     * @return
     */
	List<Map<String, Object>> findOrderItemsInfoByTid(String tid);

	/**
	 * 根据tid获取订单信息,包括物流状态
	 * @param tid
	 * @return
	 */
	Map<String, Object> findOrderInfoByTid(String tid);


}
