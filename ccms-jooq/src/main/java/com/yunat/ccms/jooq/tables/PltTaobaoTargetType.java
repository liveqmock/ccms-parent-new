/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 订单查询的客户汇总指标字典表
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class PltTaobaoTargetType extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 333407218;

	/**
	 * The singleton instance of ccms.plt_taobao_target_type
	 */
	public static final com.yunat.ccms.jooq.tables.PltTaobaoTargetType PLT_TAOBAO_TARGET_TYPE = new com.yunat.ccms.jooq.tables.PltTaobaoTargetType();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The table column <code>ccms.plt_taobao_target_type.target_id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> TARGET_ID = createField("target_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The table column <code>ccms.plt_taobao_target_type.target_value</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TARGET_VALUE = createField("target_value", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>ccms.plt_taobao_target_type.target_desc</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TARGET_DESC = createField("target_desc", org.jooq.impl.SQLDataType.VARCHAR, this);

	public PltTaobaoTargetType() {
		super("plt_taobao_target_type", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public PltTaobaoTargetType(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.PltTaobaoTargetType.PLT_TAOBAO_TARGET_TYPE);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_TARGET_TYPE_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_TARGET_TYPE_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.PltTaobaoTargetType as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.PltTaobaoTargetType(alias);
	}
}
