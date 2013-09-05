package com.yunat.ccms.node.biz.coupon.taobao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaobaoCouponDenominationRepository extends JpaRepository<TaobaoCouponDenomination, Long> {

}
