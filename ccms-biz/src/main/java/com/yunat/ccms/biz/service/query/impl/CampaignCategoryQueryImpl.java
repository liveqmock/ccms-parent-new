package com.yunat.ccms.biz.service.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yunat.ccms.biz.domain.CampaignCategory;
import com.yunat.ccms.biz.repository.CampaignCategoryRepository;
import com.yunat.ccms.biz.service.query.CampaignCategoryQuery;

@Service
public class CampaignCategoryQueryImpl implements CampaignCategoryQuery {

	@Autowired
	private CampaignCategoryRepository campCategoryRepository;

	@Override
	public List<CampaignCategory> findAll(Sort sort) {
		return campCategoryRepository.findAll(sort);
	}

	@Override
	public CampaignCategory findById(Long id) {
		return campCategoryRepository.findOne(id);
	}

}
