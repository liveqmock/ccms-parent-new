package com.yunat.ccms.biz.repository.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.yunat.ccms.biz.domain.Template;

public class TemplateSpecifications {

	public static Specification<Template> templateName(final String templateName) {
		return new Specification<Template>() {

			@Override
			public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("templateName"), templateName);
			}
		};
	}

	public static Specification<Template> filterName(final Long userId, final String filterName) {
		return new Specification<Template>() {

			@Override
			public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("creater").get("id"), userId),
						cb.equal(root.get("templateName"), filterName));
			}
		};
	}

	public static Specification<Template> templateIdIn(final List<Long> idList) {
		return new Specification<Template>() {

			@Override
			public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return root.get("templateId").in(idList);
			}
		};
	}

	public static Specification<Template> allEnabled(final String platCode, final String edition) {
		return new Specification<Template>() {

			@Override
			public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("disabled"), false),
						cb.and(cb.equal(root.get("platCode"), platCode), 
								cb.equal(root.get("edition"), edition)));
			}
		};
	}
}
