/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 卖家自定义类目
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class PltTaobaoItemSellerCat extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 1576400350;

	/**
	 * The singleton instance of ccms.plt_taobao_item_seller_cat
	 */
	public static final com.yunat.ccms.jooq.tables.PltTaobaoItemSellerCat PLT_TAOBAO_ITEM_SELLER_CAT = new com.yunat.ccms.jooq.tables.PltTaobaoItemSellerCat();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 卖家自定义类目编号
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SELLER_CID = createField("seller_cid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 父类目编号，值等于0：表示此类目为店铺下的一级类目，值不等于0：表示此类目有父类目
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PARENT_CID = createField("parent_cid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 卖家自定义类目名称
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 链接图片地址
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PIC_URL = createField("pic_url", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 该类目在页面上的排序位置
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> SORT_ORDER = createField("sort_order", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 店铺类目类型：可选值：manual_type：手动分类，new_type：新品上价， tree_type：二三级类目树 ，property_type：属性叶子类目树， brand_type：品牌推广
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TYPE = createField("type", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 卖家店铺ID
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DP_ID = createField("dp_id", org.jooq.impl.SQLDataType.VARCHAR, this);

	public PltTaobaoItemSellerCat() {
		super("plt_taobao_item_seller_cat", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public PltTaobaoItemSellerCat(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.PltTaobaoItemSellerCat.PLT_TAOBAO_ITEM_SELLER_CAT);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_ITEM_SELLER_CAT_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_ITEM_SELLER_CAT_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.PltTaobaoItemSellerCat as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.PltTaobaoItemSellerCat(alias);
	}
}
