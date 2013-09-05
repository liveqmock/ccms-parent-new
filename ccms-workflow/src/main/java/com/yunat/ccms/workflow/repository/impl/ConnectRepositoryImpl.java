package com.yunat.ccms.workflow.repository.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.repository.ConnectRepository;
import com.yunat.ccms.workflow.repository.specification.ConnectSpecifications;

@Repository
public class ConnectRepositoryImpl implements ConnectRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcPaginationHelper jdbcPaginationHelper;

	private SimpleJpaRepository<Connect, Long> connectRepository;

	@PostConstruct
	public void init() {
		JpaEntityInformation<Connect, Long> connectEntityInfo = new JpaMetamodelEntityInformation<Connect, Long>(
				Connect.class, entityManager.getMetamodel());
		connectRepository = new SimpleJpaRepository<Connect, Long>(
				connectEntityInfo, entityManager);
	}

	@Override
	public Connect findById(Long connectId) {
		return connectRepository.findOne(connectId);
	}

	@Override
	public List<Connect> findByWorkflowId(Long workflowId) {
		return connectRepository.findAll(ConnectSpecifications.workflowId(workflowId));
	}

	@Override
	public List<Connect> findByTargetNodeId(Long targetNodeId) {
		return connectRepository.findAll(ConnectSpecifications.targetNodeId(targetNodeId));
	}


	@Override
	public void saveOrUpdate(Connect newConnect) {
		connectRepository.saveAndFlush(newConnect);
	}

	@Override
	public void deleteByList(List<Long> deletedConnects) {
		String deleteSql = "delete from twf_connect where connect_Id in (:connectId)";
		Map<String, Object> paramMap = Maps.newHashMap();
		jdbcPaginationHelper.update(deleteSql, paramMap);
	}

}