package com.yunat.ccms.node.support.repository;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.yunat.ccms.node.spi.NodeEntity;
import com.yunat.ccms.node.spi.repository.NodeEntityRepository;
import com.yunat.ccms.workflow.service.WorkflowConstraintService;
import com.yunat.ccms.workflow.validation.NodeConstraint;

@Repository
public class DefaultNodeEntityRepository implements NodeEntityRepository, InitializingBean {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private WorkflowConstraintService workflowConstraintService;

	private Map<String, NodeConstraint> nodeConstraitMap;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public NodeEntity getNodeEntity(String nodeType, Long nodeId) {
		NodeConstraint nodeConstraint = nodeConstraitMap.get(nodeType);
		if (nodeConstraint == null) {
			return null;
		}
		try {
			Class clazz = nodeConstraint.getEntityClass();
			if (!clazz.isAnnotationPresent(Entity.class)) {
				return (NodeEntity) clazz.newInstance();
			}
			return (NodeEntity) entityManager.find(clazz, nodeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		nodeConstraitMap = workflowConstraintService.getNodeConstraintMap();
		Assert.notNull(nodeConstraitMap);
		Assert.notEmpty(nodeConstraitMap);
	}
}
