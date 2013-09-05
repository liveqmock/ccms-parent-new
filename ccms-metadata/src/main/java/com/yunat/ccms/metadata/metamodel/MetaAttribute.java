package com.yunat.ccms.metadata.metamodel;

import java.util.ArrayList;
import java.util.List;

import com.yunat.ccms.metadata.ApplicationContextUtil;
import com.yunat.ccms.metadata.Faceable;
import com.yunat.ccms.metadata.Persistable;
import com.yunat.ccms.metadata.Sqlable;
import com.yunat.ccms.metadata.face.FaceAttribute;
import com.yunat.ccms.metadata.pojo.QueryColumn;
import com.yunat.ccms.metadata.quota.Quota;
import com.yunat.ccms.metadata.repository.QueryColumnDao;

/**
 * 元数据：属性，对应一个字段
 * 
 * @author kevin.jiang 2013-2-26
 */
public class MetaAttribute implements Sqlable, Faceable<MetaAttribute, FaceAttribute>,
		Persistable<MetaAttribute, QueryColumn> {

	private final String baseExpr = "?";

	private Long key; // QueryColumn的主键ID
	private Long columnId;
	private String columnName;

	private Quota quotaType;

	private String showName;
	private String columnExpr;
	private String columnAlias;
	private MetaEntity metaEntity;
	private Boolean isMockColumn = false;
	private Boolean isQuotaColumn = false;

	private static QueryColumnDao queryColumnDao;

	static {

		queryColumnDao = (QueryColumnDao) ApplicationContextUtil.getContext().getBean("queryColumnDao");
	}

	public MetaAttribute() {

	}

	public MetaAttribute(Quota quota) {

		this.quotaType = quota;
		this.showName = this.quotaType.getChineseName();
		this.isQuotaColumn = true;
	}

	/**
	 * 用伪列构造属性
	 * 
	 * @param mockColumnName
	 * @param columnAlias
	 */
	public MetaAttribute(String mockColumnName, String columnAlias) {
		this.columnName = mockColumnName;
		this.columnAlias = columnAlias;
		this.isMockColumn = true;
	}

	public MetaAttribute(MetaEntity entity, String columnName) {

		this.metaEntity = entity;
		this.columnName = columnName;
	}

	public MetaAttribute(MetaEntity entity, String columnName, String columnExpr) {

		this.metaEntity = entity;
		this.columnName = columnName;
		this.columnExpr = columnExpr;
	}

	protected void setMetaEntity(MetaEntity metaEntity) {
		this.metaEntity = metaEntity;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public MetaEntity getMetaEntity() {
		return metaEntity;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}

	public Boolean getIsMockColumn() {
		return isMockColumn;
	}

	public void setIsMockColumn(Boolean isMockColumn) {
		this.isMockColumn = isMockColumn;
	}

	protected Boolean getIsQuotaColumn() {
		return isQuotaColumn;
	}

	protected void setIsQuotaColumn(Boolean isQuotaColumn) {
		this.isQuotaColumn = isQuotaColumn;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Quota getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(Quota quotaType) {
		this.quotaType = quotaType;
	}

	public String toSql(Boolean useAlias, MetaEntityRegister register) {

		// 查询中的指标字段，需要表别名处理，无需字段别名处理
		if (this.isQuotaColumn) {

			List<MetaEntity> list = new ArrayList<MetaEntity>();

			for (String tableName : this.quotaType.getOrderedTableNameList()) {

				MetaEntity entity = new MetaEntity(tableName);
				list.add(entity);
			}

			String finalSql = this.quotaType.toSql();

			int index = 0;
			for (MetaEntity entity : list) {

				// 使用别名
				if (useAlias) {

					register.findAndRegistEntityByName(entity);
					finalSql = finalSql.replaceAll("[?]" + index, list.get(0).getTableAlias());
				} else {

					finalSql = finalSql.replaceAll("[?]" + index, list.get(0).getTableName());
				}

				index++;
			}

			return finalSql;
		}

		// 普通字段处理
		String finalExpr = baseExpr;
		if (columnExpr != null && !"".equals(columnExpr)) {
			finalExpr = columnExpr;
		}

		// 注册别名
		if (useAlias && !this.isMockColumn) {
			register.findAndRegistEntityByName(this.metaEntity);
		}

		String finalName = columnName;
		if (!this.isMockColumn) {
			finalName = metaEntity.getTableName() + "." + columnName;

			if (metaEntity.getTableAlias() != null && !"".equals(metaEntity.getTableAlias())) {
				finalName = metaEntity.getTableAlias() + "." + columnName;
			} else {
				finalName = metaEntity.getTableName() + "." + columnName;
			}
		}

		if (this.columnAlias != null) {
			finalName = finalName + " as " + this.columnAlias;
		}

		return finalExpr.replaceFirst("[?]", finalName);
	}

	public MetaAttribute loadFromPojo(QueryColumn pojo) {

		this.key = pojo.getQueryColumnId();
		this.columnId = pojo.getDatabaseColumn().getColumnId();
		this.columnName = pojo.getDatabaseColumn().getColumnName();

		this.metaEntity = (new MetaEntity()).loadFromPojo(pojo.getQueryTable());
		this.columnExpr = pojo.getColumnExpr();
		this.showName = pojo.getColumnName();
		if (this.showName == null || "".equals(this.showName)) {

			this.showName = pojo.getDatabaseColumn().getShowName();
		}

		return this;
	}

	public QueryColumn genPojo() {

		return queryColumnDao.findOne(this.key);
	}

	public MetaAttribute parseFace(FaceAttribute face) {

		QueryColumn queryColumn = queryColumnDao.findOne(this.key);

		this.columnName = queryColumn.getDatabaseColumn().getColumnName();
		this.columnId = queryColumn.getDatabaseColumn().getColumnId();
		this.metaEntity = new MetaEntity(queryColumn.getDatabaseColumn().getTable());
		this.showName = queryColumn.getColumnName();
		if (this.showName == null || "".equals(this.showName)) {

			this.showName = queryColumn.getDatabaseColumn().getShowName();
		}

		return this;
	}

	public FaceAttribute genFace() {

		FaceAttribute faceAttr = new FaceAttribute();
		faceAttr.setKey(String.valueOf(this.key));
		faceAttr.setName(this.showName);
		return faceAttr;
	}
}
