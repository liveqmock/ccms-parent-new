package com.yunat.ccms.schedule.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.schedule.service.ScheduleStatusService;

/**
 * Schedule状态信息
 * 
 * @author xiaojing.qu
 * 
 */
@Controller
@RequestMapping(value = "/schedule/status")
public class ScheduleStatusControler {

	@Autowired
	private ScheduleStatusService statusService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public ControlerResult test() {
		Object resultVo = statusService.getStatus();
		return ControlerResult.newSuccess(resultVo);
	}
}
