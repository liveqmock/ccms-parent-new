package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.WarnStatusDomain;

public interface WarnStatusRepository extends JpaRepository<WarnStatusDomain, String>{

	/**
     * 获取最后处理的中差评告警的记录
	 * @param pageRequest
     * @param dpId
     * @param lastDealTime 上次查询（处理）的时间
     * @return
     */
	@Query(" from WarnStatusDomain where dp_id = :dpId order by not_good_warn_time desc")
    List<WarnStatusDomain> getLastNotGoodWarn(@Param("dpId")String dpId, Pageable p);

	WarnStatusDomain getByOid(String oid);

}
