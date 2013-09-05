package com.yunat.ccms.tradecenter.service;

import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.domain.TraderateDomainPK;

/**
 * 评价事务-客户操作- 业务接口(解释 关怀)
 * 
 * @author tim.yin
 */
public interface TraderateCustomerOperate {

	// 客户解释
	public abstract void customerExplain(TraderateDomainPK traderateDomainPK, String reply, String shopId)
			throws Exception;

	// 客户关怀
	public abstract BaseResponse<String> customerRegard(CaringRequest caringRequest);

	// 批量客户关怀
	public abstract BaseResponse<String> batchCustomerRegard(CaringRequest caringRequest);

}
