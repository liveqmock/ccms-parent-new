package com.yunat.ccms.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.biz.domain.CampaignStatus;

public interface CampaignStatusRepository extends JpaRepository<CampaignStatus, String> {

}
