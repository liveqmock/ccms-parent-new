package com.yunat.ccms.biz.service.command;

import java.util.List;

import com.yunat.ccms.biz.domain.Template;

public interface TemplateCommand {
	void saveTemplate(Template template);

	void deleteTemplate(Template template);

	void updateTemplate(Template template);

	void batchDelete(List<Template> templateList);
}