package com.yunat.ccms.dashboard.repository;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChartRepositoryTest extends  JUnitRepositoryBase{

	@Autowired
	ChartRepository chartRepository;
	
	 @Test
	 public void test() { 
		//执行完成活动
		String campaignStatus = "A5";
		List<Map<String,Object>>  list =  chartRepository.queryChartByCampaignStatus(campaignStatus);
		System.out.println(list);
		
		//新创建活动
		String campaignStatus2 = null;
		List<Map<String,Object>>  list2 =  chartRepository.queryChartByCampaignStatus(campaignStatus2);
		System.out.println(list2);
		
	 } 
	
}
