package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.AffairsVO;
import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.service.queryobject.AffairsQuery;

public interface AffairService {

	/**
	 * 新增事务
	 * @param affairDomain
	 */
	void addAffair(AffairDomain affairDomain);

	/**
	 * 修改事务
	 * @param affairDomain
	 */
	void modifyAffair(AffairDomain affairDomain);

    /**
     * 查找
     * @param affairsQuery
     */
    List<AffairsVO> findAffairs(AffairsQuery affairsQuery);

    /**
     * 根据事务id查询单个事务详情
     * @param affair_id
     * @return
     */
    AffairDomain findAffair(Long affair_id);

    /**
     * 根据tid获取订单信息,包括物流状态
     * @param tid
     * @return
     */
    Map<String, Object> findOrderInfoByTid(String tid);

	/**
	 * 通过tid查询子订单信息，包括子订单的所有信息，及评价表的评价result
	 * @param tid
	 * @return
	 */
	List<Map<String, Object>> findOrderItemsInfoByTid(String tid);

}
