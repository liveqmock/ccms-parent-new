package com.yunat.ccms.workflow.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.yunat.ccms.workflow.domain.Node;

public class NodeSpecifications {
	public static Specification<Node> workflowId(final Long workflowId) {
		return new Specification<Node>() {

			@Override
			public Predicate toPredicate(Root<Node> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				return cb.equal(root.get("workflowId"), workflowId);
			}
		};
	}
}
