package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.OrderItemDomain;

public interface OrderItemRepository extends JpaRepository<OrderItemDomain, String> {

	/**
	 * 根据订单id列表获取子订单列表
	 *
	 * @return
	 */
	@Query("from OrderItemDomain where tid in (:tids)")
	List<OrderItemDomain> findOrderItems(@Param("tids") List<String> tids);

	/**
	 * 根据指定条件获取子订单列表
	 *
	 * @param modified
	 * @param dpId
	 *            店铺ID
	 * @param sellerRate
	 *            卖家是否已评价
	 * @param buyerRate
	 *            买家是否已评价
	 * @return
	 */
	@Query("from OrderItemDomain where dp_id = :dpId and item_order_updated >= :modified and seller_rate = :sellerRate order by item_order_updated")
	List<OrderItemDomain> findOrderItems(@Param("modified") String modified, @Param("dpId") String dpId,
			@Param("sellerRate") Boolean sellerRate, Pageable pageble);

	/**
	 * 根据指定条件获取子订单列表
	 *
	 * @param modified
	 * @param dpId
	 *            店铺ID
	 * @param sellerRate
	 *            卖家是否已评价
	 * @param buyerRate
	 *            买家是否已评价
	 * @return
	 */
	@Query("from OrderItemDomain where dp_id = :dpId and item_order_updated >= :modified and seller_rate = :sellerRate and buyer_rate = :buyerRate order by item_order_updated")
	List<OrderItemDomain> findOrderItems(@Param("modified") String modified, @Param("dpId") String dpId,
			@Param("sellerRate") Boolean sellerRate, @Param("buyerRate") Boolean buyerRate, Pageable pageble);

	/**
	 * 统计指定条件获取子订单列表
	 *
	 * @param modified
	 * @param dpId
	 *            店铺ID
	 * @param sellerRate
	 *            卖家是否已评价
	 * @param buyerRate
	 *            买家是否已评价
	 * @return
	 */
	@Query("select count(n) from OrderItemDomain n where dp_id = :dpId and item_order_updated >= :modified and seller_rate = :sellerRate")
	Long countOrderItems(@Param("modified") String modified, @Param("dpId") String dpId,
			@Param("sellerRate") Boolean sellerRate);

	/**
	 * 统计指定条件获取子订单列表
	 *
	 * @param modified
	 * @param dpId
	 *            店铺ID
	 * @param sellerRate
	 *            卖家是否已评价
	 * @param buyerRate
	 *            买家是否已评价
	 * @return
	 */
	@Query("select count(n) from OrderItemDomain n where dp_id = :dpId and item_order_updated >= :modified and seller_rate = :sellerRate and buyer_rate = :buyerRate")
	Long countOrderItems(@Param("modified") String modified, @Param("dpId") String dpId,
			@Param("sellerRate") Boolean sellerRate, @Param("buyerRate") Boolean buyerRate);

}
