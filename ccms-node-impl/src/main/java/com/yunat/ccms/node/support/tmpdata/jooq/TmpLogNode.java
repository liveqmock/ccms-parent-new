package com.yunat.ccms.node.support.tmpdata.jooq;

import com.yunat.ccms.core.support.cons.ColumnConstant;

public class TmpLogNode extends AbstractJooqTmplTable {

	private static final long serialVersionUID = -6860979720963497434L;

	public static final TmpLogNode TMP_LOG_NODE = new TmpLogNode();

	public final org.jooq.TableField<org.jooq.Record, java.lang.String> UNI_ID = createField(ColumnConstant.UNI_ID,
			org.jooq.impl.SQLDataType.VARCHAR, this);
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> CONTROL_GROUP_TYPE = createField(
			ColumnConstant.CONTROL_GROUP_TYPE, org.jooq.impl.SQLDataType.INTEGER, this);

	public TmpLogNode() {
		super(TemplateTablePrefix.TMP_LOG_NODE, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpLogNode(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmpLogNode(java.lang.String alias, TmpLogNode table) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, table);
	}

	@Override
	public TmpLogNode as(java.lang.String alias) {
		return new TmpLogNode(alias, this);
	}

	@Override
	public String getDDL() {
		StringBuffer ddl = new StringBuffer();
		ddl.append("CREATE TABLE tmp_log_node_? (" + 
				"uni_id varchar(64) , " + 
				"control_group_type int(2) , " + 
				"KEY idx_tmp_log_node (user_id)" + 
				");");
		return ddl.toString();
	}

}