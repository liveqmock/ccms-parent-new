package com.yunat.ccms.rule.center.conf.condition;

import com.yunat.ccms.rule.center.RuleCenterRuntimeException;


public interface ItemsInvokerService {
	/**
	 * 搜索某个店铺的在售商品
	 * @param shopId
	 * @param q
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	ItemsResponse searchProducts(String shopId, String q, long pageNo, long pageSize) throws RuleCenterRuntimeException;
	
	/**
	 * 根据商品Id集合来查找对应的商品信息集
	 * @param shopId
	 * @param numiids
	 * @return
	 */
	ItemsResponse findItemsByCondition(String shopId, String numiids) throws RuleCenterRuntimeException;
}
