/**
 *
 */
package com.yunat.ccms.tradecenter.repository;

import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.LogisticsVO;
import com.yunat.ccms.tradecenter.service.queryobject.LogisticsQuery;

/**
 * 
 * 
 * @author xiahui.zhang
 * @version 创建时间：2013-6-17 下午01:49:54
 */
public interface MQlogisticsRepository {

	/**
	 * @return
	 */
	List<Map<String, Object>> queryShopId();

	/**
	 * @return
	 */
	List<Map<String, Object>> queryOrderMQ();

	/**
	 * @param tidList
	 * @return
	 */
	List<Map<String, Object>> queryStepinfoTmp(List<String> tidList);

	/**
	 * @param tidList
	 * @return
	 */
	List<Map<String, Object>> queryOrderTC(List<String> tidList);

	/**
	 * @param tidList
	 * @return
	 */
	List<Map<String, Object>> queryCareStatus(List<String> tidList);

	/**
	 * @param tidList
	 * @return
	 */
	void deleteStepinfoTmp(List<String> tidList);

	/**
	 * 获取物流事务列表
	 * 
	 * @param logisticsQuery
	 * @param abnormalDays
	 * @return
	 */
	List<LogisticsVO> findWorkLogisticsList(LogisticsQuery logisticsQuery, Integer abnormalDays);

	/**
	 * 查询订单的物流信息
	 * 
	 * @param tid
	 * @return
	 */
	Map<String, Object> queryStansitStepInfo(String tid);
}
