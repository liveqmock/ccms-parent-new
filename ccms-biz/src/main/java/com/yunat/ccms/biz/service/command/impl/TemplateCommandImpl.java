package com.yunat.ccms.biz.service.command.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.biz.repository.TemplateRepository;
import com.yunat.ccms.biz.service.command.TemplateCommand;

@Service
public class TemplateCommandImpl implements TemplateCommand {

	@Autowired
	private TemplateRepository templateRepository;

	@Override
	public void saveTemplate(Template template) {
		templateRepository.save(template);
	}

	@Override
	public void deleteTemplate(Template template) {
		templateRepository.delete(template);
	}

	@Override
	public void updateTemplate(Template template) {
		templateRepository.saveAndFlush(template);
	}

	@Override
	public void batchDelete(List<Template> templateList) {
		templateRepository.batchDelete(templateList);
	}
}