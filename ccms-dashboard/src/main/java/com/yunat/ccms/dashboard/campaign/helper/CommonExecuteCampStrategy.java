package com.yunat.ccms.dashboard.campaign.helper;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.dashboard.service.CampaignService;

/**
 * 执行中活动信息
 * 
 * @author yinwei
 * @see DashboardCalculateBase
 * 
 */

@Component
public class CommonExecuteCampStrategy extends DashboardCalculateBase {
	@Autowired
	CampaignService campaignService;

	@Override
	public List<Map<String, Object>> nodeInfoByComposite(String[] campStatusArray, String[] subjobStatusArray) {
		return campaignService.nodeInfoByComposite(campStatusArray, subjobStatusArray);
	}
}
