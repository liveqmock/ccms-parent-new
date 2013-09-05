package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.CareConfigDomain;

/**
 * 关怀配置任务
 *
 * @author teng.zeng
 * date 2013-6-13 下午05:31:31
 */
public interface CareConfigRepository extends JpaRepository<CareConfigDomain, Long>{

	/**
	 * 根据关怀类型和店铺查询还开启状态
	 * @param careType 关怀类型
	 * @param dpId 店铺id
	 * @param isOpen TODO
	 * @return
	 */
	CareConfigDomain getByCareTypeAndDpIdAndIsOpen(Integer careType, String dpId, int isOpen);

	/**
	 * 根据关怀类型和店铺查询
	 * @param careType 关怀类型
	 * @param dpId 店铺id
	 * @param isOpen TODO
	 * @return
	 */
	CareConfigDomain getByCareTypeAndDpId(Integer careType, String dpId);


	/**
	 *
	 * 查询所有dateType = 0 and DateNumber = 30，同时is_open = 1的配置
	 *
	 * @return
	 */
	@Query("from CareConfigDomain where date_type = 0 and is_open = 1")
	List<CareConfigDomain> getOpen30DaysCareConfig();


	 @Modifying
	 @Transactional
	 @Query("update CareConfigDomain set is_open = 0,op_user='system',updated=now() where pkid = :pkId")
	 void updateCareStatusByPkId(@Param("pkId")Long pkId);


	 @Query("from CareConfigDomain where is_open = 1 and is_switch = 1 and care_type = :careType")
	 List<CareConfigDomain> getCareConfigByType(@Param("careType")Integer careType);

    @Query("from CareConfigDomain where is_open = 1 and care_type in (:careTypes)")
    List<CareConfigDomain> getOpenedCareConfigs(@Param("careTypes")List<Integer> careTypes);
}
