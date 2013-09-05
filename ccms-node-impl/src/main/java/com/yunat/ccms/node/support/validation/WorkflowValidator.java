package com.yunat.ccms.node.support.validation;

import java.util.List;

import com.yunat.ccms.node.spi.support.ValidateMessage;

/**
 * 流程验证接口
 * 
 * @author xiaojing.qu
 * 
 */
public interface WorkflowValidator {

	public List<ValidateMessage> validate(Long campaignId);

}
