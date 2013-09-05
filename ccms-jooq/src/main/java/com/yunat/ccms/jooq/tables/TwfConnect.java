/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 流程的连接信息表
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TwfConnect extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 164676397;

	/**
	 * The singleton instance of ccms.twf_connect
	 */
	public static final com.yunat.ccms.jooq.tables.TwfConnect TWF_CONNECT = new com.yunat.ccms.jooq.tables.TwfConnect();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The table column <code>ccms.twf_connect.connect_id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> CONNECT_ID = createField("connect_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The table column <code>ccms.twf_connect.edge</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> EDGE = createField("edge", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>ccms.twf_connect.workflow_id</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> WORKFLOW_ID = createField("workflow_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The table column <code>ccms.twf_connect.source</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> SOURCE = createField("source", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The table column <code>ccms.twf_connect.target</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> TARGET = createField("target", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The table column <code>ccms.twf_connect.relative</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RELATIVE = createField("relative", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>ccms.twf_connect.as_t</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> AS_T = createField("as_t", org.jooq.impl.SQLDataType.VARCHAR, this);

	public TwfConnect() {
		super("twf_connect", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TwfConnect(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TwfConnect.TWF_CONNECT);
	}

	@Override
	public org.jooq.Identity<org.jooq.Record, java.lang.Long> getIdentity() {
		return com.yunat.ccms.jooq.Keys.IDENTITY_TWF_CONNECT;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TWF_CONNECT_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TWF_CONNECT_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TwfConnect as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TwfConnect(alias);
	}
}
