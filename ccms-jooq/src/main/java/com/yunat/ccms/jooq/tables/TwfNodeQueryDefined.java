/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 已定义查询（保存界面定义的查询）
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TwfNodeQueryDefined extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 334094034;

	/**
	 * The singleton instance of ccms.twf_node_query_defined
	 */
	public static final com.yunat.ccms.jooq.tables.TwfNodeQueryDefined TWF_NODE_QUERY_DEFINED = new com.yunat.ccms.jooq.tables.TwfNodeQueryDefined();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The table column <code>ccms.twf_node_query_defined.query_defined_id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> QUERY_DEFINED_ID = createField("query_defined_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 节点
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> NODE_ID = createField("node_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 查询模版
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> QUERY_ID = createField("query_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 关系符and或者or
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RELATION = createField("relation", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 额外控制信息，提供扩展灵活性
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> EXT_CTRL_INFO = createField("ext_ctrl_info", org.jooq.impl.SQLDataType.VARCHAR, this);

	public TwfNodeQueryDefined() {
		super("twf_node_query_defined", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TwfNodeQueryDefined(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TwfNodeQueryDefined.TWF_NODE_QUERY_DEFINED);
	}

	@Override
	public org.jooq.Identity<org.jooq.Record, java.lang.Long> getIdentity() {
		return com.yunat.ccms.jooq.Keys.IDENTITY_TWF_NODE_QUERY_DEFINED;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TWF_NODE_QUERY_DEFINED_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TWF_NODE_QUERY_DEFINED_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TwfNodeQueryDefined as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TwfNodeQueryDefined(alias);
	}
}
