package com.yunat.ccms.schedule.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.evaluate.NodeEvaluate;
import com.yunat.ccms.node.biz.time.NodeTime;
import com.yunat.ccms.node.biz.wait.NodeWait;
import com.yunat.ccms.schedule.support.MybatisBaseDao;
import com.yunat.ccms.workflow.domain.Node;
import com.yunat.ccms.workflow.domain.WorkFlow;

@Component
@Scope("singleton")
public class WorkflowDao extends MybatisBaseDao {

	public WorkFlow getWorkflowByCampId(Long campId) {
		return super.get(campId);
	}

	public Node getNode(Long nodeId) {
		return super.get(nodeId);
	}

	public List<Long> getNextNodeIds(Long nodeId) {
		return super.list(nodeId);
	}

	public List<Node> getNodeByCampIdAndType(Long campId, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campId", campId);
		map.put("type", type);
		return super.list(map);
	}

	public NodeWait getWaitNodeByID(Long nodeId) {
		return super.get(nodeId);
	}

	public NodeTime getTimeNodeByID(Long nodeId) {
		return super.get(nodeId);
	}

	public NodeEvaluate getEvaluateNodeByID(Long nodeId) {
		return super.get(nodeId);
	}

}
