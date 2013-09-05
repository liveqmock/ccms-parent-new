package com.yunat.ccms.workflow.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yunat.ccms.workflow.domain.Node;

public interface NodeRepository {

	Node findById(Long nodeId);

	List<Node> findByWorkflowId(Long workflowId);

	List<Node> findNextNodesByIdAndTypeIn(Long nodeId, String[] nodeTypes);

	List<Node> findPreviousNodeByIdAndTypeIn(Long nodeId, String[] nodeTypes);

	Page<Map<String, Object>> findAllFitCondition(Pageable pageable);

	Page<Long> findAllNodesByCondition(Pageable pageable);

	Node saveOrUpdate(Node node);

	void deleteByList(List<Long> deleted);

}