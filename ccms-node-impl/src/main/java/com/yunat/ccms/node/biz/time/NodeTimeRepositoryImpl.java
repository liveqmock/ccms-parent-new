package com.yunat.ccms.node.biz.time;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NodeTimeRepositoryImpl implements NodeTimeRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private SimpleJpaRepository<NodeTime, Long> timeRepository;

	@Override
	public NodeTime findByNodeId(Long nodeId) {
		return timeRepository.findOne(nodeId);
	}

	@Override
	public NodeTime findTimeNodeByWorkflowId(Long workflowId) {
		String sqlString = "Select * from twf_node_time where node_id in (" +
				"Select node_id from twf_node where workflow_id = :workflowId and type = :type) ";
		Query query = entityManager.createNativeQuery(sqlString, NodeTime.class);
		query.setParameter("workflowId", workflowId);
		query.setParameter("type", NodeTime.TYPE);
		return (NodeTime)query.getSingleResult();
	}

	@Override
	public void saveOrUpdate(NodeTime newTimeNode) {
		timeRepository.saveAndFlush(newTimeNode);
	}

	@Override
	public void deleteById(Long nodeId) {
		timeRepository.delete(nodeId);
	}

	@PostConstruct
	public void init() {
		JpaEntityInformation<NodeTime, Long> timeEntityInfo = new JpaMetamodelEntityInformation<NodeTime, Long>(
				NodeTime.class, entityManager.getMetamodel());
		timeRepository = new SimpleJpaRepository<NodeTime, Long>(
				timeEntityInfo, entityManager);
	}
}