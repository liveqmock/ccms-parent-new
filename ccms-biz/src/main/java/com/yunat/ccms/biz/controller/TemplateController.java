package com.yunat.ccms.biz.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.biz.controller.vo.ErrorResult;
import com.yunat.ccms.biz.controller.vo.Result;
import com.yunat.ccms.biz.controller.vo.SuccessResult;
import com.yunat.ccms.biz.controller.vo.TemplateListRequest;
import com.yunat.ccms.biz.controller.vo.TemplateRequest;
import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.biz.service.command.TemplateCommand;
import com.yunat.ccms.biz.service.query.TemplateQuery;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.workflow.domain.WorkFlow;
import com.yunat.ccms.workflow.service.command.WorkFlowCommand;
import com.yunat.ccms.workflow.service.query.WorkFlowQuery;
import com.yunat.ccms.workflow.utils.MxGraphJsonUtils;
import com.yunat.ccms.workflow.vo.MxGraph;

@Controller
public class TemplateController {
	private static Logger logger = LoggerFactory.getLogger(TemplateController.class);

	@Autowired
	private TemplateQuery templateQuery;

	@Autowired
	private TemplateCommand templateCommand;

	@Autowired
	private WorkFlowCommand workflowCommand;

	@Autowired
	private WorkFlowQuery workflowQuery;

	@ResponseBody
	@RequestMapping(value = "/template", method = RequestMethod.POST)
	public Result addTemplate(@RequestBody TemplateRequest templateRequest) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Template template = new Template();
			template.setTemplateName(templateRequest.getTemplateName());
			template.setTemplateDesc(templateRequest.getTemplateDesc());
			template.setCreatedTime(new Date());
			template.setPlatCode(templateRequest.getPlatCode());
			template.setEdition(LoginInfoHolder.getProductEdition().name());
			template.setCreater(LoginInfoHolder.getCurrentUser());
			WorkFlow workflow = workflowCommand.createWorkflow(MxGraphJsonUtils.standardConvert());
			template.setWorkflow(workflow);
			templateCommand.saveTemplate(template);
			result.put("templateId", template.getTemplateId());
			return new SuccessResult(result);
		} catch (Exception e) {
			logger.info("新建模板异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "新建模板失败!");
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template/option", method = RequestMethod.GET)
	public Result findTemplate(@RequestParam String platCode) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			ProductEdition productionEdition = LoginInfoHolder.getProductEdition();
			List<Map<String, Object>> templates = templateQuery.findAllEnable(platCode, productionEdition.name());
			return new SuccessResult(templates.toArray());
		} catch (Exception e) {
			logger.info("查找可用于创建活动的模板异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "查找可于创建活动的模板失败!");
		return new ErrorResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template/{id}", method = RequestMethod.PUT)
	public Result editTemplate(@PathVariable Long id, @RequestBody TemplateRequest templateRequest) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Template template = templateQuery.findByTemplateId(id);
			template.setTemplateName(templateRequest.getTemplateName());
			template.setTemplateDesc(templateRequest.getTemplateDesc());
			templateCommand.saveTemplate(template);
			result.put("templateId", template.getTemplateId());
			return new SuccessResult(result);
		} catch (Exception e) {
			logger.info("修改更新模板异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "修改模板失败!");
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template/{id}", method = RequestMethod.DELETE)
	public Result deleteTemplate(@PathVariable Long id) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Template template = templateQuery.findByTemplateId(id);
			templateCommand.deleteTemplate(template);
			result.put("templateId", template.getTemplateId());
			return new SuccessResult(result);
		} catch (Exception e) {
			logger.info("删除模板异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "删除模板失败!");
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template", method = RequestMethod.DELETE)
	public Result batchDeleteTemplate(@RequestParam String ids) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			String[] idArr = ids.split(",");
			List<Long> idList = Lists.newArrayList();
			for (String id : idArr) {
				idList.add(Long.parseLong(id.trim()));
			}
			List<Template> templateList = templateQuery.findByTemplateIdIn(idList);
			templateCommand.batchDelete(templateList);
			return new SuccessResult();
		} catch (Exception e) {
			logger.info("批量删除模板异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "批量删除模板失败!");
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template/{id}", method = RequestMethod.GET)
	public Result showTemplate(@PathVariable Long id) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Template template = templateQuery.findByTemplateId(id);
			return new SuccessResult(template);
		} catch (Exception e) {
			logger.info("获取模板信息异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "获取模板信息失败!");
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public Result templateList(@RequestBody TemplateListRequest listRequest) {
		Map<String, Object> result = Maps.newHashMap();
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template/workflow/snapshot", method = RequestMethod.GET)
	public Result openWorkflow(@RequestParam Long templateId) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Template template = templateQuery.findByTemplateId(templateId);
			MxGraph graph = workflowQuery.findMxGraphById(template.getWorkflow().getWorkflowId());
			return new SuccessResult(graph);
		} catch (Exception e) {
			logger.info("打开流程模板异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "打开模板流程失败!");
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/template/workflow/snapshot/{id}", method = RequestMethod.POST)
	public Result saveWorkflow(@PathVariable Long id, @RequestBody MxGraph graph) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Template template = templateQuery.findByTemplateId(id);
			workflowCommand.updateWorkflow(graph, template.getWorkflow().getWorkflowId());
			result.put("templateId", id);
			return new SuccessResult(result);
		} catch (Exception e) {
			logger.info("保存模板流程异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "保存模板流程失败!");
		return new SuccessResult(result);
	}
}
