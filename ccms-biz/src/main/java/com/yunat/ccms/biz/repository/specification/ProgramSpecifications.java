package com.yunat.ccms.biz.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.yunat.ccms.biz.domain.Program;

public class ProgramSpecifications {

	public static Specification<Program> progName(final String progName) {
		return new Specification<Program>() {

			@Override
			public Predicate toPredicate(Root<Program> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("progName"), progName);
			}
		};
	}
	
	public static Specification<Program> progNameAndNotProgId(final String progName, 
			final Long progId) {
		return new Specification<Program>() {

			@Override
			public Predicate toPredicate(Root<Program> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("progName"), progName), 
						cb.notEqual(root.get("progId"), progId));
			}
		};
	}

	public static Specification<Program> filterName(final Long userId, final String filterName) {
		return new Specification<Program>() {

			@Override
			public Predicate toPredicate(Root<Program> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("creater").get("id"), userId), 
						cb.equal(root.get("progName"), filterName));
			}
		}; 
	}

}
