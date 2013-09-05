package com.yunat.ccms.node.biz.wait;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NodeWaitRepositoryImpl implements NodeWaitRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private SimpleJpaRepository<NodeWait, Long> waitRepository;

	@Override
	public NodeWait findByNodeId(Long nodeId) {
		return waitRepository.findOne(nodeId);
	}

	@PostConstruct
	public void init() {
		JpaEntityInformation<NodeWait, Long> waitEntityInfo = new JpaMetamodelEntityInformation<NodeWait, Long>(
				NodeWait.class, entityManager.getMetamodel());
		waitRepository = new SimpleJpaRepository<NodeWait, Long>(
				waitEntityInfo, entityManager);
	}
}
