package com.yunat.ccms.schedule.controler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.schedule.service.ScheduleService;

/**
 * 活动执行，中止，节点恢复
 * 
 * @author xiaojing.qu
 * 
 */
@Controller
@RequestMapping(value = "/schedule/*")
public class ScheduleControler {

	private static Logger logger = LoggerFactory.getLogger(ScheduleControler.class);

	@Autowired
	private ScheduleService scheduleService;

	@ResponseBody
	@RequestMapping(value = "/campaign/{campId}/test", method = RequestMethod.GET)
	public ControlerResult test(@PathVariable Long campId) {
		logger.info("test:{}", campId);
		return scheduleService.execute(campId, true);
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/{campId}/stopTest", method = RequestMethod.GET)
	public ControlerResult stopTest(@PathVariable Long campId) {
		logger.info("stopTest:{}", campId);
		return scheduleService.stop(campId, true);
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/{campId}/execute", method = RequestMethod.GET)
	public ControlerResult execute(@PathVariable Long campId) {
		logger.info("execute:{}", campId);
		return scheduleService.execute(campId, false);
	}

	@ResponseBody
	@RequestMapping(value = "/campaign/{campId}/stop", method = RequestMethod.GET)
	public ControlerResult stop(@PathVariable Long campId) {
		logger.info("stop:{}", campId);
		return scheduleService.stop(campId, false);
	}

	@ResponseBody
	@RequestMapping(value = "/job/{jobId}/node/{nodeId}/recover", method = RequestMethod.PUT)
	public ControlerResult recover(@PathVariable Long jobId, @PathVariable Long nodeId) {
		logger.info("recover,job:{},node:{}", jobId, nodeId);
		return scheduleService.recover(jobId, nodeId);
	}
}
