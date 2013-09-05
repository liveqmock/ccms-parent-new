package com.yunat.ccms.dashboard.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.dashboard.repository.DashboardChannelRepository;
import com.yunat.ccms.node.biz.wait.NodeWait;
import com.yunat.ccms.workflow.domain.Connect;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.repository.ConnectRepository;

@Service
public class DashboardChannelService {
	
	@Autowired
	private DashboardChannelRepository  channelRepository;
	
	@Autowired
	private ConnectRepository connectRepository;
	
	@Transactional(readOnly = true) 
	public List<Map<String,Object>>  channelSendInfo(String channelType){
		String  nowDate=new SimpleDateFormat( "yyyy-MM-dd").format(new Date());
		return  channelRepository.queryChannelSendInfo(channelType,nowDate);
	}
	
	
	
	
	/**
	 * TODO  移除到节点工程业务层更加合适
	 * 获取等待节点
	 */
	@Transactional(readOnly = true)
	public NodeWait  getWaitNode(Long id){
		return null;
	}
	
	
	/**
	 * 
	 * @param nodeId
	 * @param type
	 * @return
	 * @desc 递归去找前面节点类型为type的节点
	 * 这里主要是为了首页渠道节点去找它前面的等待节点个数，来估算发送时间
	 */
	public Set<Long> search(Long nodeId, String type){
		Set<Long> nodeIdSet = new java.util.TreeSet<Long>();
		search(nodeIdSet, nodeId, type);
		return nodeIdSet;
	}
	
	private void search(Set<Long> nodeIdSet, Long nodeId, String type){
		List<Connect> connectList = connectRepository.findByTargetNodeId(nodeId);
		for (Connect connect:connectList){
			Node sourceNode = connect.getSourceNode();
			if (sourceNode == null){
				return;
			}
			Long sourceNodeId = sourceNode.getId();
			if (type.equals(sourceNode.getType())){
				nodeIdSet.add(sourceNodeId);
			}
			search(nodeIdSet, sourceNodeId, type);
		}
	}
	
}
