package com.yunat.ccms.configuration.repository.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.yunat.ccms.configuration.domain.AppProperties;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;

@Repository
public class AppPropertiesRepositoryImpl implements AppPropertiesRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private SimpleJpaRepository<AppProperties, Long> repository;

	/**
	 * An initialization method which is run after the bean has been
	 * constructed. This ensures that the entity manager is injected before we
	 * try to use it.
	 */
	@PostConstruct
	public void init() {
		JpaEntityInformation<AppProperties, Long> entityInfo = new JpaMetamodelEntityInformation<AppProperties, Long>(
				AppProperties.class, entityManager.getMetamodel());
		repository = new SimpleJpaRepository<AppProperties, Long>(entityInfo, entityManager);
	}

	@Override
	public String retrieveConfiguration(String propertyGroup, String propertyName) {
		List<AppProperties> appPropertiesList = repository.findAll(propGroupAndPropName(propertyGroup, propertyName));
		if (CollectionUtils.isNotEmpty(appPropertiesList)) {
			return StringUtils.trimToNull(appPropertiesList.get(0).getPropValue());
		}
		return null;
	}

	@Override
	public Map<String, String> retrieveAllConfiguration() {
		Map<String, String> result = Maps.newHashMap();
		List<AppProperties> appPropertiesList = repository.findAll();
		for (AppProperties appProperties : appPropertiesList) {
			String prop_group = appProperties.getPropGroup();
			String prop_name = appProperties.getPropName();
			String prop_value = appProperties.getPropValue();
			prop_value = StringUtils.trimToNull(prop_value);
			result.put(prop_group + "." + prop_name, prop_value);
		}

		return result;
	}

	private static Specification<AppProperties> propGroupAndPropName(final String propGroup, final String propName) {
		return new Specification<AppProperties>() {

			@Override
			public Predicate toPredicate(Root<AppProperties> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("propGroup"), propGroup), cb.equal(root.get("propName"), propName));
			}
		};
	}
}