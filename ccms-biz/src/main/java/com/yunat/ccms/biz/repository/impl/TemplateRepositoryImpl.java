package com.yunat.ccms.biz.repository.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.biz.repository.TemplateRepository;
import com.yunat.ccms.biz.repository.specification.TemplateSpecifications;

@Repository
public class TemplateRepositoryImpl implements TemplateRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private SimpleJpaRepository<Template, Long> templateRepository;
	
	@Override
	public List<Template> findByTemplateName(String templateName) {
		return templateRepository.findAll(TemplateSpecifications.templateName(templateName));
	}

	@Override
	public Page<Template> findByFilterName(User user, String filterName,
			Pageable pageable) {
		return templateRepository.findAll(TemplateSpecifications.filterName(user.getId(), filterName), pageable);
	}

	@Override
	public void save(Template template) {
		templateRepository.save(template);
	}

	@Override
	public void delete(Template template) {
		templateRepository.delete(template);
	}

	@Override
	public void saveAndFlush(Template template) {
		templateRepository.saveAndFlush(template);
	}

	@Override
	public List<Template> findAll() {
		return templateRepository.findAll();
	}

	@Override
	public Template findByTemplateId(Long templateId) {
		return templateRepository.findOne(templateId);
	}

	@Override
	public Page<Template> findAll(Pageable pageable) {
		return templateRepository.findAll(pageable);
	}

	@Override
	public List<Template> findByTemplateIdIn(List<Long> idList) {
		return templateRepository.findAll(TemplateSpecifications.templateIdIn(idList));
	}
	
	@Override
	public void batchDelete(List<Template> templateList) {
		templateRepository.deleteInBatch(templateList);
	}
	
	@Override
	public List<Template> findAllEnable(String platCode, String edition) {
		return templateRepository.findAll(TemplateSpecifications.allEnabled(platCode, edition));
	}
	
	public void setTemplateRepository(
			SimpleJpaRepository<Template, Long> templateRepository) {
		this.templateRepository = templateRepository;
	}
	
	/**
	 * An initialization method which is run after the bean has been
	 * constructed. This ensures that the entity manager is injected before we
	 * try to use it.
	 */
	@PostConstruct
	public void init() {
		JpaEntityInformation<Template, Long> templateEntityInfo = new JpaMetamodelEntityInformation<Template, Long>(
				Template.class, entityManager.getMetamodel());
		templateRepository = new SimpleJpaRepository<Template, Long>(
				templateEntityInfo, entityManager);
	}

}