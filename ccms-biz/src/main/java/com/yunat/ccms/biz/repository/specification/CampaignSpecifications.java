package com.yunat.ccms.biz.repository.specification;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.biz.domain.CampaignStatus_;
import com.yunat.ccms.biz.domain.Campaign_;

public class CampaignSpecifications {
	public static Specification<Campaign> campStateIn(final Collection<String> campStates) {
		return new Specification<Campaign>() {

			@Override
			public Predicate toPredicate(Root<Campaign> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("disabled"), false), root.<CampaignStatus> get(Campaign_.campState)
						.<String> get(CampaignStatus_.statusId).in(campStates));
			}

		};
	}

	public static Specification<Campaign> campState(final String statusId) {
		return new Specification<Campaign>() {

			@Override
			public Predicate toPredicate(Root<Campaign> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("disabled"), false),
						cb.equal(root.get("campState").get("statusId"), statusId));
			}

		};
	}

	public static Specification<Campaign> creatorAndWorkflowTypeIn(final Long userId,
			final Collection<String> workflowTypes) {
		return new Specification<Campaign>() {

			@Override
			public Predicate toPredicate(Root<Campaign> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("disabled"), false), cb.equal(root.get("creater").get("id"), userId),
						root.get("workflowType").in(workflowTypes));
			}
		};
	}

	public static Specification<Campaign> checkerInvestigatorAndCampStateIn(final Long userId,
			final Collection<String> campState) {
		return new Specification<Campaign>() {

			@Override
			public Predicate toPredicate(Root<Campaign> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("disabled"), false),
						cb.equal(root.get("investigator").get("id"), userId),
						root.get("campState").get("statusId").in(campState));
			}
		};
	}

	public static Specification<Campaign> campIdIn(final List<Long> idList) {
		return new Specification<Campaign>() {

			@Override
			public Predicate toPredicate(Root<Campaign> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return root.get("campId").in(idList);
			}
		};
	}

	public static Specification<Campaign> hasCampaignName(final String campaignName) {
		return new Specification<Campaign>() {

			@Override
			public Predicate toPredicate(Root<Campaign> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("campName"), campaignName);
			}
		};
	}
}