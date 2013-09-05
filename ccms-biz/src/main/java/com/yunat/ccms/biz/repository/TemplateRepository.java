package com.yunat.ccms.biz.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Template;

public interface TemplateRepository {

	List<Template> findByTemplateName(String templateName);

	Page<Template> findByFilterName(User user, String filterName,
			Pageable pageable);

	void save(Template template);

	void delete(Template template);

	void saveAndFlush(Template template);

	List<Template> findAll();

	Template findByTemplateId(Long templateId);

	Page<Template> findAll(Pageable pageable);

	List<Template> findByTemplateIdIn(List<Long> idList);

	void batchDelete(List<Template> templateList);

	List<Template> findAllEnable(String platCode, String version);

}
