package com.yunat.ccms.tradecenter.service;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;

/**
 * 催付配置表
 *
 * @author teng.zeng date 2013-5-31 下午02:12:13
 */
public interface UrpayConfigService {

	/**
	 * 根据催付类型获取所有开启此催付类型的催付配置
	 *
	 * @param urpayType
	 * @return
	 */
	List<UrpayConfigDomain> getUrpayConfigListByType(int urpayType,int taskType);

	/**
	 * 催付配置根据催付类型和店铺id查询
	 * @param urpayType
	 * @param dpId
	 * @return
	 */
	UrpayConfigDomain getByUrpayTypeAndDpId(Integer urpayType, String dpId);

	/**
	 * 催付配置任务保存
	 * @param urpayConfigDomain
	 * @return
	 */
	UrpayConfigDomain saveUrpayConfigDomain(UrpayConfigDomain urpayConfigDomain);
}
