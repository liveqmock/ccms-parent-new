/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 短信节点生成抽样数据
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TwfNodeSmsSampleRecord extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -822314916;

	/**
	 * The singleton instance of ccms.twf_node_sms_sample_record
	 */
	public static final com.yunat.ccms.jooq.tables.TwfNodeSmsSampleRecord TWF_NODE_SMS_SAMPLE_RECORD = new com.yunat.ccms.jooq.tables.TwfNodeSmsSampleRecord();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The table column <code>ccms.twf_node_sms_sample_record.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * 节点ID
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> NODE_ID = createField("node_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The table column <code>ccms.twf_node_sms_sample_record.subjob_id</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> SUBJOB_ID = createField("subjob_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The table column <code>ccms.twf_node_sms_sample_record.uni_id</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> UNI_ID = createField("uni_id", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>ccms.twf_node_sms_sample_record.content</code>
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CONTENT = createField("content", org.jooq.impl.SQLDataType.VARCHAR, this);

	public TwfNodeSmsSampleRecord() {
		super("twf_node_sms_sample_record", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TwfNodeSmsSampleRecord(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TwfNodeSmsSampleRecord.TWF_NODE_SMS_SAMPLE_RECORD);
	}

	@Override
	public org.jooq.Identity<org.jooq.Record, java.lang.Long> getIdentity() {
		return com.yunat.ccms.jooq.Keys.IDENTITY_TWF_NODE_SMS_SAMPLE_RECORD;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TWF_NODE_SMS_SAMPLE_RECORD_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TWF_NODE_SMS_SAMPLE_RECORD_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TwfNodeSmsSampleRecord as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TwfNodeSmsSampleRecord(alias);
	}
}
