package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.support.bean.AssociatBeanList;



/**
 * 关怀服务
 * @author 李卫林
 *
 */
public interface CareService {

	/**
	 * 同城关怀
	 * @param orderDomainList TODO
	 */
	void cityCare(Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> dpIdToOrderShip);

	/**
	 * 派送关怀
	 * @param orderDomainList TODO
	 */
	void deliveryCare(Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> dpIdToOrderShip);

	/**
	 * 签收关怀
	 * @param orderDomainList TODO
	 */
	void signCare(Map<String, AssociatBeanList<OrderDomain, TransitstepinfoDomain>> dpIdToOrderShip);

	/**
	 * 确认收货关怀
	 * @param dpIdOrderDomainsMap
	 */
	void confirmCare(Map<String, List<OrderDomain>> dpIdOrderDomainsMap);
}
