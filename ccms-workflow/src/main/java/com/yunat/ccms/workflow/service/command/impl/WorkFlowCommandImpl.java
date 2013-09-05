package com.yunat.ccms.workflow.service.command.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.ccms.node.spi.repository.NodeCloneHandlerRepository;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.domain.WorkFlow;
import com.yunat.ccms.workflow.repository.ConnectRepository;
import com.yunat.ccms.workflow.repository.NodeRepository;
import com.yunat.ccms.workflow.repository.WorkFlowRepository;
import com.yunat.ccms.workflow.service.WorkflowConstraintService;
import com.yunat.ccms.workflow.service.command.WorkFlowCommand;
import com.yunat.ccms.workflow.service.query.WorkFlowQuery;
import com.yunat.ccms.workflow.validation.NodeConstraint;
import com.yunat.ccms.workflow.vo.MxConnect;
import com.yunat.ccms.workflow.vo.MxGraph;
import com.yunat.ccms.workflow.vo.MxNode;

@Service
@Transactional(readOnly = true)
public class WorkFlowCommandImpl implements WorkFlowCommand {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkFlowCommandImpl.class);

	@Autowired
	private WorkflowConstraintService nodeConnectProfile;

	@Autowired
	private WorkFlowQuery workFlowQuery;

	@Autowired
	private WorkFlowRepository workFlowRepository;

	@Autowired
	private NodeRepository nodeRepository;

	@Autowired
	private ConnectRepository connectRepository;

	@Autowired
	private NodeCloneHandlerRepository nodeCloneHandlerRepository;

	@Override
	public WorkFlow createWorkflow(MxGraph mxGraph) {
		WorkFlow workflow = new WorkFlow();
		workflow.setCreateTime(new Date());
		workflow.setUpdateTime(new Date());
		workFlowRepository.saveOrUpdate(workflow);
		Long workflowId = workflow.getWorkflowId();

		List<MxNode> mxNodes = mxGraph.getNodes();
		Map<Long, Long> map = Maps.newHashMap();
		for (MxNode mxNode : mxNodes) {
			Node node = new Node();
			BeanUtils.copyProperties(mxNode, node);
			node.setId(null);
			node.setWorkflowId(workflowId);
			nodeRepository.saveOrUpdate(node);
			map.put(mxNode.getId(), node.getId());
		}

		List<MxConnect> mxConnects = mxGraph.getConnects();
		for (MxConnect mxConnect : mxConnects) {
			Connect connect = new Connect();
			BeanUtils.copyProperties(mxConnect, connect);
			connect.setId(null);
			connect.setWorkflowId(workflowId);
			connect.setSource(map.get(mxConnect.getSource()));
			connect.setTarget(map.get(mxConnect.getTarget()));
			connectRepository.saveOrUpdate(connect);
		}
		// need clean
		map.clear();
		return workflow;
	}

	@Override
	public void updateWorkflow(MxGraph mxGraph, Long workflowId) {
		MxGraph origin = workFlowQuery.findMxGraphById(workflowId);
		Map<Long, Long> nodeIdMapper = Maps.newHashMap();
		updateWorkflowNodes(mxGraph, workflowId, origin, nodeIdMapper);
		updateWorkflowConnects(mxGraph, workflowId, origin, nodeIdMapper);
		nodeIdMapper.clear();
	}

	@Override
	public WorkFlow clone(Long workflowId) {
		WorkFlow workflow = new WorkFlow();
		workflow.setCreateTime(new Date());
		workflow.setUpdateTime(new Date());
		workFlowRepository.saveOrUpdate(workflow);

		Long newWorkflowId = workflow.getWorkflowId();
		List<Node> needUpdatable = Lists.newArrayList();
		Map<Long, Long> map = cloneNodes(workflowId, newWorkflowId, needUpdatable);
		refreshClone(needUpdatable, map);
		cloneConnect(workflowId, newWorkflowId, map);
		return workflow;
	}

	private Map<Long, Long> cloneNodes(Long workflowId, Long newWorkflowId, List<Node> needUpdatable) {
		Map<String, NodeConstraint> nodeConfigMap = nodeConnectProfile.getNodeConstraintMap();
		List<Node> nodes = nodeRepository.findByWorkflowId(workflowId);
		Map<Long, Long> map = Maps.newHashMap();
		for (Node node : nodes) {
			Node newNode = new Node();
			BeanUtils.copyProperties(node, newNode);
			newNode.setId(null);
			newNode.setWorkflowId(newWorkflowId);
			nodeRepository.saveOrUpdate(newNode);
			map.put(node.getId(), newNode.getId());

			NodeConstraint nodeConfig = nodeConfigMap.get(node.getType());
			if (null != nodeConfig && nodeConfig.isCloneable()) {
				NodeCloneHandler handler = nodeCloneHandlerRepository.getNodeCloneHandler(node.getType());
				handler.clone(node.getId(), newNode.getId());
				if (handler.updatable()) {
					needUpdatable.add(node);
				}
			}
		}
		return map;
	}

	private void refreshClone(List<Node> needUpdatable, Map<Long, Long> map) {
		Map<String, NodeConstraint> nodeConfigMap = nodeConnectProfile.getNodeConstraintMap();
		for (Node node : needUpdatable) {
			NodeConstraint nodeConfig = nodeConfigMap.get(node.getType());
			if (null != nodeConfig && nodeConfig.isCloneable()) {
				NodeCloneHandler handler = nodeCloneHandlerRepository.getNodeCloneHandler(node.getType());
				handler.refresh(node.getId(), map.get(node.getId()));
			}
		}
		// need clean
		needUpdatable = null;
	}

	private void cloneConnect(Long workflowId, Long newWorkflowId, Map<Long, Long> map) {
		List<Connect> connects = connectRepository.findByWorkflowId(workflowId);
		for (Connect connect : connects) {
			Connect newConnect = new Connect();
			BeanUtils.copyProperties(connect, newConnect);
			newConnect.setId(null);
			newConnect.setSource(map.get(connect.getSource()));
			newConnect.setTarget(map.get(connect.getTarget()));
			newConnect.setWorkflowId(newWorkflowId);
			connectRepository.saveOrUpdate(newConnect);
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void updateWorkflowNodes(MxGraph dest, Long workflowId, MxGraph origin, Map<Long, Long> nodeIdMapper) {
		List<Long> nodeIdsOrigin = Lists.newArrayList();
		List<MxNode> nodesOrigin = origin.getNodes();
		for (MxNode mxNode : nodesOrigin) {
			nodeIdsOrigin.add(mxNode.getId());
		}

		List<Long> nodeIdsDest = Lists.newArrayList();
		List<MxNode> nodesDest = dest.getNodes();
		for (MxNode mxNode : nodesDest) {
			nodeIdsDest.add(mxNode.getId());
		}

		// add workflow node
		List<Long> intersection = (List<Long>) CollectionUtils.intersection(nodeIdsOrigin, nodeIdsDest);
		for (final Long intersectionNode : intersection) {
			nodeIdMapper.put(intersectionNode, intersectionNode);
		}

		List<Long> addNodes = (List<Long>) CollectionUtils.subtract(nodeIdsDest, intersection);
		for (final Long addNodeId : addNodes) {
			MxNode mxNode = (MxNode) CollectionUtils.find(nodesDest, new Predicate() {

				@Override
				public boolean evaluate(Object object) {
					MxNode mn = (MxNode) object;
					if (mn.getId().equals(addNodeId)) {
						return true;
					}
					return false;
				}
			});
			Node node = new Node();
			BeanUtils.copyProperties(mxNode, node);
			node.setId(null);
			node.setWorkflowId(workflowId);
			nodeRepository.saveOrUpdate(node);
			nodeIdMapper.put(mxNode.getId(), node.getId());
		}

		// delete workflow node & sub node record
		List<Long> deleteNodes = (List<Long>) CollectionUtils.subtract(nodeIdsOrigin, intersection);
		if (CollectionUtils.isNotEmpty(deleteNodes)) {
			nodeRepository.deleteByList(deleteNodes);
			for (final Long deletedNodeId : deleteNodes) {
				MxNode mxNode = (MxNode) CollectionUtils.find(nodesDest, new Predicate() {

					@Override
					public boolean evaluate(Object object) {
						MxNode mn = (MxNode) object;
						if (mn.getId().equals(deletedNodeId)) {
							return true;
						}
						return false;
					}
				});
				NodeCloneHandler handler = nodeCloneHandlerRepository.getNodeCloneHandler(mxNode.getType());
				handler.delete(mxNode.getId());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void updateWorkflowConnects(MxGraph dest, Long workflowId, MxGraph origin, Map<Long, Long> nodeIdMapper) {
		// connect process
		List<MxConnect> connectsOrigin = origin.getConnects();
		List<Long> connectIdsOrigin = Lists.newArrayList();
		for (MxConnect mxConnect : connectsOrigin) {
			connectIdsOrigin.add(mxConnect.getId());
		}

		List<MxConnect> connectsDest = dest.getConnects();
		List<Long> connectIdsDest = Lists.newArrayList();
		for (MxConnect mxConnect : connectsDest) {
			connectIdsDest.add(mxConnect.getId());
		}

		List<Long> intersectionConnectIds = (List<Long>) CollectionUtils.intersection(connectIdsOrigin, connectIdsDest);
		List<Long> addConnects = (List<Long>) CollectionUtils.subtract(connectIdsDest, intersectionConnectIds);
		for (final Long newConnectId : addConnects) {
			MxConnect mxConnet = (MxConnect) CollectionUtils.find(connectsDest, new Predicate() {

				@Override
				public boolean evaluate(Object object) {
					MxConnect mc = (MxConnect) object;
					if (mc.getId().equals(newConnectId)) {
						return true;
					}
					return false;
				}
			});

			Connect connect = new Connect();
			BeanUtils.copyProperties(mxConnet, connect);
			connect.setId(null);
			connect.setWorkflowId(workflowId);
			connect.setSource(nodeIdMapper.get(connect.getSource()));
			connect.setTarget(nodeIdMapper.get(connect.getTarget()));
			connectRepository.saveOrUpdate(connect);
		}

		List<Long> deletedConnects = (List<Long>) CollectionUtils.subtract(connectIdsOrigin, intersectionConnectIds);
		if (CollectionUtils.isNotEmpty(deletedConnects)) {
			connectRepository.deleteByList(deletedConnects);
		}
	}
}