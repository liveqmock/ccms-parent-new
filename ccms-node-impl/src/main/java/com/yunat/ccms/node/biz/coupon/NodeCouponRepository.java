package com.yunat.ccms.node.biz.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeCouponRepository extends JpaRepository<NodeCoupon, Long> {

}
