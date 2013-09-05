/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 订单明细表（子订单/商品明细）
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class PltTaobaoOrderItem extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 2108060570;

	/**
	 * The singleton instance of ccms.plt_taobao_order_item
	 */
	public static final com.yunat.ccms.jooq.tables.PltTaobaoOrderItem PLT_TAOBAO_ORDER_ITEM = new com.yunat.ccms.jooq.tables.PltTaobaoOrderItem();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 子订单编号
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> OID = createField("oid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 订单编号
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TID = createField("tid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 店铺ID
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DP_ID = createField("dp_id", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 客户ID
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CUSTOMERNO = createField("customerno", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 应付金额
	 */
	public final org.jooq.TableField<org.jooq.Record, java.math.BigDecimal> TOTAL_FEE = createField("total_fee", org.jooq.impl.SQLDataType.DECIMAL, this);

	/**
	 * 订单优惠金额
	 */
	public final org.jooq.TableField<org.jooq.Record, java.math.BigDecimal> DISCOUNT_FEE = createField("discount_fee", org.jooq.impl.SQLDataType.DECIMAL, this);

	/**
	 * 手工调整金额
	 */
	public final org.jooq.TableField<org.jooq.Record, java.math.BigDecimal> ADJUST_FEE = createField("adjust_fee", org.jooq.impl.SQLDataType.DECIMAL, this);

	/**
	 * 子订单实付金额
	 */
	public final org.jooq.TableField<org.jooq.Record, java.math.BigDecimal> PAYMENT = createField("payment", org.jooq.impl.SQLDataType.DECIMAL, this);

	/**
	 * 订单状态
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> STATUS = createField("status", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 购买数量
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> NUM = createField("num", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 商品数字ID
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> NUM_IID = createField("num_iid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 交易创建时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> CREATED = createField("created", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 交易结束时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> ENDTIME = createField("endtime", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 交易来源。 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TRADE_FROM = createField("trade_from", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed一口价 auction拍卖 guarantee_trade一口价、拍卖 auto_delivery自动发货 independent_simple_trade旺店入门版交易 independent_shop_trade旺店标准版交易 ec直冲 cod货到付款 fenxiao分销 game_equipment游戏装备...
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TYPE = createField("type", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 付款时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> PAY_TIME = createField("pay_time", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 卖家发货时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> CONSIGN_TIME = createField("consign_time", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 退款状态。 可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> REFUND_STATUS = createField("refund_status", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	public final org.jooq.TableField<org.jooq.Record, java.math.BigDecimal> REFUND_FEE = createField("refund_fee", org.jooq.impl.SQLDataType.DECIMAL, this);

	/**
	 * CCMS自定义的订单状态
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Short> CCMS_ORDER_STATUS = createField("ccms_order_status", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * 商品标题
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 订单修改时间（冗余字段）
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> MODIFIED = createField("modified", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	public PltTaobaoOrderItem() {
		super("plt_taobao_order_item", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public PltTaobaoOrderItem(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.PltTaobaoOrderItem.PLT_TAOBAO_ORDER_ITEM);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_ORDER_ITEM_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_ORDER_ITEM_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.PltTaobaoOrderItem as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.PltTaobaoOrderItem(alias);
	}
}
