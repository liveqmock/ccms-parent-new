package com.yunat.ccms.dashboard.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.dashboard.repository.ChartRepository;



@Service
public class ChartService {
   @Autowired
   private ChartRepository   chartRepository;
	
    @Transactional(readOnly = true)
    public List<Map<String, Object>> chartDataByCampaignStatus(String campaignStatus){
		return   chartRepository.queryChartByCampaignStatus(campaignStatus);
	}
}
