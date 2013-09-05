package com.yunat.ccms.workflow.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.yunat.ccms.workflow.domain.Connect;

public class ConnectSpecifications {
	public static Specification<Connect> workflowId(final Long workflowId) {
		return new Specification<Connect>() {

			@Override
			public Predicate toPredicate(Root<Connect> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				return cb.equal(root.get("workflowId"), workflowId);
			}
		};
	}
	
	public static Specification<Connect> targetNodeId(final Long targetNodeId) {
		return new Specification<Connect>() {

			@Override
			public Predicate toPredicate(Root<Connect> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("target"), targetNodeId);
			}
		};
	}
}
