/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 淘宝订单交易类型
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class PltTaobaoOrderType extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 583212754;

	/**
	 * The singleton instance of ccms.plt_taobao_order_type
	 */
	public static final com.yunat.ccms.jooq.tables.PltTaobaoOrderType PLT_TAOBAO_ORDER_TYPE = new com.yunat.ccms.jooq.tables.PltTaobaoOrderType();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 交易类型，可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TYPE = createField("type", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 交易类型名称
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TYPE_NAME = createField("type_name", org.jooq.impl.SQLDataType.VARCHAR, this);

	public PltTaobaoOrderType() {
		super("plt_taobao_order_type", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public PltTaobaoOrderType(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.PltTaobaoOrderType.PLT_TAOBAO_ORDER_TYPE);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_ORDER_TYPE_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_ORDER_TYPE_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.PltTaobaoOrderType as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.PltTaobaoOrderType(alias);
	}
}
