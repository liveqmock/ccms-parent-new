package com.yunat.ccms.biz.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.biz.controller.vo.ErrorResult;
import com.yunat.ccms.biz.controller.vo.ProgramListRequest;
import com.yunat.ccms.biz.controller.vo.ProgramRequest;
import com.yunat.ccms.biz.controller.vo.Result;
import com.yunat.ccms.biz.controller.vo.SuccessResult;
import com.yunat.ccms.biz.domain.Program;
import com.yunat.ccms.biz.service.command.ProgramCommand;
import com.yunat.ccms.biz.service.query.ProgramCategoryQuery;
import com.yunat.ccms.biz.service.query.ProgramQuery;
import com.yunat.ccms.core.support.utils.DateUtils;

@Controller
@RequestMapping(value = "/program")
public class ProgramController {
	private static Logger logger = LoggerFactory.getLogger(ProgramController.class);

	@Autowired
	private ProgramQuery programQuery;

	@Autowired
	private ProgramCommand programCommand;

	@Autowired
	private ProgramCategoryQuery programCategoryQuery;

	@ResponseBody
	@RequestMapping(value = "/program", method = RequestMethod.POST)
	public Result addProgram(@RequestBody final ProgramRequest progRequest) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			final Program program = new Program();
			program.setProgName(progRequest.getProgName());
			if (null != progRequest.getProgType()) {
				program.setType(programCategoryQuery.findById(progRequest.getProgType()));
			}
			program.setStartTime(progRequest.getStartTime());
			program.setEndTime(progRequest.getEndTime());
			program.setProgDesc(progRequest.getProgDesc());
			program.setPlatCode(progRequest.getPlatCode());
			program.setEdition(LoginInfoHolder.getProductEdition().name());
			program.setCreator(LoginInfoHolder.getCurrentUser());
			programCommand.saveProgram(program);
			result.put("progId", program.getProgId());
			return new SuccessResult(result);
		} catch (final Exception e) {
			logger.info("新建项目异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "新建项目失败!");
		return new ErrorResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/program/{id}", method = RequestMethod.PUT)
	public Result editProgram(@PathVariable final Long id, @RequestBody final ProgramRequest progRequest) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			final Program program = programQuery.findById(id);
			program.setProgName(progRequest.getProgName());
			if (null != progRequest.getProgType()) {
				program.setType(programCategoryQuery.findById(progRequest.getProgType()));
			}
			program.setStartTime(progRequest.getStartTime());
			program.setEndTime(progRequest.getEndTime());
			program.setProgDesc(progRequest.getProgDesc());
			programCommand.saveProgram(program);
			result.put("progId", program.getProgId());
			return new SuccessResult(result);
		} catch (final Exception e) {
			logger.info("修改更新项目异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "修改更新项目失败!");
		return new ErrorResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/program/{id}", method = RequestMethod.GET)
	public Result showProgram(@PathVariable final Long id) {
		final Map<String, Object> result = Maps.newHashMap();
		try {
			final Program program = programQuery.findById(id);
			jsonMapper(result, program);
			return new SuccessResult(result);
		} catch (final Exception e) {
			logger.info("获取项目异常 : [{}]", e);
		}
		result.clear();
		result.put("errordesc", "获取项目失败!");
		return new SuccessResult(result);
	}

	@ResponseBody
	@RequestMapping(value = "/program", method = RequestMethod.GET)
	public Result programList(@RequestBody final ProgramListRequest listRequest) {
		return new SuccessResult();
	}

	private void jsonMapper(final Map<String, Object> result, final Program program) {
		result.put("progId", program.getProgId());
		result.put("progName", program.getProgName());
		result.put("progType", null != program.getType() ? program.getType().getProgramCategoryId() : null);
		result.put("progTypeValue", null != program.getType() ? program.getType().getProgramCategoryValue() : null);
		result.put("creater", program.getCreator() != null ? program.getCreator().getLoginName() : null);
		result.put("createrId", program.getCreator() != null ? program.getCreator().getId() : null);
		result.put("startTime", null != program.getStartTime() ? DateUtils.getStringDate(program.getStartTime()) : null);
		result.put("endTime", null != program.getEndTime() ? DateUtils.getString(program.getEndTime()) : null);
		result.put("progDesc", program.getProgDesc());
		result.put("platCode", program.getPlatCode());
		result.put("edition", program.getEdition());
		result.put("createdTime", null != program.getCreateTime() ? DateUtils.getStringDate(program.getCreateTime())
				: null);
	}
}