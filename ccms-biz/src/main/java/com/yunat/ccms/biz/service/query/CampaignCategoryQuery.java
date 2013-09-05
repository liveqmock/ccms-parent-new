package com.yunat.ccms.biz.service.query;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.yunat.ccms.biz.domain.CampaignCategory;

public interface CampaignCategoryQuery {
	List<CampaignCategory> findAll(Sort sort);
	CampaignCategory findById(Long id);
}
