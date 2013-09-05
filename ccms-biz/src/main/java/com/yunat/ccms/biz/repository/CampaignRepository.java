package com.yunat.ccms.biz.repository;

import java.util.Collection;
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

public interface CampaignRepository {

	// need permission checker
	Page<CampaignForWeb> findByFilter(CampaignFilter campFilter, Pageable pageable);

	// need permission checker
	Page<CampaignForWeb> findByMySelfFilter(CampaignFilter campFilter, User user, Pageable pageable);

	// need permission checker
	Page<CampaignForWeb> findByFilter(CampaignSimpleFilter filter, Pageable pageable);

	// need permission checker
	Page<CampaignForWeb> findByInvestigatorAndCampStateIn(User user, Collection<CampaignStatus> campStatus,
			Pageable pageable);

	List<Campaign> findByCampState(CampaignStatus campStatus, Sort sort);

	List<Campaign> findByCreatorAndWorkflowTypeIn(User userId, Collection<String> workflowTypes);

	List<Campaign> findAll(Sort sort);

	Campaign findByCampId(Long campId);

	void saveOrUpdate(Campaign campaign);

	void delete(Campaign campaign);

	List<Campaign> findByCampStateIn(Collection<CampaignStatus> campStatus);

	List<Campaign> findByCampIdIn(List<Long> idList);

	List<Campaign> findByCampaignName(String campaignName);

}
