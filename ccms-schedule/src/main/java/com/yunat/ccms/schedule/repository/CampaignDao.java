package com.yunat.ccms.schedule.repository;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.schedule.support.MybatisBaseDao;

@Component
@Scope("singleton")
public class CampaignDao extends MybatisBaseDao {

	public Campaign get(Long campId) {
		return super.get(campId);
	}

	public String getCampStatusId(Long campId) {
		return super.get(campId);
	}

	public int update(Campaign campaign) {
		return super.update(campaign);
	}

	public List<Campaign> getCampaignsByStatus(String campStatus) {
		return super.list(campStatus);
	}

}
