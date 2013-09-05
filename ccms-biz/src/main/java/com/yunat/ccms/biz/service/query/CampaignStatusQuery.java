package com.yunat.ccms.biz.service.query;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.yunat.ccms.biz.domain.CampaignStatus;

public interface CampaignStatusQuery {
	List<CampaignStatus> findAll(Sort sort);
	CampaignStatus findByStatusId(String statusId);
}
