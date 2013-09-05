package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.tradecenter.domain.RefundCareDomain;

public interface RefundCareRepository  extends JpaRepository<RefundCareDomain, Long>{

	List<RefundCareDomain> findByOidIn(List<String> oids);

}
