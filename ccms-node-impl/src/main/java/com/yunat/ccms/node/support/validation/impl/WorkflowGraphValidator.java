package com.yunat.ccms.node.support.validation.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.ValidateMessage;
import com.yunat.ccms.node.support.validation.WorkflowValidator;

/**
 * 流程图结构验证</br> <li>验证图中是否有环 <li>验证图中是否有孤立节点
 * 
 * @author xiaojing.qu
 * 
 */

@Component
public class WorkflowGraphValidator implements WorkflowValidator {

	@Override
	public List<ValidateMessage> validate(Long campaignId) {
		// TODO 未实现
		return null;
	}

}
