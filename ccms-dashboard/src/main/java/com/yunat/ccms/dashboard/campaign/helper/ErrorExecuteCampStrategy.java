package com.yunat.ccms.dashboard.campaign.helper;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.dashboard.service.CampaignService;

/**
 * 执行出错活动信息
 * @author yinwei
 * @see DashboardCalculateBase
 *
 */
@Component
public class ErrorExecuteCampStrategy extends DashboardCalculateBase {

	@Autowired
    CampaignService campaignService;
	
    @Override
	public List<Map<String, Object>> nodeInfoByComposite(
			String[] campStatusArray, String[] subjobStatusArray) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
    public List<Map<String,Object>> campInfoByStatus(String type) {
		return campaignService.campInfoByErrorStatus(); 
	}

}
