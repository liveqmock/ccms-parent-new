package com.yunat.ccms.node.support.repository;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.yunat.ccms.configuration.variable.SystemVariable;
import com.yunat.ccms.core.support.utils.HStringUtils;
import com.yunat.ccms.node.spi.repository.NodeValidatorRepository;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.workflow.service.WorkflowConstraintService;
import com.yunat.ccms.workflow.validation.NodeConstraint;

@Component
public class DefaultNodeValidatorRepository implements NodeValidatorRepository {

	@Autowired
	private WorkflowConstraintService workflowConstraintService;

	@Autowired
	private SystemVariable appVar;

	private Map<String, NodeValidator> validatorMap = new HashMap<String, NodeValidator>();

	public NodeValidator getNodeValidator(String nodeType) {
		return validatorMap.get(nodeType);
	}

	@PostConstruct
	public void init() {
		Map<String, NodeConstraint> nodeConfigMap = workflowConstraintService.getNodeConstraintMap();
		ApplicationContext applicationContext = appVar.getApplicationContext();
		for (String nodeType : nodeConfigMap.keySet()) {
			NodeConstraint nodeConfig = nodeConfigMap.get(nodeType);
			Class validatorClass = nodeConfig.getValidatorClass();
			String validatorClassName = validatorClass.getName();
			String name = HStringUtils.getLastName(validatorClassName);
			String beanName = HStringUtils.toLowerCaseInitial(name, true);
			try {
				// 因为Processor有泛型，所以按照getBean(Class)方式无法获取
				NodeValidator processor = (NodeValidator) applicationContext.getBean(beanName);
				validatorMap.put(nodeType, processor);
			} catch (BeansException e) {
				e.printStackTrace();
			}

		}
	}

}
