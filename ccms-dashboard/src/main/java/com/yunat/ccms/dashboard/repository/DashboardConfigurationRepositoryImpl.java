package com.yunat.ccms.dashboard.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.mapping.Map;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.dashboard.model.DashboardModuleConfig;
import com.yunat.ccms.dashboard.model.DashboardModuleConfigPK;

//http://stackoverflow.com/questions/13700565/jpa-query-getresultlist-use-in-a-generic-way

@Repository
public class DashboardConfigurationRepositoryImpl  implements  DashboardConfigurationRepositoryPlus{


    @PersistenceContext
	private EntityManager entityManager;


    @Override
    public void addRelationConfigByUserId(Long userId) {

		String Temp = " select "
				+ userId
				+ " , dm.dashboardModuleId , case when  dm.dashboardDefaultModule = 1  then 0   else  1  end    "
				+ "  from  DashboardModule  dm   ";

		Query query = entityManager.createQuery(Temp);
        
		List<Object[]> result = query.getResultList();
		for (Object[] object : result) {
			DashboardModuleConfig dashboardModuleConfig = new DashboardModuleConfig();
			dashboardModuleConfig.setId(new DashboardModuleConfigPK(Long
					.valueOf(object[0].toString()), Long.valueOf(object[1]
					.toString())));
			dashboardModuleConfig.setDisabled((Boolean.valueOf(object[2]
					.toString())));
			entityManager.persist(dashboardModuleConfig);
		}
	}

	@Override
	public void addRelationConfigByModuleId(Long moduleId, Long defaultModule) {

		StringBuffer sb = new StringBuffer();

		sb.append("  insert into  dashboard_module_config  ");

		sb.append("  SELECT ts.id as userId ,:moduleId as moduleId ,  ");

		sb.append("  CASE WHEN  :defaultModule = 1   THEN  0   ELSE 1   END   disabled ");

		sb.append("  FROM tb_sysuser  ts    ");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("moduleId", moduleId);
		query.setParameter("defaultModule", defaultModule);
        
		query.executeUpdate();
		
	}

	
}
