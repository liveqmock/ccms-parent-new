package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.LoginSubuserRelaDomain;

public interface LoginSubuserRelaRepository  extends JpaRepository<LoginSubuserRelaDomain, Long>{

	@Query("select l from LoginSubuserRelaDomain l where l.loginName = :loginName and dpId = :dpId")
	LoginSubuserRelaDomain findOneByloginName(@Param("loginName") String loginName, @Param("dpId") String dpId);

}

