package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.UrpayConfigDomain;

/**
 * 催付配置信息数据接口
 *
 * @author teng.zeng date 2013-5-31 上午11:48:52
 */
public interface UrpayConfigRepository extends JpaRepository<UrpayConfigDomain, Long> {


	UrpayConfigDomain getByUrpayTypeAndDpId(int urpayType, String dpId);

	/**
     * 根据催付类型获取所有开启此催付类型的催付配置
     *
     * @param urpayType
     * @return
     */
	@Query("from UrpayConfigDomain where urpay_type = :urpayType and is_open = 1 and is_switch = 1")
	List<UrpayConfigDomain> getByUrpayType(@Param("urpayType") int urpayType);

	@Query("from UrpayConfigDomain where urpay_type = :urpayType and task_type = :taskType and is_open = 1 and is_switch = 1")
	List<UrpayConfigDomain> getByUrpayTypeAndTaskType(@Param("urpayType") int urpayType,@Param("taskType") int taskType);

	/**
    *
    * 查询所有dateType = 0 and DateNumber = 30，同时is_open = 1的配置
    *
    * @return
    */
   @Query("from UrpayConfigDomain where date_type = 0 and is_open = 1")
   List<UrpayConfigDomain> getOpen30DaysUrpayConfig();

   @Modifying
   @Transactional
   @Query("update UrpayConfigDomain set is_open = 0,op_user='system',updated=now() where pkid = :pkId")
   void updateUrpayStatusByPkId(@Param("pkId") Long pkId);

}
