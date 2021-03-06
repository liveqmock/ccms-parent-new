/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TbUserRole extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -530000081;

	/**
	 * The singleton instance of ccms.tb_user_role
	 */
	public static final com.yunat.ccms.jooq.tables.TbUserRole TB_USER_ROLE = new com.yunat.ccms.jooq.tables.TbUserRole();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The table column <code>ccms.tb_user_role.user_id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The table column <code>ccms.tb_user_role.role_id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> ROLE_ID = createField("role_id", org.jooq.impl.SQLDataType.BIGINT, this);

	public TbUserRole() {
		super("tb_user_role", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TbUserRole(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TbUserRole.TB_USER_ROLE);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TB_USER_ROLE_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TB_USER_ROLE_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TbUserRole as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TbUserRole(alias);
	}
}
