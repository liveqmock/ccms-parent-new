package com.yunat.ccms.biz.service.query.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.biz.repository.TemplateRepository;
import com.yunat.ccms.biz.service.query.TemplateQuery;

@Service
public class TemplateQueryImpl implements TemplateQuery {

	@Autowired
	private TemplateRepository templateRepository;

	@Override
	public List<Template> findAll() {
		return templateRepository.findAll();
	}

	@Override
	public List<Template> findByTemplateName(String templateName) {
		return templateRepository.findByTemplateName(templateName);
	}

	@Override
	public Template findByTemplateId(Long templateId) {
		return templateRepository.findByTemplateId(templateId);
	}

	@Override
	public Page<Template> findByFilterName(User user, String filterName,
			Pageable pageable) {
		return templateRepository.findByFilterName(user, filterName, pageable);
	}

	@Override
	public Page<Template> findAll(Pageable pageable) {
		return templateRepository.findAll(pageable);
	}

	@Override
	public List<Template> findByTemplateIdIn(List<Long> idList) {
		return templateRepository.findByTemplateIdIn(idList);
	}

	@Override
	public List<Map<String, Object>> findAllEnable(String platCode, String edition) {
		List<Template> templates = templateRepository.findAllEnable(platCode, edition);
		List<Map<String, Object>> lists = Lists.newArrayList();
		for (Template template : templates) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("templateId", template.getTemplateId());
			map.put("templateName", template.getTemplateName());
			map.put("picUrl", template.getPicUrl());
			
			lists.add(map);
		}
		return lists;
	}
}