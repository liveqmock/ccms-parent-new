package com.yunat.ccms.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.dashboard.model.DashboardModuleConfig;
import com.yunat.ccms.dashboard.model.DashboardModuleConfigPK;
import com.yunat.ccms.dashboard.repository.DashboardConfigurationRepository;

@Service
public class DashboardConfigurationService {
	
	@Autowired
	DashboardConfigurationRepository   dashboardConfigurationRepository;
	
	
	/*private ModuleService moduleService = new ModuleService();
	
	@Transactional(readOnly = true)
	public  List<Module> allModule(Long userId) throws Exception{
		return moduleService.getModules("SY01", "L3");
		//return  dashboardConfigurationRepository.queryModuleForUser(userId);
	}*/
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void switchModule(Long userId,Long moduleId,Boolean switchSign){
		DashboardModuleConfigPK  dashboardModuleConfigPK  = new DashboardModuleConfigPK(userId,moduleId);
		DashboardModuleConfig  dashboardModuleConfig = dashboardConfigurationRepository.findById(dashboardModuleConfigPK);
		dashboardModuleConfig.setDisabled(switchSign);
		dashboardConfigurationRepository.save(dashboardModuleConfig);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void maintainConfigOfUserLaunch(Long userId){
		dashboardConfigurationRepository.addRelationConfigByUserId(userId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void maintainConfigOfDashboardModuleLaunch(Long moduleId,Long  defaultModuleFlag){
		dashboardConfigurationRepository.addRelationConfigByModuleId(moduleId,defaultModuleFlag);
	}
	

}
