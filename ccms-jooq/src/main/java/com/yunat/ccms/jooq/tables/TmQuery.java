/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 查询模版，代表一个查询的结构，但是查询条件尚未明确，需要用户保存查询条件
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TmQuery extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 1196872203;

	/**
	 * The singleton instance of ccms.tm_query
	 */
	public static final com.yunat.ccms.jooq.tables.TmQuery TM_QUERY = new com.yunat.ccms.jooq.tables.TmQuery();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The table column <code>ccms.tm_query.query_id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> QUERY_ID = createField("query_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 有含义的查询代码，唯一标识作用
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CODE = createField("code", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 显示名称
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SHOW_NAME = createField("show_name", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 数据库视图名称
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PHY_VIEW = createField("phy_view", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 平台代码
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PLAT_CODE = createField("plat_code", org.jooq.impl.SQLDataType.VARCHAR, this);

	public TmQuery() {
		super("tm_query", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TmQuery(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TmQuery.TM_QUERY);
	}

	@Override
	public org.jooq.Identity<org.jooq.Record, java.lang.Long> getIdentity() {
		return com.yunat.ccms.jooq.Keys.IDENTITY_TM_QUERY;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TM_QUERY_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TM_QUERY_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TmQuery as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TmQuery(alias);
	}
}