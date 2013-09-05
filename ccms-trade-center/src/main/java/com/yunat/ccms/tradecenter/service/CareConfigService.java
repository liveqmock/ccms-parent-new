package com.yunat.ccms.tradecenter.service;

import java.util.List;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;

/**
 * 关怀配置服务层
 *
 * @author teng.zeng
 * date 2013-6-13 下午05:33:24
 */
public interface CareConfigService {

	/**
	 * 根据关怀类型和店铺查询关怀配置信息
	 * @param careType
	 * @param dpId
	 * @return
	 */
	CareConfigDomain getByCareTypeAndDpId(Integer careType, String dpId);

	/**
	 * 保存关怀配置信息
	 * @param careConfigDomain
	 * @return
	 */
	CareConfigDomain saveCareConfigDomain(CareConfigDomain careConfigDomain);


	/**
	 * 根据关怀类型获取关怀配置
	 *
	 * @param careType
	 * @return
	 */
	List<CareConfigDomain> getByCareType(Integer careType);

}
