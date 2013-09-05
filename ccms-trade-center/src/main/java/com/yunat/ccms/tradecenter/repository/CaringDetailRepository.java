package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.tradecenter.domain.CaringDetailDomain;

public interface CaringDetailRepository  extends JpaRepository<CaringDetailDomain, Long> {

	CaringDetailDomain getByTidAndOidAndCaringperson(String tid, String oid, String caringperson);

	@Override
	CaringDetailDomain saveAndFlush(CaringDetailDomain caringDetailDomain);


}
