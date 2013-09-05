package com.yunat.ccms.tradecenter.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.BuyerInteractionStatisticDomain;

/**
 *  买家交互统计数据库接口
 *
 * @author 李卫林
 *
 */
public interface BuyerInteractionStatisticRepository extends CrudRepository<BuyerInteractionStatisticDomain, Long>{


	/**
	 * 获取制定日期的统计指标
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("from BuyerInteractionStatisticDomain where dealDate between :startDate and :endDate order by dealDate desc")
	List<BuyerInteractionStatisticDomain> getByDealDateBetween(@Param("startDate")Date startDate, @Param("endDate")Date endDate);

	List<BuyerInteractionStatisticDomain> getByDealDateAndDpId(Date dealDate, String dpId);

	List<BuyerInteractionStatisticDomain> getByDpId(String dpId);

	/**
	 * 根据店铺id和昵称列表获取
	 * @param dpId
	 * @param customernos
	 * @return
	 */
	@Query("from BuyerInteractionStatisticDomain where dpId = :dpId and customerno in (:customernos)")
	List<BuyerInteractionStatisticDomain> getByCustomernos(@Param("dpId")String dpId, @Param("customernos")List<String> customernos);

}
