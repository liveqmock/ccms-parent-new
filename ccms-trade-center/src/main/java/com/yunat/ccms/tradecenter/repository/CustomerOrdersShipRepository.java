package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.CustomerOrdersShipDomain;

/**
 * 接口操作客服订单关系实体
 *
 * @author ming.peng
 * @date 2013-6-6
 * @since 4.1.0
 */
public interface CustomerOrdersShipRepository extends Repository<CustomerOrdersShipDomain, Long>{

//	@Transactional(propagation= Propagation.REQUIRED)
//	@Query("select u.serviceStaffName from CustomerOrdersShipDomain u where u.dpId = :dpId group by u.serviceStaffName")
//	List<String> getWwListByDpId(@Param("dpId")String dpId);

	/**
	 * 根据订单ID 得到客服订单关系实体
	 * @param tid 订单ID
	 * @return
	 */
	@Query("select u from CustomerOrdersShipDomain u where u.tid = :tid")
	CustomerOrdersShipDomain findCusOrdsByTid(@Param("tid")String tid);

	/**
	 * 获取指定订单列表的数据
	 * @param tids
	 * @return
	 */
	@Query("from CustomerOrdersShipDomain where tid in (:tids)")
	List<CustomerOrdersShipDomain> getByTidIn(@Param("tids")List<String> tids);

}
