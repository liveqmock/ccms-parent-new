package com.yunat.ccms.biz.service.query.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.biz.repository.CampaignStatusRepository;
import com.yunat.ccms.biz.service.query.CampaignStatusQuery;

@Service
public class CampaignStatusQueryImpl implements CampaignStatusQuery {

	@Autowired
	private CampaignStatusRepository campStateRepository;

	@Override
	public List<CampaignStatus> findAll(Sort sort) {
		return campStateRepository.findAll(sort);
	}

	@Override
	public CampaignStatus findByStatusId(String statusId) {
		return campStateRepository.findOne(statusId);
	}

}
