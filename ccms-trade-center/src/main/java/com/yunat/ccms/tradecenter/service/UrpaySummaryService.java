/**
 *
 */
package com.yunat.ccms.tradecenter.service;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.UrpaySummaryDomain;

/**
 *催付统计接口类
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-3 上午10:17:45
 */
public interface UrpaySummaryService {

	/**
     * 根据催付类型和店铺ID获取催付统计数据
     *
     * @param urpayType,dpId
     * @return
     */
	List<UrpaySummaryDomain> queryUrpaySummaryList(Integer urpayType, String dpId);

	/**
     * 获取一条统计数据
     *
     * @param int urpayType, String dpId, String urpayDate
     * @return
     */
	UrpaySummaryDomain queryUrpaySummaryDomain(Integer urpayType, String dpId, String urpayDate);

	/**
     * 保存统计数据
     *
     * @param UrpaySummaryDomain
     * @return
     */
	void saveUrpaySummaryDomain(UrpaySummaryDomain summary);

}
