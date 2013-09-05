/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 系统功能限制
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TbFunctionLimit extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 1438661603;

	/**
	 * The singleton instance of ccms.tb_function_limit
	 */
	public static final com.yunat.ccms.jooq.tables.TbFunctionLimit TB_FUNCTION_LIMIT = new com.yunat.ccms.jooq.tables.TbFunctionLimit();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 编号
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * CCMS规格版本号; BASIC: 基础版； STANDARD: 标准； ADVANCED: 高级版，ULTIMATE：旗舰版
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> EDITION = createField("edition", org.jooq.impl.SQLDataType.CHAR, this);

	/**
	 * 限制项名称，ACTIVITY_CAMPAIGNS：活动； SYS_USERS:系统用户 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> LIMIT_ITEM = createField("limit_item", org.jooq.impl.SQLDataType.CHAR, this);

	/**
	 * The table column <code>ccms.tb_function_limit.limit_count</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> LIMIT_COUNT = createField("limit_count", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 描述
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 是否有效
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Byte> DISABLED = createField("disabled", org.jooq.impl.SQLDataType.TINYINT, this);

	/**
	 * 创建时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 修改时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	public TbFunctionLimit() {
		super("tb_function_limit", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TbFunctionLimit(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TbFunctionLimit.TB_FUNCTION_LIMIT);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TB_FUNCTION_LIMIT_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TB_FUNCTION_LIMIT_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TbFunctionLimit as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TbFunctionLimit(alias);
	}
}