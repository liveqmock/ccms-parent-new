package com.yunat.ccms.dashboard.repository;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.dashboard.model.DashboardModule;
import com.yunat.ccms.dashboard.model.DashboardModuleConfig;
import com.yunat.ccms.dashboard.model.DashboardModuleConfigPK;

/**
 * 
 * DashboardConfigurationRepositoryPlus:需要自己扩展的接口  
 * @author yinwei
 *
 */
public interface DashboardConfigurationRepository extends
		Repository<DashboardModuleConfig, Long> , CrudRepository<DashboardModuleConfig, Long> , DashboardConfigurationRepositoryPlus {
	
	List<DashboardModuleConfig> findByDisabled(Boolean disabled);
	
	/**
	 * 根据联合主键去获取配置信息
	 * @param dashboardModuleConfigPK
	 * @return
	 */
	DashboardModuleConfig findById(DashboardModuleConfigPK  dashboardModuleConfigPK);
	
	
	/**
	 * 根据模块ID获取配置信息
	 * @param moduleId
	 * @return
	 */
	@Query("SELECT p FROM DashboardModuleConfig p WHERE p.id.moduleId = :moduleId ")
	List<DashboardModuleConfig> find(@Param("moduleId") Long moduleId);
	
	
	
	/**
	 * 根据用户获取模块信息
	 * @param userId
	 * @return
	 */
	@Query( " select m.dashboardModuleName ,  m.dashboardModuleUrl , c.disabled   " +
			" from DashboardModuleConfig c , DashboardModule  m   " +
			" where c.id.moduleId = m.dashboardModuleId  and  c.id.userId =  :userId ")
	List<Map<String,Object>>  queryModuleForUser(@Param("userId") Long userId );
	
}
