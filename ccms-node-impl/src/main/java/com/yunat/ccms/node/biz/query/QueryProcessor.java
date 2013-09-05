package com.yunat.ccms.node.biz.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.metadata.support.SetOperationSqlSupport;
import com.yunat.ccms.node.biz.AbstractNodeProcessor;
import com.yunat.ccms.node.spi.NodeOutput;
import com.yunat.ccms.node.spi.NodeProcessingContext;
import com.yunat.ccms.node.spi.NodeProcessingException;
import com.yunat.ccms.node.support.io.DefaultNodeOutput;
import com.yunat.ccms.node.support.io.Table;
import com.yunat.ccms.node.support.tmpdata.TemporaryTableDecorator;

@Component
public class QueryProcessor extends AbstractNodeProcessor<NodeQuery> {

	@Autowired
	QueryNodeExecuteService queryNodeExecuteService;

	@Autowired
	QueryNodeConfigService queryNodeConfigService;

	@Autowired
	TemporaryTableDecorator nodeTempTableDecorator;

	@Autowired
	NodeQuerySupport nodeQuerySupport;

	public NodeOutput process(NodeQuery nodeQuery, NodeProcessingContext context) throws NodeProcessingException {

		// 运行上下文
		if (context == null) {
			throw new NodeProcessingException("query node: the context could not be null.");
		}

		Long nodeId = context.getNodeId();
		Long jobId = context.getJobId();
		Long subjobId = context.getSubjobId();

		if (nodeId == null) {
			throw new NodeProcessingException("query node: the node id in context cound not be null.");
		}

		if (jobId == null) {
			throw new NodeProcessingException("query node: the job id in context could not be null.");
		}

		if (subjobId == null) {
			throw new NodeProcessingException("query node: the subjob id in context could not be null.");
		}

		// 输入表或视图
		String nodeInputTableName = context.getNodeInput().getInputData(context.getNodeInput().getInputNodes().get(0))
				.getCode();
		if (nodeInputTableName == null || "".equals(nodeInputTableName)) {
			throw new NodeProcessingException("query node: the input code(name) of node data could not be null.");
		}

		// 查询节点的界面基本配置
		NodeQuery node = queryNodeConfigService.loadQueryNodeByID(nodeId);

		String tempTable = nodeInputTableName;
		List<String> tempTableNameList = new ArrayList<String>();

		// 客户属性查询
		tempTable = queryNodeExecuteService.executePropertiesQuery(nodeId, jobId, subjobId, tempTable,
				tempTableNameList);

		// 消费信息查询
		tempTable = queryNodeExecuteService.executeConsumeQuery(nodeId, jobId, subjobId, tempTable, tempTableNameList);

		// 生成输出
		String nodeOutputTableName = this.createOutputNode(nodeId, jobId, subjobId, nodeInputTableName, tempTable,
				node.getIsExclude());

		// 删除节点内部生成的临时表
		if (tempTableNameList != null || tempTableNameList.size() > 0) {
			for (String tempTableName : tempTableNameList) {

				nodeQuerySupport.dropNodeTempTable(tempTableName);
			}
		}

		return new DefaultNodeOutput(nodeId, new Table(nodeOutputTableName), this.getOutputMessage(nodeOutputTableName));
	}

	/**
	 * 产生最终的查询节点输出
	 * 
	 * @param nodeId
	 * @param jobId
	 * @param subjobId
	 * @param nodeInputTableName
	 *            节点输入的表名
	 * @param tempTableName
	 *            查询节点处理过程最后一个临时表名
	 * @param exclude
	 *            是否排除 true排除（补集），false不排除（交集）
	 * @return
	 */
	private String createOutputNode(long nodeId, long jobId, long subjobId, String nodeInputTableName,
			String tempTableName, boolean exclude) {

		// 默认使用并且（交集）
		String sql = SetOperationSqlSupport.genIntersectionSetSql(nodeInputTableName, tempTableName);

		// 如果选择排除（补集）
		if (exclude) {

			sql = SetOperationSqlSupport.genComplementarySetSql(nodeInputTableName, tempTableName);
		}

		return this.nodeTempTableDecorator.generate(jobId, subjobId, null, sql);
	}
}
