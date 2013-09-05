/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 节点类型分组
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TwfNodeTypeGroup extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 256710249;

	/**
	 * The singleton instance of ccms.twf_node_type_group
	 */
	public static final com.yunat.ccms.jooq.tables.TwfNodeTypeGroup TWF_NODE_TYPE_GROUP = new com.yunat.ccms.jooq.tables.TwfNodeTypeGroup();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 主键ID
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 名称
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 显示文本
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TEXT = createField("text", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 备注
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR, this);

	public TwfNodeTypeGroup() {
		super("twf_node_type_group", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TwfNodeTypeGroup(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TwfNodeTypeGroup.TWF_NODE_TYPE_GROUP);
	}

	@Override
	public org.jooq.Identity<org.jooq.Record, java.lang.Long> getIdentity() {
		return com.yunat.ccms.jooq.Keys.IDENTITY_TWF_NODE_TYPE_GROUP;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TWF_NODE_TYPE_GROUP_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TWF_NODE_TYPE_GROUP_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TwfNodeTypeGroup as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TwfNodeTypeGroup(alias);
	}
}
