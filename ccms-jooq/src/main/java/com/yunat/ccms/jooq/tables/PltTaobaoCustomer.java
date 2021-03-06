/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 客户信息表
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class PltTaobaoCustomer extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -210938022;

	/**
	 * The singleton instance of ccms.plt_taobao_customer
	 */
	public static final com.yunat.ccms.jooq.tables.PltTaobaoCustomer PLT_TAOBAO_CUSTOMER = new com.yunat.ccms.jooq.tables.PltTaobaoCustomer();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 用户id，CCMS系统用户ID,淘宝UID(number)
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CUSTOMERNO = createField("customerno", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 客户姓名
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> FULL_NAME = createField("full_name", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 性别。可选值:m(男),f(女)
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SEX = createField("sex", org.jooq.impl.SQLDataType.CHAR, this);

	/**
	 * 买家信用等级
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> BUYER_CREDIT_LEV = createField("buyer_credit_lev", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 买家信用评分
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> BUYER_CREDIT_SCORE = createField("buyer_credit_score", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 买家好评订单数
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> BUYER_CREDIT_GOOD_NUM = createField("buyer_credit_good_num", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 买家累计订单数
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> BUYER_CREDIT_TOTAL_NUM = createField("buyer_credit_total_num", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * 邮编
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> ZIP = createField("zip", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 地址
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> ADDRESS = createField("address", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 城市
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CITY = createField("city", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 省份
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> STATE = createField("state", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 国家
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> COUNTRY = createField("country", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 区域
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DISTRICT = createField("district", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 用户淘宝注册时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> CREATED = createField("created", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 最后登陆时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> LAST_VISIT = createField("last_visit", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 生日
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Date> BIRTHDAY = createField("birthday", org.jooq.impl.SQLDataType.DATE, this);

	/**
	 * 用户的全站vip信息，可取值如下：c(普通会员),asso_vip(荣誉会员)，vip1,vip2,vip3,vip4,vip5,vip6(六个等级的正式vip会员)，共8种取值，其中asso_vip是由vip会员衰退而成，与主站上的vip0对应。
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> VIP_INFO = createField("vip_info", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 用户email
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> EMAIL = createField("email", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 用户手机号
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> MOBILE = createField("mobile", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 用户电话号码
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PHONE = createField("phone", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 数据最后一次同步时间
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> LAST_SYNC = createField("last_sync", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * 职业
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> JOB = createField("job", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 出生年份，四位整数
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Short> BIRTH_YEAR = createField("birth_year", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * 客户属性变更时间（仅限统一客户信息表uni_user中包含的属性）
	 */
	public final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> CHANGED = createField("changed", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	public PltTaobaoCustomer() {
		super("plt_taobao_customer", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public PltTaobaoCustomer(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.PltTaobaoCustomer.PLT_TAOBAO_CUSTOMER);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_CUSTOMER_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_PLT_TAOBAO_CUSTOMER_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.PltTaobaoCustomer as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.PltTaobaoCustomer(alias);
	}
}
