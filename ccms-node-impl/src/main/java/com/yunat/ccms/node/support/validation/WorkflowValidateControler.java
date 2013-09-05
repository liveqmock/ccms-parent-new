package com.yunat.ccms.node.support.validation;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.node.spi.support.ValidateMessage;

/**
 * 流程验证
 * 
 * @author xiaojing.qu
 * 
 */
@Controller
@RequestMapping(value = "/workflow/validate/*")
public class WorkflowValidateControler {

	@Autowired
	private WorkflowValidateService workflowValidateService;

	/**
	 * 得到活动的状态和周期快照
	 * 
	 * @param campId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/campaign/{campaignId}", method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	public ControlerResult validate(@PathVariable Long campaignId) {
		List<ValidateMessage> allMessages = workflowValidateService.validate(campaignId);
		WorkflowValidateResultVO vo = new WorkflowValidateResultVO();
		vo.setCampaignId(campaignId);
		vo.setVisit(DateUtils.getStringDate(new Date()));
		if (allMessages.size() > 0) {
			vo.setPass(false);
			vo.setDetails(allMessages);
			return ControlerResult.newSuccess(vo);
		} else {
			vo.setPass(true);
			return ControlerResult.newSuccess(vo);
		}
	}
}
