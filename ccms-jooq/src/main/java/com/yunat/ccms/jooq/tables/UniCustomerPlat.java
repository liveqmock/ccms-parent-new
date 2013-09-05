/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 客户各平台ID关系表
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class UniCustomerPlat extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 916937011;

	/**
	 * The singleton instance of ccms.uni_customer_plat
	 */
	public static final com.yunat.ccms.jooq.tables.UniCustomerPlat UNI_CUSTOMER_PLAT = new com.yunat.ccms.jooq.tables.UniCustomerPlat();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 客户全局统一ID，关联uni_customer.uni_id
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> UNI_ID = createField("uni_id", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 来源平台代码，关联uni_plat.plat_code
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PLAT_CODE = createField("plat_code", org.jooq.impl.SQLDataType.CHAR, this);

	/**
	 * 客户在来源平台的客户ID，如：在taobao平台下为淘宝昵称、在paipai平台下为拍拍昵称
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CUSTOMERNO = createField("customerno", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 客户所属来源平台的优先级。平台优先级是从0开始的自然数，0最高，数字越大优先级越低
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Short> PLAT_PRIORITY = createField("plat_priority", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * 客户属性在平台内的变更时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> CHANGED = createField("changed", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	public UniCustomerPlat() {
		super("uni_customer_plat", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public UniCustomerPlat(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.UniCustomerPlat.UNI_CUSTOMER_PLAT);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_UNI_CUSTOMER_PLAT_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_UNI_CUSTOMER_PLAT_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.UniCustomerPlat as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.UniCustomerPlat(alias);
	}
}
