package com.yunat.ccms.schedule.core.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.utils.HStringUtils;
import com.yunat.ccms.node.spi.NodeProcessor;
import com.yunat.ccms.node.spi.repository.NodeProcessorRepository;
import com.yunat.ccms.workflow.service.WorkflowConstraintService;
import com.yunat.ccms.workflow.validation.NodeConstraint;

/**
 * 从合适的Factory获得相应的NodeProcessor
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class DefaultNodeProcessorRepository implements NodeProcessorRepository, ApplicationContextAware {

	@Autowired
	private WorkflowConstraintService workflowConstraintService;

	private ApplicationContext applicationContext;
	private Map<String, NodeConstraint> nodeConfigMap = null;
	private Map<String, NodeProcessor> processorMap = null;

	public NodeProcessor getNodeProcessor(String nodeType) {
		return processorMap.get(nodeType);
	}

	@PostConstruct
	public void init() {
		processorMap = new HashMap<String, NodeProcessor>();
		nodeConfigMap = workflowConstraintService.getNodeConstraintMap();
		for (String nodeType : nodeConfigMap.keySet()) {
			NodeConstraint nodeConfig = nodeConfigMap.get(nodeType);
			Class processorClass = nodeConfig.getProcessorClass();
			String processorClassName = processorClass.getName();
			String name = HStringUtils.getLastName(processorClassName);
			String beanName = HStringUtils.toLowerCaseInitial(name, true);
			try {
				// 因为Processor有泛型，所以按照getBean(Class)方式无法获取
				NodeProcessor processor = (NodeProcessor) applicationContext.getBean(beanName);
				processorMap.put(nodeType, processor);
			} catch (BeansException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
