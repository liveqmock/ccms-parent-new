package com.yunat.ccms.node.biz.query;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.annotation.ResultType;
import com.yunat.ccms.node.support.NodeSQLExecutor;
import com.yunat.ccms.node.support.tmpdata.TemporayDataNamingConvention;

@Component
public class NodeQuerySupport {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NodeSQLExecutor nodeSQLExecutor;

	@Autowired
	private TemporayDataNamingConvention namingConvention;

	// 建临时表
	public String createNodeTempTable(Long jobId, Long subjobId, String suffix, String indexColumn, String subSql,
			List<String> tempTableNameList) {

		String nodeTmpTblName = namingConvention.getTemporaryDataName(subjobId, suffix, ResultType.TABLE);
		String dropsql = "Drop table if exists " + nodeTmpTblName;
		nodeSQLExecutor.execute(dropsql);

		StringBuffer sql = new StringBuffer();
		sql.append("Create table ").append(nodeTmpTblName);
		sql.append(" As ").append(subSql);
		logger.info(sql.toString());
		nodeSQLExecutor.execute(sql.toString());

		if (tempTableNameList != null) {

			tempTableNameList.add(nodeTmpTblName);
		}

		// 建临时表索引
		sql.setLength(0);
		sql.append("Create Index idx_nodetemp_").append(subjobId).append("_").append(indexColumn);
		if (null != suffix)
			sql.append("_").append(suffix);
		sql.append(" On ").append(nodeTmpTblName).append("(");
		sql.append(indexColumn);
		sql.append(")");
		nodeSQLExecutor.execute(sql.toString());

		return nodeTmpTblName;
	}

	// 通过标准接口删临时表
	public void dropNodeTempTable(Long jobId, Long subjobId, String suffix) {

		String nodeTmpTblName = namingConvention.getTemporaryDataName(subjobId, suffix, ResultType.TABLE);
		dropNodeTempTable(nodeTmpTblName);
	}

	// 通过名称删临时表
	public void dropNodeTempTable(String nodeTmpTblName) {

		String dropsql = "Drop table if exists " + nodeTmpTblName;
		nodeSQLExecutor.execute(dropsql);
	}
}
