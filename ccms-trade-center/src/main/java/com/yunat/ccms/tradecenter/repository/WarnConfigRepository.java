package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.WarnConfigDomain;

/**
 * 警告配置
 * 
 * @author xin.chen
 */
public interface WarnConfigRepository extends JpaRepository<WarnConfigDomain, Long>,
		CrudRepository<WarnConfigDomain, Long> {

	@Query("from WarnConfigDomain where warn_type = :warn_type and is_switch = 1 and is_open = 1")
	List<WarnConfigDomain> getRefundWarnConfigListAtPresent(@Param("warn_type") String warnType);

	WarnConfigDomain findByDpIdAndWarnType(@Param("dpId") String dpId, @Param("warnType") Integer warnType);

	/**
	 * <pre>
	 * 获取中差评警告的，需要处理的店铺list。
	 * where中，有 is_switch、is_open、warn_start_time、warn_end_time
	 * </pre>
	 * 
	 * @param warnType
	 * @return
	 */
	@Query("from WarnConfigDomain where warn_type = :warn_type and is_switch = 1 and is_open = 1 and warn_start_time < CURRENT_TIME() and warn_end_time > CURRENT_TIME()")
	List<WarnConfigDomain> getNotGoodConfigListAtPresent(@Param("warn_type") String warnType);

}
