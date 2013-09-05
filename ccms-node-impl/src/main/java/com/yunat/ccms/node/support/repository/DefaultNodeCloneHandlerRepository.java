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
import com.yunat.ccms.node.spi.repository.NodeCloneHandlerRepository;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;
import com.yunat.ccms.workflow.service.WorkflowConstraintService;
import com.yunat.ccms.workflow.validation.NodeConstraint;

@Component
public class DefaultNodeCloneHandlerRepository implements NodeCloneHandlerRepository {

	@Autowired
	private WorkflowConstraintService workflowConstraintService;

	@Autowired
	private SystemVariable appVar;

	private Map<String, NodeCloneHandler> cloneHandlerMap = new HashMap<String, NodeCloneHandler>();

	@Override
	public NodeCloneHandler getNodeCloneHandler(String nodeType) {
		return cloneHandlerMap.get(nodeType);
	}

	@PostConstruct
	public void init() {
		Map<String, NodeConstraint> nodeConfigMap = workflowConstraintService.getNodeConstraintMap();
		ApplicationContext applicationContext = appVar.getApplicationContext();
		for (String nodeType : nodeConfigMap.keySet()) {
			NodeConstraint nodeConfig = nodeConfigMap.get(nodeType);
			Class handlerClass = nodeConfig.getHandlerClass();
			String handlerClassName = handlerClass.getName();
			String name = HStringUtils.getLastName(handlerClassName);
			String beanName = HStringUtils.toLowerCaseInitial(name, true);
			try {
				// 因为NodeCloneHandler有泛型，所以按照getBean(Class)方式无法获取
				NodeCloneHandler handler = (NodeCloneHandler) applicationContext.getBean(beanName);
				cloneHandlerMap.put(nodeType, handler);
			} catch (BeansException e) {
				e.printStackTrace();
			}

		}
	}
}
