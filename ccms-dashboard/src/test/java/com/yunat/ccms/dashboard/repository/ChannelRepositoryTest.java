package com.yunat.ccms.dashboard.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChannelRepositoryTest extends JUnitRepositoryBase {
   @Autowired
   DashboardChannelRepository channelRepository;
   
   @Test  
   public void test() {  
	   String channelType = "tcommunicateSMS";
	   String  nowDate=new SimpleDateFormat( "yyyy-MM-dd").format(new Date());
	   List<Map<String,Object>>  testList =  channelRepository.queryChannelSendInfo(channelType, nowDate);
	   System.out.println(testList.size());
	}  
	
}
