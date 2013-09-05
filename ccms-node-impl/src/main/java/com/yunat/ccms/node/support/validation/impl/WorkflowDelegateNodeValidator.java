package com.yunat.ccms.node.support.validation.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.yunat.ccms.configuration.variable.SystemVariable;
import com.yunat.ccms.node.spi.NodeEntity;
import com.yunat.ccms.node.spi.repository.NodeEntityRepository;
import com.yunat.ccms.node.spi.repository.NodeValidatorRepository;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;
import com.yunat.ccms.node.support.validation.WorkflowValidator;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.domain.WorkFlow;
import com.yunat.ccms.workflow.repository.WorkFlowRepository;
import com.yunat.ccms.workflow.service.WorkflowConstraintService;
import com.yunat.ccms.workflow.validation.NodeConstraint;

/**
 * 按照流程图中依次调用节点的验证
 * 1:验证节点是否配置
 * 2：验证时间相关节点时间是否配置正确
 * 
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class WorkflowDelegateNodeValidator implements WorkflowValidator {

	private static Logger logger = LoggerFactory.getLogger(WorkflowDelegateNodeValidator.class);

	@Autowired
	private SystemVariable appVar;

	@Autowired
	private WorkflowConstraintService workflowConstraintService;

	@Autowired
	private WorkFlowRepository workflowRespository;

	@Autowired
	private NodeValidatorRepository nodeValidatorRepository;

	@Autowired
	private NodeEntityRepository nodeEntityRespository;

	@Override
	@SuppressWarnings({ "unused", "unchecked" })
	public List<ValidateMessage> validate(Long campaignId) {
		List<ValidateMessage> messages = new ArrayList<ValidateMessage>();
		ApplicationContext appContext = appVar.getApplicationContext();
		Map<String, NodeConstraint> nodeConstraintsMap = workflowConstraintService.getNodeConstraintMap();
		WorkFlow workflow = workflowRespository.findByCampId(campaignId);
		if (workflow == null) {
			messages.add(ValidateMessage.forError("流程未保存"));
			return messages;
		}
		// 基础版只做有无保存的验证，不验证时间
		// 下面是孤立的节点验证
		Set<Node> nodes = workflow.getAllNodes();
		for (Node node : nodes) {
			Long nodeId = node.getId();
			String nodeType = node.getType();
			NodeEntity entity = nodeEntityRespository.getNodeEntity(nodeType, nodeId);
			logger.info("Get NodeEntity for Validation,NodeType:{},NodeId:{},return:{}", new Object[] { nodeType,
					nodeId, entity });
			NodeValidator<NodeEntity> validator = nodeValidatorRepository.getNodeValidator(nodeType);
			if (validator == null) {
				logger.error("Validator for NodeType:{} not Found!!!!!!!!!!", nodeType);
				continue;
			}
			DefaultNodeValidationContext validationContext = new DefaultNodeValidationContext(nodeId, new Date());
			ValidateMessage msg = validator.validate(entity, validationContext);
			logger.info("Validator:{} validate Node:{},return:{}", new Object[] { validator.getClass().getSimpleName(),
					nodeId, msg });
			if (msg != null) {
				messages.add(msg);
			}

		}
		return messages;
	}
}
