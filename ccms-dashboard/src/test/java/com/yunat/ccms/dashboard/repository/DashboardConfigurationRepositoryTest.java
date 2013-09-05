package com.yunat.ccms.dashboard.repository;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.dashboard.model.DashboardModule;
import com.yunat.ccms.dashboard.model.DashboardModuleConfig;
import com.yunat.ccms.dashboard.model.DashboardModuleConfigPK;


@Transactional
@TransactionConfiguration(defaultRollback = true)
public class DashboardConfigurationRepositoryTest extends JUnitRepositoryBase{
        
	   @Autowired
	   DashboardConfigurationRepository dashboardConfigurationRepository;
	   

	   
	   @Before
	   public void testReady(){
		   
	   }
	   
	   @Test
	   public void testQueryModuleByUser() { 
		  /* Long userId = 1L;
		   Boolean disable =  false;
		   List<DashboardModule>  listShow =   dashboardConfigurationRepository.queryModuleForUser(userId,disable);
		   Assert.assertFalse(0 == listShow.size());*/
		   
		   
		  /* List<DashboardModule>  listDisable =   dashboardConfigurationRepository.queryModuleForUser(userId,true);
		   Assert.assertFalse(0 == listDisable.size());*/
		}
	   
	   
	   
	   
	   @Test
	   public void  testUpdateModule(){
		   DashboardModuleConfigPK  dashboardModuleConfigPK  = new DashboardModuleConfigPK(1L,109L);
		   
		   DashboardModuleConfig  dashboardModuleConfig=  dashboardConfigurationRepository.findById(dashboardModuleConfigPK);
		   
		   dashboardModuleConfig.setDisabled(true);
		   
		   dashboardConfigurationRepository.save(dashboardModuleConfig);
		   
		   Assert.assertTrue(true == dashboardConfigurationRepository.findById(dashboardModuleConfigPK).getDisabled());
	   }
	   
	   
	   
	   
	   
	   @Test
	   public void testAddRelationConfigByModuleId(){
		   Long moduleId  = 100L;
		   Long defaultModule  = 1L;
		   dashboardConfigurationRepository.addRelationConfigByModuleId(moduleId, defaultModule);
		   Assert.assertNotNull(dashboardConfigurationRepository.find(100L));
		}
	   
}
