package com.yunat.ccms.node.support.service;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yunat.ccms.configuration.variable.SystemVariable;
import com.yunat.ccms.node.support.describe.ConnectDescribeRead;
import com.yunat.ccms.node.support.describe.NodeDescribe;
import com.yunat.ccms.node.support.describe.NodeDescribeRead;
import com.yunat.ccms.workflow.service.WorkflowConstraintService;
import com.yunat.ccms.workflow.validation.NodeConnectConstraint;
import com.yunat.ccms.workflow.validation.NodeConstraint;

@Component
public class WorkflowConstraintServiceImpl implements WorkflowConstraintService, InitializingBean {

	private Map<String, NodeConstraint> nodeConstraintMap = Maps.newHashMap();
	private Map<String, NodeConnectConstraint> nodeConnectConstraintMap = Maps.newHashMap();

	@Autowired
	private SystemVariable appVar;

	@Override
	public Map<String, NodeConstraint> getNodeConstraintMap() {
		return this.nodeConstraintMap;
	}

	@Override
	public Map<String, NodeConnectConstraint> getNodeConnectConstraintMap() {
		return this.nodeConnectConstraintMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// node configuration
		NodeDescribe nodeConfiguration = new NodeDescribeRead(nodeConstraintMap, appVar.getApplicationContext());
		nodeConfiguration.load();

		// connect configuration
		NodeDescribe connectConfiguration = new ConnectDescribeRead(nodeConnectConstraintMap);
		connectConfiguration.load();

	}

}
