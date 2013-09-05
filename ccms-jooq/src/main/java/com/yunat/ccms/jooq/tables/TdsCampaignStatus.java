/**
 * This class is generated by jOOQ
 */
package com.yunat.ccms.jooq.tables;

/**
 * This class is generated by jOOQ.
 *
 * 活动状态表
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.3"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TdsCampaignStatus extends org.jooq.impl.UpdatableTableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 1593613930;

	/**
	 * The singleton instance of ccms.tds_campaign_status
	 */
	public static final com.yunat.ccms.jooq.tables.TdsCampaignStatus TDS_CAMPAIGN_STATUS = new com.yunat.ccms.jooq.tables.TdsCampaignStatus();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * 活动状态ID （定义 待补充）
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> STATUS_ID = createField("status_id", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 活动状态显示值
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> STATUS_VALUE = createField("status_value", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * 顺序
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Byte> ORDERID = createField("orderid", org.jooq.impl.SQLDataType.TINYINT, this);

	public TdsCampaignStatus() {
		super("tds_campaign_status", com.yunat.ccms.jooq.Ccms.CCMS);
	}

	public TdsCampaignStatus(java.lang.String alias) {
		super(alias, com.yunat.ccms.jooq.Ccms.CCMS, com.yunat.ccms.jooq.tables.TdsCampaignStatus.TDS_CAMPAIGN_STATUS);
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.Record> getMainKey() {
		return com.yunat.ccms.jooq.Keys.KEY_TDS_CAMPAIGN_STATUS_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.Record>>asList(com.yunat.ccms.jooq.Keys.KEY_TDS_CAMPAIGN_STATUS_PRIMARY);
	}

	@Override
	public com.yunat.ccms.jooq.tables.TdsCampaignStatus as(java.lang.String alias) {
		return new com.yunat.ccms.jooq.tables.TdsCampaignStatus(alias);
	}
}
