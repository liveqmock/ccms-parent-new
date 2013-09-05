package com.yunat.ccms.biz.controller;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;
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
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.auth.user.UserQuery;
import com.yunat.ccms.biz.controller.vo.CampaignListRequest;
import com.yunat.ccms.biz.controller.vo.CampaignRequest;
import com.yunat.ccms.biz.controller.vo.ErrorResult;
import com.yunat.ccms.biz.controller.vo.Result;
import com.yunat.ccms.biz.controller.vo.SuccessResult;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignCategory;
import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.biz.service.command.CampaignCommand;
import com.yunat.ccms.biz.service.query.CampaignCategoryQuery;
import com.yunat.ccms.biz.service.query.CampaignQuery;
import com.yunat.ccms.biz.service.query.CampaignStatusQuery;
import com.yunat.ccms.biz.service.query.ProgramQuery;
import com.yunat.ccms.biz.service.query.TemplateQuery;
import com.yunat.ccms.biz.support.cons.WorkflowEnum;
import com.yunat.ccms.biz.support.filter.CampaignFilter;
import com.yunat.ccms.biz.support.filter.NormalCampaignFilter;
import com.yunat.ccms.biz.vo.CampaignForWeb;
import com.yunat.ccms.core.support.auth.AclManaged;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.PagedResultVo;
import com.yunat.ccms.workflow.service.command.WorkFlowCommand;
import com.yunat.ccms.workflow.service.query.WorkFlowQuery;
import com.yunat.ccms.workflow.vo.MxGraph;
import com.yunat.ccms.workflow.vo.MxNode;

@Controller
public class CampaignController {
	private static Logger logger = LoggerFactory.getLogger(CampaignController.class);

	@Autowired
	private CampaignQuery campaignQuery;

	@Autowired
	private CampaignCommand campaignCommand;

	@Autowired
	private ProgramQuery programQuery;

	@Autowired
	private TemplateQuery templateQuery;

	@Autowired
	private CampaignCategoryQuery campCategoryQuery;

	@Autowired
	private CampaignStatusQuery campStatusQuery;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private WorkFlowCommand workflowCommand;

	@Autowired
	private WorkFlowQuery workflowQuery;

	@ResponseBody
	@RequestMapping(value = "/campaign", method = RequestMethod.POST)
	public Result addCampaign(@RequestBody final CampaignRequest campaignRequest) throws Exception {
		final Map<String, Object> result = Maps.newHashMap();
		final boolean valid = campaignQuery.checkUniqueCampaign(campaignRequest.getCampName().trim());
		if (!valid) {
			result.put("errordesc", "活动名称已存在!");
			return new ErrorResult(result);
		}

		final Campaign campaign = new Campaign();
		campaign.setCampName(campaignRequest.getCampName());
		if (null != campaignRequest.getProgId()) {
			campaign.setProg(programQuery.findById(campaignRequest.getProgId()));
		}
		if (null != campaignRequest.getInverstigatorId()) {
			final User investigator = userQuery.findById(campaignRequest.getInverstigatorId());
			campaign.setInvestigator(investigator);
		}
		if (null != campaignRequest.getCampTypeId()) {
			final CampaignCategory campCategory = campCategoryQuery.findById(campaignRequest.getCampTypeId());
			campaign.setCampCategory(campCategory);
		}
		campaign.setStartTime(campaignRequest.getStartTime());
		campaign.setEndTime(campaignRequest.getEndTime());
		campaign.setCampDesc(campaignRequest.getCampDesc());
		campaign.setPlatCode(campaignRequest.getPlatCode());
		campaign.setEdition(LoginInfoHolder.getProductEdition().name());
		campaign.setWorkflowType(WorkflowEnum.STANDARD.toString());
		campaign.setCampStatus(campStatusQuery.findByStatusId(CampaignState.DESIGN.getCode()));
		campaign.setDisabled(false);
		campaign.setCreatedTime(new Date());

		campaign.setCreater(LoginInfoHolder.getCurrentUser());
		Template template = null;
		if (null != campaignRequest.getTemplateId()) {
			template = templateQuery.findByTemplateId(campaignRequest.getTemplateId());
			campaign.setPicUrl(template.getPicUrl());
		}

		try {
			campaignCommand.createCampaign(campaign, template);
			result.put("campId", campaign.getCampId());
			return new SuccessResult(result);
		} catch (final Exception e) {
			logger.info("创建活动异常 : [{}]", e.getMessage());
			e.printStackTrace();
			throw new Exception("创建活动出现异常!");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/{id}", method = RequestMethod.PUT)
	public Result editCampaign(@PathVariable final Long id, @RequestBody final CampaignRequest campaignRequest) {
		final Campaign campaign = campaignQuery.findByCampId(id);
		campaign.setCampName(campaignRequest.getCampName());
		if (null != campaignRequest.getInverstigatorId()) {
			final User investigator = userQuery.findById(campaignRequest.getInverstigatorId());
			campaign.setInvestigator(investigator);
		}
		if (null != campaignRequest.getCampTypeId()) {
			final CampaignCategory campCategory = campCategoryQuery.findById(campaignRequest.getCampTypeId());
			campaign.setCampCategory(campCategory);
		}
		campaign.setStartTime(campaignRequest.getStartTime());
		campaign.setEndTime(campaignRequest.getEndTime());
		campaign.setCampDesc(campaignRequest.getCampDesc());

		final Map<String, Object> result = Maps.newHashMap();
		try {
			campaignCommand.saveCampaign(campaign);
			result.put("campId", id);
			return new SuccessResult(result);

		} catch (final AccessDeniedException e) {
			//FIXME:这个写在这里不好吧?但是原来的是直接catch Exception,连这个一起处理了;而且没有本地化,抛到上层也不会变成汉字.
			result.clear();
			result.put("errordesc", "没有权限");
			return new ErrorResult(result);
		} catch (final Exception e) {
			logger.info("更新活动信息异常 : [{}]", e);
			result.clear();
			result.put("errordesc", "更新活动信息失败!");
			return new ErrorResult(result);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/{id}", method = RequestMethod.GET)
	public Result showCampaign(@PathVariable final Long id) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			final Campaign campaign = campaignQuery.findByCampId(id);
			return new SuccessResult(new SingleCampaign(campaign));
		} catch (final Exception e) {
			logger.info("显示活动信息异常: [{}]", e.getMessage());
		}
		result.put("errordesc", "显示活动失败!");
		return new ErrorResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/{id}", method = RequestMethod.DELETE)
	public Result deleteCampaign(@PathVariable final Long id) throws Exception {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			final Campaign campaign = campaignQuery.findByCampId(id);
			final String statusId = campaign.getCampStatus().getStatusId();
			final CampaignState campState = CampaignState.fromCode(statusId);
			if (campState != null && campState.supportDelete()) {
				campaignCommand.deleteCampaign(campaign);
				result.put("campId", id);
				return new SuccessResult(result);
			} else {
				result.put("errordesc", "活动只有在设计中，执行完成，终止，执行错误状态下才可以执行删除操作!");
				return new ErrorResult(result);
			}
		} catch (final AccessDeniedException e) {
			//FIXME:这个写在这里不好吧?但是原来的是直接catch Exception,连这个一起处理了;而且没有本地化,抛到上层也不会变成汉字.
			result.clear();
			result.put("errordesc", "没有权限");
			return new ErrorResult(result);
		} catch (final Exception e) {
			logger.info("删除活动信息异常 : [{}]", e.getMessage());
			result.clear();
			result.put("errordesc", "删除活动失败!");
			return new ErrorResult(result);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/campaign")
	public Result batchDeleteCampaign(@RequestParam final String ids) {
		try {
			final String[] idArr = ids.split(",");
			final List<Long> idList = Lists.newArrayList();
			for (final String id : idArr) {
				idList.add(Long.parseLong(id.trim()));
			}
			final List<Campaign> campaignList = campaignQuery.findByCampIdIn(idList);
			campaignCommand.deleteCampaign(campaignList);
			return new SuccessResult();
		} catch (final Exception e) {
			logger.info("删除批量活动异常 : [{}]", e.getMessage());
		}
		final Map<String, Object> result = Maps.newHashMap();
		result.put("errordesc", "批量删除活动失败!");
		return new ErrorResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/campaign", method = RequestMethod.GET)
	public PagedResultVo<CampaignForWeb> campaignList(final CampaignListRequest listRequest) {
		try {
			final String sortname = listRequest.getSortname();
			final String sortorder = listRequest.getSortorder();
			final int page = listRequest.getPage();
			final int rp = listRequest.getRp();

			Sort sort = null;
			Pageable pageable = new PageRequest(page - 1, rp);
			if (StringUtils.isNotBlank(sortname) && StringUtils.isNotBlank(sortorder)) {
				sort = new Sort(Direction.ASC.toString().equalsIgnoreCase(sortorder) ? Direction.ASC : Direction.DESC,
						sortname);
				pageable = new PageRequest(page - 1, rp, sort);
			}

			final CampaignFilter campaignFilter = new NormalCampaignFilter();
			if (StringUtils.isNotBlank(listRequest.getCampState())) {
				campaignFilter.setCampState(listRequest.getCampState());
			}
			if (StringUtils.isNotBlank(listRequest.getPlatCode())) {
				campaignFilter.setPlatCode(listRequest.getPlatCode());
			}

			if (StringUtils.isNotBlank(listRequest.getQtype()) && StringUtils.isNotBlank(listRequest.getQuery())) {
				if ("campName".equals(listRequest.getQtype())) {
					campaignFilter.setFilterName(listRequest.getQuery());
				} else {
					campaignFilter.setKeywords(listRequest.getQuery());
				}
			}

			Page<CampaignForWeb> pageList = null;
			if (null != listRequest.getShow_activity() && "true".equals(listRequest.getShow_activity())) {
				final User user = LoginInfoHolder.getCurrentUser();
				pageList = campaignQuery.findByMySelfFilter(campaignFilter, user, pageable);
			} else {
				pageList = campaignQuery.findByFilter(campaignFilter, pageable);
			}

			final PagedResultVo<CampaignForWeb> prv = new PagedResultVo<CampaignForWeb>(pageList);
			return prv;
		} catch (final Exception e) {
			logger.info("获取活动列表异常 : [{}]", e.getMessage());
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/workflow/snapshot", method = RequestMethod.GET)
	public Result openWorkflow(@RequestParam final Long campId) {
		try {
			final Campaign campaign = campaignQuery.findByCampId(campId);
			final MxGraph graph = workflowQuery.findMxGraphById(campaign.getWorkflow().getWorkflowId());

			if (null != campaign.getPicUrl()) {
				final List<MxNode> nodes = graph.getNodes();
				graph.setNodes(nodes);
			}
			return new SuccessResult(graph);
		} catch (final Exception e) {
			logger.info("打开流程活动异常 : [{}]", e.getMessage());
		}
		final Map<String, Object> result = Maps.newHashMap();
		result.put("errordesc", "打开活动流程失败!");
		return new ErrorResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/workflow/snapshot/{id}", method = RequestMethod.POST)
	public Result saveWorkflow(@PathVariable final Long id, @RequestBody final MxGraph graph) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			final Campaign campaign = campaignQuery.findByCampId(id);
			workflowCommand.updateWorkflow(graph, campaign.getWorkflow().getWorkflowId());
			result.put("campId", id);
			return new SuccessResult(result);
		} catch (final Exception e) {
			logger.info("保存活动流程异常 : [{}]", e.getMessage());
		}
		result.clear();
		result.put("errordesc", "保存活动流程失败!");
		return new ErrorResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/check", method = RequestMethod.GET)
	public Map<String, Object> checkCampaign(@RequestParam final String campaignName) throws Exception {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			logger.info("decode : [{}]", URLDecoder.decode(campaignName, "UTF-8"));
			final boolean valid = campaignQuery.checkUniqueCampaign(URLDecoder.decode(campaignName, "UTF-8").trim());
			if (valid) {
				result.put("valid", 1);
				result.put("message", "活动名称不存在");
			} else {
				result.put("valid", 0);
				result.put("message", "活动名称已存在");
			}
			return result;
		} catch (final Exception e) {
			logger.info("检查活动名称的唯一性出现异常 : [{}]", e.getMessage());
			throw new Exception("检查活动名称的唯一性出现异常!");
		}
	}

	@AclManaged(value = "campaign", getIdMethodName = "getCampId")
	public static class SingleCampaign {
		private final Campaign campaign;

		public SingleCampaign(final Campaign campaign) {
			super();
			this.campaign = campaign;
		}

		public String getCreatedTime() {
			return null != campaign.getCreatedTime() ? DateUtils.getStringDate(campaign.getCreatedTime()) : null;
		}

		public Long getWorkflowId() {
			return null != campaign.getWorkflow() ? campaign.getWorkflow().getWorkflowId() : null;
		}

		public String getEndTime() {
			return null != campaign.getEndTime() ? DateUtils.getString(campaign.getEndTime()) : null;
		}

		public String getStartTime() {
			return null != campaign.getStartTime() ? DateUtils.getString(campaign.getStartTime()) : null;
		}

		public String getCampStatusValue() {
			return null != campaign.getCampStatus() ? campaign.getCampStatus().getStatusValue() : null;
		}

		public String getCampStatus() {
			return null != campaign.getCampStatus() ? campaign.getCampStatus().getStatusId() : null;
		}

		public String getCreater() {
			return campaign.getCreater() != null ? campaign.getCreater().getRealName() : null;
		}

		public Long getCreaterId() {
			return campaign.getCreater() != null ? campaign.getCreater().getId() : null;
		}

		public Long getInvestigatorId() {
			return null != campaign.getInvestigator() ? campaign.getInvestigator().getId() : null;
		}

		public String getInvestigator() {
			return null != campaign.getInvestigator() ? campaign.getInvestigator().getLoginName() : null;
		}

		public String getCampType() {
			return null != campaign.getCampCategory() ? campaign.getCampCategory().getCategoryValue() : null;
		}

		public Long getCampTypeId() {
			return null != campaign.getCampCategory() ? campaign.getCampCategory().getCategoryId() : null;
		}

		public Serializable getProgId() {
			return null != campaign.getProg() ? campaign.getProg().getProgId() : null;
		}

		public String getProgName() {
			return null != campaign.getProg() ? campaign.getProg().getProgName() : null;
		}

		public Long getCampId() {
			return campaign.getCampId();
		}

		public String getCampName() {
			return campaign.getCampName();
		}

		public String getCampDesc() {
			return campaign.getCampDesc();
		}

		public String getWorkflowType() {
			return campaign.getWorkflowType();
		}

		public String getPlatCode() {
			return campaign.getPlatCode();
		}

		public String getEdition() {
			return campaign.getEdition();
		}

		public String getPicUrl() {
			return campaign.getPicUrl();
		}
	}
}