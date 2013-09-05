package com.yunat.ccms.node.support.tmpdata;

import static com.yunat.ccms.core.support.cons.ColumnConstant.CONTROL_GROUP_TYPE;
import static com.yunat.ccms.core.support.cons.ColumnConstant.DB_TYPE_CONTROL_GROUP_TYPE;
import static com.yunat.ccms.core.support.cons.ColumnConstant.DB_TYPE_USER_ID;
import static com.yunat.ccms.core.support.cons.ColumnConstant.UNI_ID;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.annotation.ResultType;
import com.yunat.ccms.node.spi.NodeTemporaryDataHandler;
import com.yunat.ccms.node.support.NodeSQLExecutor;

/**
 * role :ConcreteComponent
 * 
 * @author yinwei
 */
@Component(value = "temporaryTable")
public class TemporaryTable implements NodeTemporaryDataHandler {

	private static final Map<String, String> NODE_TEMP_TABLE_COLS = new LinkedHashMap<String, String>();
	static {
		NODE_TEMP_TABLE_COLS.put(UNI_ID, DB_TYPE_USER_ID);
		NODE_TEMP_TABLE_COLS.put(CONTROL_GROUP_TYPE, DB_TYPE_CONTROL_GROUP_TYPE);
	}

	@Autowired
	private NodeSQLExecutor nodeSQLExecutor;

	@Autowired
	private TemporayDataNamingConvention namingConvention;

	public String generate(Long jobId, Long subjobId, String suffix, String sql) {
		return this.createNodeTempTable(jobId, subjobId, suffix, sql, true);
	}

	private int getTmpTblDDL(String nodeTmpTblName) {
		StringBuffer ddl = new StringBuffer();
		ddl.append("CREATE TABLE " + nodeTmpTblName + " (" + "uni_id varchar(64) , " + "control_group_type int(2) , "
				+ "KEY idx_tmp_uni_id (uni_id)" + ");");
		return nodeSQLExecutor.execute(ddl.toString());
	}

	private String createNodeTempTable(Long jobId, Long subjobId, String suffix, String sql, boolean ifCreateBySelect) {
		// 建临时表
		String nodeTmpTblName = namingConvention.getTemporaryDataName(subjobId, suffix, ResultType.TABLE);
		String dropsql = "Drop table if exists " + nodeTmpTblName;
		nodeSQLExecutor.execute(dropsql);

		StringBuffer sb = new StringBuffer();
		this.getTmpTblDDL(nodeTmpTblName);
		if (ifCreateBySelect) {// 建含数据的临时表
			sb.append("insert into " + nodeTmpTblName + " ").append(sql + ";");
		}
		nodeSQLExecutor.execute(sb.toString());
		return nodeTmpTblName;
	}

	@Override
	public void destroy(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public String convert(Long subjobId, String suffix, String name) {
		// TODO Auto-generated method stub
		return null;
	}

}