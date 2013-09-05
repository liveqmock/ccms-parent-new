package com.yunat.ccms.biz.service.query;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Template;

public interface TemplateQuery {
	List<Template> findAll();

	List<Template> findByTemplateName(String templateName);

	Template findByTemplateId(Long templateId);

	Page<Template> findByFilterName(User user, String filterName,
			Pageable pageable);

	Page<Template> findAll(Pageable pageable);

	List<Template> findByTemplateIdIn(List<Long> idList);

	List<Map<String, Object>> findAllEnable(String platCode, String version);

}