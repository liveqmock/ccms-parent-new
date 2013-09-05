package com.yunat.ccms.dashboard.campaign.helper;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.dashboard.service.CampaignService;

/**
 * ©Copyright：yunat Project：CCMS Module ID： Comments： 首页获取活动信息和节点信息的抽象类 JDK
 * version used：<JDK1.6> Author：yinwei Create Date： 2012-12-21 Version：1.0
 * Modified By： Modified Date： Why & What is modified： Version：
 */
public abstract class DashboardCalculateBase implements DashboardCalculate {
	@Autowired
	protected CampaignService campaignService;

	@Override
	public abstract List<Map<String, Object>> nodeInfoByComposite(String[] campStatusArray, String[] subjobStatusArray);

	@Override
	public List<Map<String, Object>> campInfoByStatus(String type) {
		return campaignService.campInfoByStatus(type);
	}

}
