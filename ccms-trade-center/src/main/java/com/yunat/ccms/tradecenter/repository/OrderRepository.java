package com.yunat.ccms.tradecenter.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.OrderDomain;

/**
 *  买家交互统计数据库接口
 *
 * @author 李卫林
 *
 */
public interface OrderRepository extends CrudRepository<OrderDomain, String>{

	/**
	 * 获取指定日期区间的订单
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("from OrderDomain where modified between :startDate and :endDate and dpId = :dpId order by modified asc")
	List<OrderDomain> findByModifiedBetween(@Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("dpId")String dpId);

	@Query("from OrderDomain where created between :startDate and :endDate and modified < :modifyEndDate and dpId = :dpId")
	List<OrderDomain> findByCreatedBetween(@Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("modifyEndDate")Date modifyEndDate, @Param("dpId")String dpId);

	@Query("from OrderDomain o where created between :startDate and :endDate and dpId = :dpId")
	List<OrderDomain> findByCreatedBetween(@Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("dpId")String dpId);

	@Query("from OrderDomain o where created between :startDate and :endDate and dpId = :dpId and customerno in (:customernos)")
	List<OrderDomain> findByCreatedBetween(@Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("dpId")String dpId, @Param("customernos")List<String> customernos);

	/**
	 *
	 * 根据店铺，买家查询最近2天所有付款的订单
	 * @param dpId
	 * @param dataDate
	 * @return
	 */
	@Query("from OrderDomain where  dpId = :dpId and customerno =:buyer and date(payTime) >= :startDate and date(payTime) <= :endDate")
	List<OrderDomain> findPayedOrder(@Param("dpId")String dpId,@Param("buyer") String buyer,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

	/**
	 * 查询指定tid列表的订单
	 * @param tids
	 * @return
	 */
	@Query("from OrderDomain where tid in (:tids)")
	List<OrderDomain> findOrderByTids(@Param("tids")List<String> tids);

	@Query("select count(n) from OrderDomain n where created >= :startDate and dpId = :dpId and status = :status")
	Integer countByOrderCreatedBetween(@Param("startDate")Date startDate, @Param("dpId")String dpId, @Param("status")String status);


	/** 获取订单表最大时间 **/
	@Query("select max(n.modified) from OrderDomain n where dpId = :dpId")
	Date getOrderMaxUpdateTime(@Param("dpId")String dpId);

}
