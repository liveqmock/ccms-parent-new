package com.yunat.ccms.rule.center.runtime.job;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RcJobLogRepository extends JpaRepository<RcJobLog, Long> {

	List<RcJobLog> findByShopIdAndEndTimeBetween(String shopId, Date from, Date to);

	@Modifying
	@Query("update RcJobLog set countFlag=true where shopId=?1 and tid = ?2 ")
	void enableJobCountFlag(String shopId, String tid);
}
