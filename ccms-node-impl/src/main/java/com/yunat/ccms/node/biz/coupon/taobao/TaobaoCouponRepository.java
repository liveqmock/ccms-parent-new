package com.yunat.ccms.node.biz.coupon.taobao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaobaoCouponRepository extends JpaRepository<TaobaoCoupon, Long>,
		JpaSpecificationExecutor<TaobaoCoupon> {

	@Query(" select DISTINCT tc from TaobaoCoupon tc where tc.enable=true and tc.shop.shopId=:shopId and tc.endTime > NOW()")
	public List<TaobaoCoupon> getAvaliableCoupons(@Param("shopId") String shopId);

}
