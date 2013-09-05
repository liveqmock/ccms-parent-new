package com.yunat.ccms.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.LogisticsRequest;
import com.yunat.ccms.tradecenter.controller.vo.LogisticsVO;
import com.yunat.ccms.tradecenter.domain.OrderDomain;
import com.yunat.ccms.tradecenter.domain.TransitstepinfoDomain;
import com.yunat.ccms.tradecenter.service.queryobject.LogisticsQuery;

/**
 * The Interface LogisticsService.
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-17 下午08:29:08
 */
public interface LogisticsService {

	/**
	 * Update timeout by tid.
	 *
	 * @param extensionDays the extension days
	 * @param tids the tids
	 * @return the int
	 */
	public int updateTimeoutByTid(Long extensionDays, String... tids);

	/**
	 * Update timeout action times.
	 *
	 * @param req
	 *            the req
	 * @return the int
	 */
	int updateTimeoutActionTimes(LogisticsRequest req);

	/**
	 * Save transitstepinfo domain.
	 *
	 * @param domain
	 *            the domain
	 */
	void saveTransitstepinfoDomain(TransitstepinfoDomain domain);

	/**
	 * Analysis transitstepinfo list.
	 *
	 * @param infoList
	 *            the info list
	 * @param orderMap
	 *            the order map
	 * @param SIGNED
	 *            the sIGNED
	 * @param REJECT
	 *            the rEJECT
	 * @param DELIVERY
	 *            the dELIVERY
	 * @return the map
	 */
	Map<String, TransitstepinfoDomain> analysisTransitstepinfoList(List<Map<String, Object>> infoList,
			Map<String, OrderDomain> orderMap, String SIGNED, String REJECT, String DELIVERY);

	/**
	 * 获取物流事务列表.
	 *
	 * @param logisticsQuery the logistics query
	 * @return the list
	 */
	List<LogisticsVO> findWorkLogisticsList(LogisticsQuery logisticsQuery);

	/**
	 * 获取订单的物流信息清单.
	 *
	 * @param tid the tid
	 * @return the transit step info
	 */
	Map<String, Object> getTransitStepInfo(String tid);
}
