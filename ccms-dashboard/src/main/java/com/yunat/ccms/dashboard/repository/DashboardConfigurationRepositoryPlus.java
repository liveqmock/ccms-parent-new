package com.yunat.ccms.dashboard.repository;




public interface DashboardConfigurationRepositoryPlus {
	
	void addRelationConfigByUserId(Long userId);
	
   
	void addRelationConfigByModuleId(Long moduleId, Long defaultModule);
	
}
