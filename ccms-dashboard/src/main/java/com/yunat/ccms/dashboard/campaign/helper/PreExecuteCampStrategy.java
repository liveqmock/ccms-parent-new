package com.yunat.ccms.dashboard.campaign.helper;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.dashboard.service.CampaignService;

/**
 * 预执行活动信息
 * 
 * @author yinwei
 * @see DashboardCampInfo
 * 
 */
@Component
public class PreExecuteCampStrategy extends DashboardCalculateBase {
	@Autowired
	CampaignService campaignService;

	@Override
	public List<Map<String, Object>> nodeInfoByComposite(String[] campStatusArray, String[] subjobStatusArray) {
		return campaignService.nodeInfoByComposite(campStatusArray, subjobStatusArray);
	}
}
