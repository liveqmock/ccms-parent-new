package com.yunat.ccms.schedule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.NodeData;
import com.yunat.ccms.node.spi.NodeInput;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.support.io.DefaultNodeInput;
import com.yunat.ccms.node.support.io.Empty;
import com.yunat.ccms.schedule.domain.LogJobData;
import com.yunat.ccms.schedule.domain.LogSubjob;
import com.yunat.ccms.schedule.repository.LogJobDataDao;
import com.yunat.ccms.schedule.repository.WorkflowDao;

/**
 * 得到当前节点的输入，保存当前节点的输出
 * 
 * @author xiaojing.qu
 * 
 */
@Component
@Scope("singleton")
public class JobDataService {

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private LogJobDataDao jobdataDao;

	public void saveJobData(LogSubjob subjob, NodeOutput output) {
		if (output == null) {
			return;
		}
		Long campId = subjob.getCampId();
		Long jobId = subjob.getJobId();
		Long subjobId = subjob.getSubjobId();
		Long nodeId = subjob.getNode().getId();
		jobdataDao.clearJobData(subjobId, nodeId);
		List<Long> nextIds = workflowDao.getNextNodeIds(nodeId);
		for (Long nextNodeId : nextIds) {
			LogJobData jobdata = new LogJobData(campId, jobId, subjobId, nodeId);
			jobdata.setTarget(nextNodeId);
			NodeData data = output.getOutputData(nextNodeId);
			if (data != null && !(data instanceof Empty)) {
				jobdata.setDataType(data.getType());
				jobdata.setDataCode(data.getCode());
			}
			jobdataDao.save(jobdata);
		}

	}

	public NodeInput getNodeInput(LogSubjob subjob) {
		NodeInput nodeInput = null;
		Long jobId = subjob.getJobId();
		Long nodeId = subjob.getNode().getId();
		List<LogJobData> jobdataList = jobdataDao.getJobDataByJobAndTarget(jobId, nodeId);
		if (jobdataList == null) {

		} else if (jobdataList.size() == 1) {
			LogJobData jobdata = jobdataList.get(0);
			nodeInput = new DefaultNodeInput(nodeId, jobdata.getSource(), jobdata.toNodeData());
		} else {
			Map<Long, NodeData> map = new HashMap<Long, NodeData>();
			for (LogJobData jobdata : jobdataList) {
				NodeData nodedata = jobdata.toNodeData();
				map.put(jobdata.getSource(), nodedata);
			}
			nodeInput = new DefaultNodeInput(nodeId, map);
		}
		return nodeInput;
	}

}
