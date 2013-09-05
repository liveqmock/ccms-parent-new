package com.yunat.ccms.metadata.metamodel;

import com.yunat.ccms.metadata.ApplicationContextUtil;
import com.yunat.ccms.metadata.Persistable;
import com.yunat.ccms.metadata.Sqlable;
import com.yunat.ccms.metadata.pojo.DatabaseTable;
import com.yunat.ccms.metadata.pojo.QueryTable;
import com.yunat.ccms.metadata.repository.DatabaseTableDao;

/**
 * 元数据：实体，对应一个物理表
 * 
 * @author kevin.jiang 2013-2-26
 */
public class MetaEntity implements Sqlable, Persistable<MetaEntity, QueryTable> {

	private Long tableId;
	private String tableAlias;
	private String tableName;

	private static DatabaseTableDao databaseTableDao;

	static {

		databaseTableDao = (DatabaseTableDao) ApplicationContextUtil.getContext().getBean("databaseTableDao");
	}

	public MetaEntity() {

	}

	protected MetaEntity(DatabaseTable table, String tableAlias) {

		this.tableId = table.getTableId();
		this.tableName = table.getDbName();
		this.tableAlias = tableAlias;
	}

	protected MetaEntity(String tableName, String tableAlias) {

		this.tableName = tableName;
		this.tableAlias = tableAlias;
	}

	protected void setTableAlias(String tableAlias) {

		this.tableAlias = tableAlias;
	}

	protected String getTableAlias() {

		return tableAlias;
	}

	public MetaEntity(DatabaseTable table) {

		this.tableId = table.getTableId();
		this.tableName = table.getDbName();
	}

	public MetaEntity(String tableName) {

		this.tableName = tableName;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String toSql(Boolean useAlias, MetaEntityRegister register) {

		// 注册别名
		if (useAlias) {
			register.findAndRegistEntityByName(this);
		}

		String finalName = this.tableName;
		if (tableAlias != null && !"".equals(tableAlias)) {
			finalName = finalName + " " + tableAlias;
		}
		return finalName;
	}

	@Override
	public MetaEntity loadFromPojo(QueryTable pojo) {

		this.tableId = pojo.getDbTable().getTableId();
		this.tableName = pojo.getDbTable().getDbName();
		return this;
	}

	public MetaEntity loadFromDbTableByName() throws Exception {

		if (this.tableName == null || "".equals(this.tableName)) {
			throw new Exception("table name can't be null.");
		}
		DatabaseTable table = databaseTableDao.findByTableName(this.tableName);
		this.tableId = table.getTableId();
		this.tableName = table.getDbName();
		return this;
	}

	@Override
	public QueryTable genPojo() {

		QueryTable queryTable = new QueryTable();
		queryTable.setDbTable(databaseTableDao.findByTableName(this.tableName));
		return queryTable;
	}
}
