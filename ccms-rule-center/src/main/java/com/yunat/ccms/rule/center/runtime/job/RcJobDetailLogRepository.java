package com.yunat.ccms.rule.center.runtime.job;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RcJobDetailLogRepository extends JpaRepository<RcJobDetailLog, Long> {

	List<RcJobDetailLog> findBytidAndPlanId(String tid, long planId);

	@Modifying
	@Query("update RcJobDetailLog set countFlag=true where shopId=?1 and tid = ?2 ")
	void enableJobCountFlag(String shopId, String tid);
}
