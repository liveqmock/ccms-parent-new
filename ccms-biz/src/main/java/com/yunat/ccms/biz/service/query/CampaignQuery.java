package com.yunat.ccms.biz.service.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.biz.support.filter.CampaignFilter;
import com.yunat.ccms.biz.support.filter.CampaignSimpleFilter;
import com.yunat.ccms.biz.vo.CampaignForWeb;

public interface CampaignQuery {
	Page<CampaignForWeb> findByFilter(CampaignFilter campFilter, Pageable pageable);

	Page<CampaignForWeb> findByMySelfFilter(CampaignFilter campFilter, User user, Pageable pageable);

	Page<CampaignForWeb> findByFilter(CampaignSimpleFilter filter, Pageable pageable);

	Page<CampaignForWeb> findByInvestigatorAndCampState(User user, CampaignStatus campStatus, Pageable pageable);

	List<Campaign> findAll(Sort sort);

	List<Campaign> findByCampState(CampaignStatus campStatus, Sort sort);

	Campaign findByCampId(Long campId);

	List<Campaign> findByCampIdIn(List<Long> idList);

	boolean checkUniqueCampaign(String campaignName);

}