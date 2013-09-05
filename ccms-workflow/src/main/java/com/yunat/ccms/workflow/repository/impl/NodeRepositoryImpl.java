package com.yunat.ccms.workflow.repository.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.repository.NodeRepository;
import com.yunat.ccms.workflow.repository.specification.NodeSpecifications;

@Repository
public class NodeRepositoryImpl implements NodeRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcPaginationHelper jdbcPaginationHelper;

	private SimpleJpaRepository<Node, Long> nodeRepository;

	@PostConstruct
	public void init() {
		JpaEntityInformation<Node, Long> nodeEntityInfo = new JpaMetamodelEntityInformation<Node, Long>(
				Node.class, entityManager.getMetamodel());
		nodeRepository = new SimpleJpaRepository<Node, Long>(
				nodeEntityInfo, entityManager);
	}

	@Override
	public Node findById(Long nodeId) {
		return nodeRepository.findOne(nodeId);
	}

	@Override
	public List<Node> findByWorkflowId(Long workflowId) {
		return nodeRepository.findAll(NodeSpecifications.workflowId(workflowId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Node> findNextNodesByIdAndTypeIn(Long nodeId, String[] nodeTypes) {
		StringBuilder qlString = new StringBuilder();
		qlString.append(" from Node where id in (select target from Connect where source = :source) ");
		if (null != nodeTypes) {
			qlString.append(" and type in (:type) ");
		}

		Query query = entityManager.createQuery(qlString.toString(), Node.class);
		query.setParameter("source", nodeId);
		if (null != nodeTypes) {
			query.setParameter("type", nodeTypes);
		}
		return (List<Node>)query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Node> findPreviousNodeByIdAndTypeIn(Long nodeId,
			String[] nodeTypes) {
		StringBuilder qlString = new StringBuilder();
		qlString.append(" from Node where id in (select source from Connect where target = :target) ");
		if (null != nodeTypes) {
			qlString.append(" and type in (:type) ");
		}

		Query query = entityManager.createQuery(qlString.toString(), Node.class);
		query.setParameter("target", nodeId);
		if (null != nodeTypes) {
			query.setParameter("type", nodeTypes);
		}
		return (List<Node>)query.getResultList();
	}

	@Override
	public Page<Map<String, Object>> findAllFitCondition(Pageable pageable) {
		String sqlFetchRows = "select * from twf_node where type = :type";
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("type", "tflowstart");
		return jdbcPaginationHelper.queryForMap(sqlFetchRows, paramMap, pageable);
	}

	@Override
	public Page<Long> findAllNodesByCondition(Pageable pageable) {
		String sqlFetchRows = "select node_id from twf_node where type = :type";
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("type", "tflowstart");
		return jdbcPaginationHelper.queryForList(sqlFetchRows, paramMap, pageable, Long.class);
	}

	@Override
	public Node saveOrUpdate(Node entity) {
		return nodeRepository.saveAndFlush(entity);
	}

	@Override
	public void deleteByList(List<Long> deleted) {
		String sqlDelete = "delete from twf_node where node_id in(:nodeId)";
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("nodeId", deleted);
		jdbcPaginationHelper.update(sqlDelete, paramMap);
	}

}