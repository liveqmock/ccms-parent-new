package com.yunat.ccms.tradecenter.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yunat.ccms.tradecenter.controller.vo.TraderateVO;

/**
 * 评价事务存储接口
 *
 * @author tim.yin
 */
public interface TraderateRepository {

	public abstract Page<Map<String, Object>> queryTraderateByCondition(TraderateVO TraderateVO, Pageable page);

	/**
	 * 按时间和店铺获取中差评的订单
	 * @param dpId
	 * @param lastDealTime
	 * @return
	 */
	public abstract List<Map<String, Object>> getNotGoodListByDpId(String dpId, Date lastDealTime);

	public abstract boolean findIfDealWithNotGood(String oid);
}
