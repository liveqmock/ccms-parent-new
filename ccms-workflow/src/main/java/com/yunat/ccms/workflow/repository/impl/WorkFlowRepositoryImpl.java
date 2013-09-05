package com.yunat.ccms.workflow.repository.impl;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.workflow.domain.WorkFlow;
import com.yunat.ccms.workflow.repository.WorkFlowRepository;

@Repository
public class WorkFlowRepositoryImpl implements WorkFlowRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private SimpleJpaRepository<WorkFlow, Long> workflowRepository;

	@PostConstruct
	public void init() {
		JpaEntityInformation<WorkFlow, Long> workflowEntityInfo = new JpaMetamodelEntityInformation<WorkFlow, Long>(
				WorkFlow.class, entityManager.getMetamodel());
		workflowRepository = new SimpleJpaRepository<WorkFlow, Long>(
				workflowEntityInfo, entityManager);
	}

	@Override
	public WorkFlow findById(Long workflowId) {
		return workflowRepository.findOne(workflowId);
	}

	@Override
	public WorkFlow findByCampId(Long campId) {
		String sqlString = "Select * from twf_workflow where workflow_id = ( " +
				"Select workflow_id from tb_campaign where camp_id = :campId) ";
		Query query = entityManager.createNativeQuery(sqlString, WorkFlow.class);
		query.setParameter("campId", campId);
		return (WorkFlow)query.getSingleResult();
	}

	@Override
	public void saveOrUpdate(WorkFlow workflow) {
		workflowRepository.saveAndFlush(workflow);
	}

}