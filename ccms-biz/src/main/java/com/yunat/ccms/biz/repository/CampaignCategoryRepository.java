package com.yunat.ccms.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.biz.domain.CampaignCategory;

public interface CampaignCategoryRepository extends
		JpaRepository<CampaignCategory, Long> {

}
