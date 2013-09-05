package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.yunat.ccms.tradecenter.domain.TraderateDomain;
import com.yunat.ccms.tradecenter.domain.TraderateDomainPK;

/**
 * 评价客户操作存储接口
 * 
 * @author tim.yin
 */

public interface TraderateCustomerOperateRepository extends JpaRepository<TraderateDomain, TraderateDomainPK>,
		CrudRepository<TraderateDomain, TraderateDomainPK> {

}
