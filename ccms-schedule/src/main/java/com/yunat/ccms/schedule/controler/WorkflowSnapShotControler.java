package com.yunat.ccms.schedule.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.schedule.service.WorkflowSnapShotService;

/**
 * 活动中路程的显示
 * 
 * @author xiaojing.qu
 * 
 */
@Controller
@RequestMapping(value = "/schedule/*")
public class WorkflowSnapShotControler {

	@Autowired
	private WorkflowSnapShotService workflowSnapShotService;

	/**
	 * 得到活动的状态和周期快照
	 * 
	 * @param campId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/campaign/{campaignId}/snapshot", method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	public ControlerResult getCampaignSnapShot(@PathVariable Long campaignId) {
		return workflowSnapShotService.getCampaignSnapShot(campaignId);
	}

	/**
	 * 列出job下的节点状态快照
	 * 
	 * @param jobId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/job/{jobId}/node/snapshot", method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	public ControlerResult getJobSnapShot(@PathVariable Long jobId) {
		return workflowSnapShotService.getJobSnapShot(jobId);
	}
}
