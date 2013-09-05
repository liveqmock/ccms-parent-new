package com.yunat.ccms.biz.service.query.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.biz.repository.CampaignRepository;
import com.yunat.ccms.biz.service.query.CampaignQuery;
import com.yunat.ccms.biz.support.filter.CampaignFilter;
import com.yunat.ccms.biz.support.filter.CampaignSimpleFilter;
import com.yunat.ccms.biz.vo.CampaignForWeb;

@SuppressWarnings("unchecked")
@Service
public class CampaignQueryImpl implements CampaignQuery {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CampaignRepository campaignRepository;

	@Override
	public Page<CampaignForWeb> findByFilter(final CampaignFilter campFilter, final Pageable pageable) {
		try {
			return campaignRepository.findByFilter(campFilter, pageable);
		} catch (final Exception e) {
			logger.info("find by Filter throws Exception, Exception : {}", e.getMessage());
		}
		return null;
	}

	@Override
	public Page<CampaignForWeb> findByMySelfFilter(final CampaignFilter campFilter, final User user,
			final Pageable pageable) {
		try {
			return campaignRepository.findByMySelfFilter(campFilter, user, pageable);
		} catch (final Exception e) {
			logger.info("find by Filter throws Exception, Exception : {}", e.getMessage());
		}
		return null;

	}

	@Override
	public Page<CampaignForWeb> findByFilter(final CampaignSimpleFilter filter, final Pageable pageable) {
		try {
			final Page<CampaignForWeb> campPage = campaignRepository.findByFilter(filter, pageable);
			return campPage;
		} catch (final Exception e) {
			logger.info("find by Filter throws Exception, Exception : {}", e.getMessage());
		}
		return null;
	}

	@Override
	public Page<CampaignForWeb> findByInvestigatorAndCampState(final User user, final CampaignStatus campStatus,
			final Pageable pageable) {
		try {
			final Collection<CampaignStatus> coll = new ArrayList<CampaignStatus>();
			coll.add(campStatus);
			final Page<CampaignForWeb> campPage = campaignRepository.findByInvestigatorAndCampStateIn(user, coll,
					pageable);
			return campPage;
		} catch (final Exception e) {
			logger.info("find by investigator throws Exception, Exception : {}", e.getMessage());
		}
		return null;
	}

	@Override
	public List<Campaign> findAll(final Sort sort) {
		try {
			final List<Campaign> campList = campaignRepository.findAll(sort);
			return campList;
		} catch (final Exception e) {
			logger.info("find all throws Exception, Exception : {}", e.getMessage());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Campaign> findByCampState(final CampaignStatus campStatus, final Sort sort) {
		try {
			final List<Campaign> campList = campaignRepository.findByCampState(campStatus, sort);
			return campList;
		} catch (final Exception e) {
			logger.info("find by campaign state throws Exception, Exception : {}", e.getMessage());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public Campaign findByCampId(final Long campId) {
		try {
			final Campaign campaign = campaignRepository.findByCampId(campId);
			return campaign;
		} catch (final Exception e) {
			logger.info("find by campaignId throws Exception, Exception : {}", e.getMessage());
		}
		return null;
	}

	@Override
	public List<Campaign> findByCampIdIn(final List<Long> idList) {
		try {
			final List<Campaign> campList = campaignRepository.findByCampIdIn(idList);
			return campList;
		} catch (final Exception e) {
			logger.info("find by campaignId in throws Exception, Exception : {}", e.getMessage());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean checkUniqueCampaign(final String campaignName) {
		final List<Campaign> campaignList = campaignRepository.findByCampaignName(campaignName);
		if (CollectionUtils.isEmpty(campaignList)) {
			return true;
		}
		return false;
	}

}