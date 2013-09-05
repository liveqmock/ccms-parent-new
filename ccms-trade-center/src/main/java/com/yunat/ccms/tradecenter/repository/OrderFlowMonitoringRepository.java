package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.OrderFlowMonitoringDomain;

public interface OrderFlowMonitoringRepository extends JpaRepository<OrderFlowMonitoringDomain, String> {
	
	@Query("from OrderFlowMonitoringDomain where dp_id = :dpId order by order_id")
	List<OrderFlowMonitoringDomain> findOrderFlowMonitoring(@Param("dpId") String dpId);

}
